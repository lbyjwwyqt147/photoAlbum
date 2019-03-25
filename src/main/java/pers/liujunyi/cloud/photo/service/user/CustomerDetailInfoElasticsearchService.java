package pers.liujunyi.cloud.photo.service.user;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseElasticsearchService;
import pers.liujunyi.cloud.photo.domain.user.CustomerDetailInfoQueryDto;
import pers.liujunyi.cloud.photo.domain.user.CustomerDetailInfoVo;
import pers.liujunyi.cloud.photo.entity.user.CustomerDetailInfo;


/***
 * 文件名称: CustomerDetailInfoElasticsearchService.java
 * 文件描述: 顾客档案信息 Elasticsearch Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface CustomerDetailInfoElasticsearchService extends BaseElasticsearchService<CustomerDetailInfo, Long> {

    /**
     * 根据 帐号id 获取顾客详细数据
     * @param customerAccountsId
     * @param customerStatus  状态 0：正常  1：禁用
     * @return
     */
    CustomerDetailInfo findFirstByCustomerAccountsIdAndCustomerStatus(Long customerAccountsId, Byte customerStatus);

    /**
     * 根据 帐号id 获取顾客详细数据
     * @param customerAccountsId
     * @return
     */
    CustomerDetailInfo findCustomerDetails(Long customerAccountsId);

    /**
     * 分页列表
     * @param query
     * @return
     */
    ResultInfo findPageGird(CustomerDetailInfoQueryDto query);

    /**
     * 根据 帐号id 获取顾客详细数据
     * @param customerAccountsId
     * @return
     */
    ResultInfo findByCustomerAccountsId(Long customerAccountsId);

    /**
     * 根据 帐号id 获取顾客详细数据
     * @param customerAccountsId
     * @return
     */
    CustomerDetailInfoVo getCustomerDetailsByCustomerAccountsId(Long customerAccountsId);

    /**
     * 根据 帐号id 获取顾客详细数据
     * @param id
     * @return
     */
    ResultInfo findById(Long id);

    /**
     * 根据 id 获取顾客详细数据
     * @param id
     * @return
     */
    CustomerDetailInfoVo getCustomerDetailsById(Long id);

}
