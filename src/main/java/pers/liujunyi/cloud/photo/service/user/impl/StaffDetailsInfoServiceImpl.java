package pers.liujunyi.cloud.photo.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.exception.ErrorCodeEnum;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseServiceImpl;
import pers.liujunyi.cloud.common.util.DozerBeanMapperUtil;
import pers.liujunyi.cloud.common.util.UserUtils;
import pers.liujunyi.cloud.photo.domain.user.StaffDetailsInfoDto;
import pers.liujunyi.cloud.photo.entity.user.StaffDetailsInfo;
import pers.liujunyi.cloud.photo.repository.elasticsearch.user.StaffDetailsInfoElasticsearchRepository;
import pers.liujunyi.cloud.photo.repository.jpa.user.StaffDetailsInfoRepository;
import pers.liujunyi.cloud.photo.service.user.StaffDetailsInfoService;
import pers.liujunyi.cloud.photo.util.Constant;
import pers.liujunyi.cloud.security.domain.user.UserAccountsDto;
import pers.liujunyi.cloud.security.domain.user.UserAccountsUpdateDto;
import pers.liujunyi.cloud.security.entity.organizations.StaffOrg;
import pers.liujunyi.cloud.security.service.organizations.StaffOrgService;
import pers.liujunyi.cloud.security.service.user.UserAccountsService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/***
 * 文件名称: StaffDetailsInfoServiceImpl.java
 * 文件描述: 职工档案信息 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class StaffDetailsInfoServiceImpl extends BaseServiceImpl<StaffDetailsInfo, Long> implements StaffDetailsInfoService {

    @Autowired
    private StaffDetailsInfoRepository staffDetailsInfoRepository;
    @Autowired
    private StaffDetailsInfoElasticsearchRepository staffDetailsInfoElasticsearchRepository;
    @Autowired
    private UserAccountsService userAccountsService;
    @Autowired
    private StaffOrgService staffOrgService;
    @Autowired
    private UserUtils userUtils;


    public StaffDetailsInfoServiceImpl(BaseRepository<StaffDetailsInfo, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(StaffDetailsInfoDto record) {
        // 先保存账户信息
        ResultInfo result = this.saveUserAccountRecord(record);
        record.setStaffPhone(record.getMobilePhone());
        if (result.getSuccess()) {
            if (record.getStaffCategory() == null) {
                record.setStaffCategory((byte)2);
            }
            if (record.getStaffStatus() == null) {
                record.setStaffStatus(Constant.ENABLE_STATUS);
            }
            if (record.getId() != null) {
                record.setUpdateTime(new Date());
                record.setUpdateUserId(this.userUtils.getPresentLoginUserId());
            } else {
                record.setStaffAccountsId(Long.valueOf(result.getData().toString()));
            }
            StaffDetailsInfo staffDetailsInfo = DozerBeanMapperUtil.copyProperties(record, StaffDetailsInfo.class);
            StaffDetailsInfo saveObj = this.staffDetailsInfoRepository.save(staffDetailsInfo);
            if (saveObj != null && saveObj.getId() != null) {
                result.setData(saveObj.getId());
                record.setId(saveObj.getId());
                this.saveStaffOrg(record);
                this.staffDetailsInfoElasticsearchRepository.save(saveObj);
            }else {
                result.setSuccess(false);
                result.setStatus(ErrorCodeEnum.FAIL.getCode());
            }
        }
        return result;
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids, List<Long> userIds, String putParams) {
        int count = this.staffDetailsInfoRepository.setStaffStatusByIds(status, new Date(), ids);
        if (count > 0) {
            Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            Map<String, Object> docDataMap = new HashMap<>();
            docDataMap.put("staffStatus", status);
            docDataMap.put("updateTime", System.currentTimeMillis());
            ids.stream().forEach(item -> {
                sourceMap.put(String.valueOf(item), docDataMap);
            });
            super.updateBatchElasticsearchData(sourceMap);
            this.userAccountsService.updateStatus(status.byteValue() == Constant.ENABLE_STATUS.byteValue() ? Constant.ENABLE_STATUS : Constant.DISABLE_STATUS, userIds, putParams);
            this.staffOrgService.updateStatusByStaffIds(status, ids);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo deleteBatch(List<Long> ids, List<Long> userIds) {
        long count = this.staffDetailsInfoRepository.deleteByIdIn(ids);
        if (count > 0) {
            this.staffDetailsInfoElasticsearchRepository.deleteByIdIn(ids);
            this.userAccountsService.deleteBatch(userIds);
            this.staffOrgService.deleteByStaffIds(ids);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo deleteSingle(Long id, Long userId) {
        this.staffDetailsInfoRepository.deleteById(id);
        this.staffDetailsInfoElasticsearchRepository.deleteById(id);
        this.userAccountsService.deleteSingle(userId);
        this.staffOrgService.deleteByStaffId(id);
        return ResultUtil.success();
    }

    @Override
    public ResultInfo syncDataToElasticsearch() {
        // 先同步账户信息
        this.userAccountsService.userAccountsSyncDataToElasticsearch();
        Sort sort =  new Sort(Sort.Direction.ASC, "id");
        List<StaffDetailsInfo> list = this.staffDetailsInfoRepository.findAll(sort);
        if (!CollectionUtils.isEmpty(list)) {
            this.staffDetailsInfoElasticsearchRepository.deleteAll();
            // 限制条数
            int pointsDataLimit = 1000;
            int size = list.size();
            //判断是否有必要分批
            if(pointsDataLimit < size){
                //分批数
                int part = size/pointsDataLimit;
                for (int i = 0; i < part; i++) {
                    //1000条
                    List<StaffDetailsInfo> partList = new LinkedList<>(list.subList(0, pointsDataLimit));
                    //剔除
                    list.subList(0, pointsDataLimit).clear();
                    this.staffDetailsInfoElasticsearchRepository.saveAll(partList);
                }
                //表示最后剩下的数据
                if (!CollectionUtils.isEmpty(list)) {
                    this.staffDetailsInfoElasticsearchRepository.saveAll(list);
                }
            } else {
                this.staffDetailsInfoElasticsearchRepository.saveAll(list);
            }
        } else {
            this.staffDetailsInfoElasticsearchRepository.deleteAll();
        }
        // 同步人员机构
        this.staffOrgService.syncDataToElasticsearch();
        return ResultUtil.success();
    }

    @Override
    public ResultInfo setPortrait(Long id, String portrait, Long portraitId) {
        int count = this.staffDetailsInfoRepository.setPortrait(id, portrait, portraitId, new Date());
        if (count > 0) {
            Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            Map<String, Object> docDataMap = new HashMap<>();
            docDataMap.put("staffPortrait", portrait);
            docDataMap.put("staffPortraitId", portraitId);
            docDataMap.put("updateTime", System.currentTimeMillis());
            sourceMap.put(id.toString(), docDataMap);
            super.updateBatchElasticsearchData(sourceMap);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo setCurDimissionInfo(Long id, Long userId, Date date, String dimissionReason, Long dataVersion) {
        StaffDetailsInfo staffDetailsInfo = new StaffDetailsInfo();
        staffDetailsInfo.setId(id);
        staffDetailsInfo.setStaffStatus((byte) 2);
        staffDetailsInfo.setDimissionDate(date);
        staffDetailsInfo.setDimissionReason(dimissionReason);
        StaffDetailsInfo detailsInfo = this.staffDetailsInfoRepository.save(staffDetailsInfo);
        if (detailsInfo != null) {
            Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            Map<String, Object> docDataMap = new HashMap<>();
            docDataMap.put("dimissionDate", staffDetailsInfo.getDimissionDate());
            docDataMap.put("staffStatus", staffDetailsInfo.getStaffStatus());
            docDataMap.put("dimissionReason", staffDetailsInfo.getDimissionReason());
            docDataMap.put("updateTime", System.currentTimeMillis());
            sourceMap.put(id.toString(), docDataMap);
            super.updateBatchElasticsearchData(sourceMap);
            this.userAccountsService.updateStatus(Constant.DISABLE_STATUS, userId, dataVersion);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    /**
     * 保存用户帐号信息
     * @param record
     * @return
     */
    private ResultInfo saveUserAccountRecord(StaffDetailsInfoDto record) {
        if (record.getId() == null) {
            UserAccountsDto userAccounts = new UserAccountsDto();
            userAccounts.setMobilePhone(record.getMobilePhone());
            userAccounts.setUserAccounts(record.getMobilePhone());
            userAccounts.setUserMailbox(record.getStaffEmail());
            userAccounts.setUserName(record.getStaffName());
            userAccounts.setUserNickName(record.getStaffNickName());
            userAccounts.setUserNumber(record.getStaffNumber());
            userAccounts.setUserPassword(record.getMobilePhone());
            userAccounts.setRegisteredSource((byte) 1);
            userAccounts.setUserCategory(Constant.USER_CATEGORY_STAFF);
            return this.userAccountsService.saveRecord(userAccounts);
        } else {
            UserAccountsUpdateDto userAccountsUpdate = new UserAccountsUpdateDto();
            userAccountsUpdate.setRegisteredSource((byte) 1);
            userAccountsUpdate.setId(record.getStaffAccountsId());
            userAccountsUpdate.setMobilePhone(record.getMobilePhone());
            userAccountsUpdate.setUserAccounts(record.getMobilePhone());
            userAccountsUpdate.setUserMailbox(record.getStaffEmail());
            userAccountsUpdate.setUserName(record.getStaffName());
            userAccountsUpdate.setUserNickName(record.getStaffNickName());
            userAccountsUpdate.setUserNumber(record.getStaffNumber());
            userAccountsUpdate.setDataVersion(record.getDataVersion());
            return this.userAccountsService.updateUserAccountsInfo(userAccountsUpdate);
        }
    }

    /**
     * 保存 职工 所属组织机构 关联数据
     * @param record
     */
    private void saveStaffOrg(StaffDetailsInfoDto record) {
        this.staffOrgService.deleteByStaffId(record.getId());
        StaffOrg staffOrg = new StaffOrg();
        staffOrg.setFullParent(record.getStaffFullParent());
        staffOrg.setOrgId(record.getStaffOrgId());
        staffOrg.setOrgNumber(record.getStaffOrgNumber());
        List<Long> staffIdList = new CopyOnWriteArrayList<>();
        staffIdList.add(record.getId());
        this.staffOrgService.saveRecord(staffOrg, staffIdList);
    }
}
