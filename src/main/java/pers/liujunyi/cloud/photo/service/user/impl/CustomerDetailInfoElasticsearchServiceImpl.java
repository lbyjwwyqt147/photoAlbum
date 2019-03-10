package pers.liujunyi.cloud.photo.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.photo.entity.user.CustomerDetailInfo;
import pers.liujunyi.cloud.photo.repository.elasticsearch.user.CustomerDetailInfoElasticsearchRepository;
import pers.liujunyi.cloud.photo.service.user.CustomerDetailInfoElasticsearchService;
import pers.liujunyi.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.common.service.impl.BaseElasticsearchServiceImpl;

/***
 * 文件名称: CustomerDetailInfoElasticsearchServiceImpl.java
 * 文件描述: 顾客档案信息 Elasticsearch Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class CustomerDetailInfoElasticsearchServiceImpl extends BaseElasticsearchServiceImpl<CustomerDetailInfo, Long> implements CustomerDetailInfoElasticsearchService {

    @Autowired
    private CustomerDetailInfoElasticsearchRepository customerDetailInfoElasticsearchRepository;

    public CustomerDetailInfoElasticsearchServiceImpl(BaseElasticsearchRepository<CustomerDetailInfo, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }


}
