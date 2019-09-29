package pers.liujunyi.cloud.photo.service.setting;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseService;
import pers.liujunyi.cloud.photo.domain.setting.CompanySettingDto;
import pers.liujunyi.cloud.photo.entity.setting.CompanySetting;

/***
 * 文件名称: CompanySettingService.java
 * 文件描述:  公司信息设置 Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年09月29日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface CompanySettingService extends BaseService<CompanySetting, Long> {

    /**
     * 保存数据
     * @param record
     * @return
     */
    ResultInfo saveRecord(CompanySettingDto record);



    /**
     * 同步数据到es中
     * @return
     */
    ResultInfo syncDataToElasticsearch();


}
