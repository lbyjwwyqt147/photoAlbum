package pers.liujunyi.cloud.photo.service.setting.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseServiceImpl;
import pers.liujunyi.cloud.common.util.DozerBeanMapperUtil;
import pers.liujunyi.cloud.common.util.FileManageUtil;
import pers.liujunyi.cloud.photo.domain.setting.CompanySettingDto;
import pers.liujunyi.cloud.photo.entity.setting.CompanySetting;
import pers.liujunyi.cloud.photo.repository.elasticsearch.setting.CompanySettingElasticsearchRepository;
import pers.liujunyi.cloud.photo.repository.jpa.setting.CompanySettingRepository;
import pers.liujunyi.cloud.photo.service.setting.CompanySettingService;

import java.util.List;

/***
 * 文件名称: CompanySettingServiceImpl.java
 * 文件描述: 公司信息设置 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年09月29日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class CompanySettingServiceImpl extends BaseServiceImpl<CompanySetting, Long> implements CompanySettingService {


    @Autowired
    private CompanySettingRepository companySettingRepository;
    @Autowired
    private CompanySettingElasticsearchRepository companySettingElasticsearchRepository;
    @Autowired
    private FileManageUtil fileManageUtil;

    public CompanySettingServiceImpl(BaseRepository<CompanySetting, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(CompanySettingDto record) {
        CompanySetting companySetting = DozerBeanMapperUtil.copyProperties(record, CompanySetting.class);
        CompanySetting saveObject = this.companySettingRepository.save(companySetting);
        if (saveObject.getId() != null) {
            this.companySettingElasticsearchRepository.save(saveObject);
        }
        return ResultUtil.success();
    }


    @Override
    public ResultInfo syncDataToElasticsearch() {
        List<CompanySetting> companySettingList = this.companySettingRepository.findAll();
        if (!CollectionUtils.isEmpty(companySettingList)) {
            this.companySettingElasticsearchRepository.deleteAll();
            this.companySettingElasticsearchRepository.saveAll(companySettingList);
        } else {
            this.companySettingElasticsearchRepository.deleteAll();
        }
        return ResultUtil.success();
    }

}
