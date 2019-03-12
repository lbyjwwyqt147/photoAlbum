package pers.liujunyi.cloud.photo.service.permission.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.photo.entity.permission.StaffOrg;
import pers.liujunyi.cloud.photo.repository.elasticsearch.permission.StaffOrgElasticsearchRepository;
import pers.liujunyi.cloud.photo.service.permission.StaffOrgElasticsearchService;
import pers.liujunyi.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.common.service.impl.BaseElasticsearchServiceImpl;

/***
 * 文件名称: StaffOrgElasticsearchServiceImpl.java
 * 文件描述: 职工关联组织机构 Elasticsearch Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class StaffOrgElasticsearchServiceImpl extends BaseElasticsearchServiceImpl<StaffOrg, Long> implements StaffOrgElasticsearchService {

    @Autowired
    private StaffOrgElasticsearchRepository staffOrgElasticsearchRepository;

    public StaffOrgElasticsearchServiceImpl(BaseElasticsearchRepository<StaffOrg, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }


}