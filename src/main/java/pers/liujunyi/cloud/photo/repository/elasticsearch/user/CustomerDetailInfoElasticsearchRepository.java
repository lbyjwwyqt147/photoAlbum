package pers.liujunyi.cloud.photo.repository.elasticsearch.user;

import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.photo.entity.user.CustomerDetailInfo;

/***
 * 文件名称: CustomerDetailInfoElasticsearchRepository.java
 * 文件描述: 顾客档案信息 Elasticsearch Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface CustomerDetailInfoElasticsearchRepository extends BaseElasticsearchRepository<CustomerDetailInfo, Long> {

    /**
     * 根据 帐号id 获取顾客详细数据
     * @param customerAccountsId
     * @param customerStatus  状态 0：正常  1：禁用
     * @return
     */
    CustomerDetailInfo findFirstByCustomerAccountsIdAndCustomerStatus(Long customerAccountsId, Byte customerStatus);



}
