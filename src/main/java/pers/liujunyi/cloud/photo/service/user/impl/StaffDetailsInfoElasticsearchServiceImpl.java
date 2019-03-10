package pers.liujunyi.cloud.photo.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.photo.entity.StaffDetailsInfo;
import pers.liujunyi.cloud.photo.repository.elasticsearch.user.StaffDetailsInfoElasticsearchRepository;
import pers.liujunyi.cloud.photo.service.user.StaffDetailsInfoElasticsearchService;
import pers.liujunyi.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.common.service.impl.BaseElasticsearchServiceImpl;

/***
 * 文件名称: StaffDetailsInfoElasticsearchServiceImpl.java
 * 文件描述: 职工档案信息 Elasticsearch Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class StaffDetailsInfoElasticsearchServiceImpl extends BaseElasticsearchServiceImpl<StaffDetailsInfo, Long> implements StaffDetailsInfoElasticsearchService {

    @Autowired
    private StaffDetailsInfoElasticsearchRepository staffDetailsInfoElasticsearchRepository;

    public StaffDetailsInfoElasticsearchServiceImpl(BaseElasticsearchRepository<StaffDetailsInfo, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }


}
