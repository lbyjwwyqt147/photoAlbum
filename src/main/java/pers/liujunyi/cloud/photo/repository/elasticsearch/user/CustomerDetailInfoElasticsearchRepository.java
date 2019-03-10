package pers.liujunyi.cloud.photo.repository.elasticsearch.user;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.jpa.repository.Modifying;
import pers.liujunyi.cloud.photo.entity.user.CustomerDetailInfo;
import pers.liujunyi.common.repository.elasticsearch.BaseElasticsearchRepository;

import java.util.Date;
import java.util.List;

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
     * 修改状态
     * @param customerStatus  0：正常  1：禁用
     * @param ids
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query("update CustomerDetailInfo u set u.customerStatus = ?1, u.updateTime = ?2 where u.id in (?3)")
    int setCustomerStatusByIds(Byte customerStatus, Date updateTime,  List<Long> ids);


    /**
     * 根据 帐号id 获取顾客详细数据
     * @param customerAccountsId
     * @return
     */
    CustomerDetailInfo findFirstByCustomerAccountsId(Long customerAccountsId);
}
