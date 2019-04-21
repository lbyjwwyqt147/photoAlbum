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
import pers.liujunyi.cloud.photo.entity.StaffDetailsInfo;
import pers.liujunyi.cloud.photo.repository.elasticsearch.user.StaffDetailsInfoElasticsearchRepository;
import pers.liujunyi.cloud.photo.repository.jpa.user.StaffDetailsInfoRepository;
import pers.liujunyi.cloud.photo.service.user.StaffDetailsInfoService;
import pers.liujunyi.cloud.photo.util.Constant;
import pers.liujunyi.cloud.security.domain.user.UserAccountsDto;
import pers.liujunyi.cloud.security.domain.user.UserAccountsUpdateDto;
import pers.liujunyi.cloud.security.service.user.UserAccountsService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    private UserUtils userUtils;


    public StaffDetailsInfoServiceImpl(BaseRepository<StaffDetailsInfo, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(StaffDetailsInfoDto record) {
        ResultInfo result = this.saveUserAccountRecord(record);
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
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo batchDeletes(List<Long> ids, List<Long> userIds) {
        long count = this.staffDetailsInfoRepository.deleteByIdIn(ids);
        if (count > 0) {
            this.staffDetailsInfoElasticsearchRepository.deleteByIdIn(ids);
            this.userAccountsService.batchDeletes(userIds);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo singleDelete(Long id, Long userId) {
        this.staffDetailsInfoRepository.deleteById(id);
        this.staffDetailsInfoElasticsearchRepository.deleteById(id);
        this.userAccountsService.singleDelete(userId);
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
        return ResultUtil.success();
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
            return this.userAccountsService.saveRecord(userAccounts);
        } else {
            UserAccountsUpdateDto userAccountsUpdate = new UserAccountsUpdateDto();
            userAccountsUpdate.setId(record.getStaffAccountsId());
            userAccountsUpdate.setMobilePhone(record.getMobilePhone());
            userAccountsUpdate.setUserAccounts(record.getMobilePhone());
            userAccountsUpdate.setUserMailbox(record.getStaffEmail());
            userAccountsUpdate.setUserName(record.getStaffName());
            userAccountsUpdate.setUserNickName(record.getStaffNickName());
            userAccountsUpdate.setUserNumber(record.getStaffNumber());
            return this.userAccountsService.updateUserAccountsInfo(userAccountsUpdate);
        }
    }
}
