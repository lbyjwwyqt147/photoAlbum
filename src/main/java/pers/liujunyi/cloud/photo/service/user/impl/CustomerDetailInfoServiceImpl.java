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
import pers.liujunyi.cloud.photo.domain.user.CustomerDetailInfoDto;
import pers.liujunyi.cloud.photo.entity.user.CustomerDetailInfo;
import pers.liujunyi.cloud.photo.repository.elasticsearch.user.CustomerDetailInfoElasticsearchRepository;
import pers.liujunyi.cloud.photo.repository.jpa.user.CustomerDetailInfoRepository;
import pers.liujunyi.cloud.photo.service.user.CustomerDetailInfoService;
import pers.liujunyi.cloud.photo.util.Constant;
import pers.liujunyi.cloud.photo.util.SerialNumberUtils;
import pers.liujunyi.cloud.security.domain.user.UserAccountsDto;
import pers.liujunyi.cloud.security.domain.user.UserAccountsUpdateDto;
import pers.liujunyi.cloud.security.service.user.UserAccountsService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 文件名称: CustomerDetailInfoServiceImpl.java
 * 文件描述: 顾客档案信息 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class CustomerDetailInfoServiceImpl extends BaseServiceImpl<CustomerDetailInfo, Long> implements CustomerDetailInfoService {

    @Autowired
    private CustomerDetailInfoRepository customerDetailInfoRepository;
    @Autowired
    private CustomerDetailInfoElasticsearchRepository customerDetailInfoElasticsearchRepository;
    @Autowired
    private UserAccountsService userAccountsService;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private SerialNumberUtils serialNumberUtils;


    public CustomerDetailInfoServiceImpl(BaseRepository<CustomerDetailInfo, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(CustomerDetailInfoDto record) {
        ResultInfo result = this.saveUserAccountRecord(record);
        if (result.getSuccess()) {
            if (record.getCustomerCategory() == null) {
                record.setCustomerCategory(Constant.ENABLE_STATUS);
            }
            if (record.getCustomerGrade() == null) {
                record.setCustomerGrade(Constant.ENABLE_STATUS);
            }
            if (record.getCustomerStatus() == null) {
                record.setCustomerStatus(Constant.ENABLE_STATUS);
            }
            if (record.getId() != null) {
                record.setUpdateTime(new Date());
                record.setUpdateUserId(this.userUtils.getPresentLoginUserId());
            } else {
                record.setCustomerNumber(this.serialNumberUtils.getCurrentCustomerNumber());
                record.setCustomerAccountsId(Long.valueOf(result.getData().toString()));
            }
            CustomerDetailInfo customerDetailInfo = DozerBeanMapperUtil.copyProperties(record, CustomerDetailInfo.class);
            CustomerDetailInfo saveObj = this.customerDetailInfoRepository.save(customerDetailInfo);
            if (saveObj != null && saveObj.getId() != null) {
                this.customerDetailInfoElasticsearchRepository.save(saveObj);
            }else {
                result.setSuccess(false);
                result.setStatus(ErrorCodeEnum.FAIL.getCode());
            }
        }
        return result;
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids, List<Long> userIds) {
        int count = this.customerDetailInfoRepository.setCustomerStatusByIds(status, new Date(), ids);
        if (count > 0) {
            Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            Map<String, Object> docDataMap = new HashMap<>();
            docDataMap.put("customerStatus", status);
            docDataMap.put("updateTime", System.currentTimeMillis());
            ids.stream().forEach(item -> {
                sourceMap.put(String.valueOf(item), docDataMap);
            });
            super.updateBatchElasticsearchData(sourceMap);
            this.userAccountsService.updateStatus(status, userIds);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo batchDeletes(List<Long> ids, List<Long> userIds) {
        long count = this.customerDetailInfoRepository.deleteByIdIn(ids);
        if (count > 0) {
            this.customerDetailInfoElasticsearchRepository.deleteByIdIn(ids);
            this.userAccountsService.batchDeletes(userIds);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo singleDelete(Long id, Long userId) {
        this.customerDetailInfoRepository.deleteById(id);
        this.customerDetailInfoElasticsearchRepository.deleteById(id);
        this.userAccountsService.singleDelete(userId);
        return ResultUtil.success();
    }

    @Override
    public ResultInfo syncDataToElasticsearch() {
        // 先同步账户信息
        this.userAccountsService.userAccountsSyncDataToElasticsearch();
        Sort sort =  new Sort(Sort.Direction.ASC, "id");
        List<CustomerDetailInfo> list = this.customerDetailInfoRepository.findAll(sort);
        if (!CollectionUtils.isEmpty(list)) {
            this.customerDetailInfoElasticsearchRepository.deleteAll();
            // 限制条数
            int pointsDataLimit = 1000;
            int size = list.size();
            //判断是否有必要分批
            if(pointsDataLimit < size){
                //分批数
                int part = size/pointsDataLimit;
                for (int i = 0; i < part; i++) {
                    //1000条
                    List<CustomerDetailInfo> partList = new LinkedList<>(list.subList(0, pointsDataLimit));
                    //剔除
                    list.subList(0, pointsDataLimit).clear();
                    this.customerDetailInfoElasticsearchRepository.saveAll(partList);
                }
                //表示最后剩下的数据
                if (!CollectionUtils.isEmpty(list)) {
                    this.customerDetailInfoElasticsearchRepository.saveAll(list);
                }
            } else {
                this.customerDetailInfoElasticsearchRepository.saveAll(list);
            }
        } else {
            this.customerDetailInfoElasticsearchRepository.deleteAll();
        }
        return ResultUtil.success();
    }


    /**
     * 保存用户帐号信息
     * @param record
     * @return
     */
    private ResultInfo saveUserAccountRecord(CustomerDetailInfoDto record) {
        if (record.getId() == null) {
            UserAccountsDto userAccounts = new UserAccountsDto();
            userAccounts.setMobilePhone(record.getMobilePhone());
            userAccounts.setUserAccounts(record.getMobilePhone());
            userAccounts.setUserMailbox(record.getCustomerEmail());
            userAccounts.setUserName(record.getCustomerName());
            userAccounts.setUserNickName(record.getCustomerNickName());
            userAccounts.setUserNumber(record.getCustomerNumber());
            return this.userAccountsService.saveRecord(userAccounts);
        } else {
            UserAccountsUpdateDto userAccountsUpdate = new UserAccountsUpdateDto();
            userAccountsUpdate.setId(record.getCustomerAccountsId());
            userAccountsUpdate.setMobilePhone(record.getMobilePhone());
            userAccountsUpdate.setUserAccounts(record.getMobilePhone());
            userAccountsUpdate.setUserMailbox(record.getCustomerEmail());
            userAccountsUpdate.setUserName(record.getCustomerName());
            userAccountsUpdate.setUserNickName(record.getCustomerNickName());
            return this.userAccountsService.updateUserAccountsInfo(userAccountsUpdate);
        }
    }
}
