package pers.liujunyi.cloud.photo.service.permission.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.photo.entity.permission.Organizations;
import pers.liujunyi.cloud.photo.repository.elasticsearch.permission.OrganizationsElasticsearchRepository;
import pers.liujunyi.cloud.photo.service.permission.OrganizationsElasticsearchService;
import pers.liujunyi.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.common.service.impl.BaseElasticsearchServiceImpl;

/***
 * 文件名称: OrganizationsElasticsearchServiceImpl.java
 * 文件描述: 组织机构 Elasticsearch Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class OrganizationsElasticsearchServiceImpl extends BaseElasticsearchServiceImpl<Organizations, Long> implements OrganizationsElasticsearchService {

    @Autowired
    private OrganizationsElasticsearchRepository organizationsElasticsearchRepository;

    public OrganizationsElasticsearchServiceImpl(BaseElasticsearchRepository<Organizations, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }


}
