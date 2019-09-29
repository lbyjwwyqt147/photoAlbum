package pers.liujunyi.cloud.photo.service.setting.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.common.service.impl.BaseElasticsearchServiceImpl;
import pers.liujunyi.cloud.photo.entity.setting.CompanySetting;
import pers.liujunyi.cloud.photo.repository.elasticsearch.setting.CompanySettingElasticsearchRepository;
import pers.liujunyi.cloud.photo.service.setting.CompanySettingElasticsearchService;

/***
 * 文件名称: CompanySettingElasticsearchServiceImpl.java
 * 文件描述: 公司信息设置 Elasticsearch Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月04日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class CompanySettingElasticsearchServiceImpl extends BaseElasticsearchServiceImpl<CompanySetting, Long> implements CompanySettingElasticsearchService {

    @Autowired
    private CompanySettingElasticsearchRepository companySettingElasticsearchRepository;


    public CompanySettingElasticsearchServiceImpl(BaseElasticsearchRepository<CompanySetting, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }

}
