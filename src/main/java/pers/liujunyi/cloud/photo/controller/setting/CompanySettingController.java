package pers.liujunyi.cloud.photo.controller.setting;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.photo.domain.setting.CompanySettingDto;
import pers.liujunyi.cloud.photo.service.setting.CompanySettingElasticsearchService;
import pers.liujunyi.cloud.photo.service.setting.CompanySettingService;

import javax.validation.Valid;

/***
 * 文件名称: CompanySettingController.java
 * 文件描述: 公司信息设置 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年09月29日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "公司信息设置 API")
@RestController
public class CompanySettingController extends BaseController {

    @Autowired
    private CompanySettingService companySettingService;
    @Autowired
    private CompanySettingElasticsearchService companySettingElasticsearchService;

    /**
     * 保存数据
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "保存数据", notes = "适用于保存数据 请求示例：127.0.0.1:18081/api/v1/verify/cmpany/s")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1")
    })
    @PostMapping(value = "verify/cmpany/s")
    @ApiVersion(1)
    public ResultInfo saveRecord(@Valid CompanySettingDto param) {
        return this.companySettingService.saveRecord(param);
    }

    /**
     *  获取数据详情
     *
     * @param
     * @return
     */
    @ApiOperation(value = "获取数据详情 ", notes = "获取数据详情 请求示例：127.0.0.1:18081/api/v1/table/cmpany/details")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1")
    })
    @GetMapping(value = "table/cmpany/details")
    @ApiVersion(1)
    public ResultInfo findById() {
        return ResultUtil.success(this.companySettingElasticsearchService.findAll());
    }

    /**
     *  同步数据到es中
     * @param
     * @return
     */
    @ApiOperation(value = "同步数据", notes = "同步数据 请求示例：127.0.0.1:18081/api/v1/verify/cmpany/sync")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
    })
    @PostMapping(value = "verify/cmpany/sync")
    @ApiVersion(1)
    public ResultInfo syncDataToElasticsearch() {
        return this.companySettingService.syncDataToElasticsearch();
    }


}
