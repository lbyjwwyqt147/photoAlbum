package pers.liujunyi.cloud.photo.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.photo.domain.user.StaffDetailsInfoDto;
import pers.liujunyi.cloud.photo.domain.user.StaffDetailsInfoQueryDto;
import pers.liujunyi.cloud.photo.service.user.StaffDetailsInfoElasticsearchService;
import pers.liujunyi.cloud.photo.service.user.StaffDetailsInfoService;
import pers.liujunyi.cloud.security.domain.IdParamDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/***
 * 文件名称: StaffDetailsInfoController.java
 * 文件描述: 员工档案 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月22日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "员工档案 API")
@RestController
public class StaffDetailsInfoController extends BaseController {

    @Autowired
    private StaffDetailsInfoService staffDetailsInfoService;
    @Autowired
    private StaffDetailsInfoElasticsearchService staffDetailsInfoElasticsearchService;

    /**
     * 保存数据
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "保存数据", notes = "适用于保存数据 请求示例：127.0.0.1:18080/api/v1/staff/save")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @PostMapping(value = "verify/staff/s")
    @ApiVersion(1)
    public ResultInfo saveRecord(@Valid StaffDetailsInfoDto param) {
        return this.staffDetailsInfoService.saveRecord(param);
    }

    /**
     * 单条删除数据
     *
     * @param id
     * @param userId
     * @return
     */
    @ApiOperation(value = "单条删除数据", notes = "适用于单条删除数据 请求示例：127.0.0.1:18080/api/v1/staff/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "userId", value = "userId",  required = true, dataType = "Long"),
            @ApiImplicitParam(name = "id", value = "id",  required = true, dataType = "Long")

    })
    @DeleteMapping(value = "verify/staff/d")
    @ApiVersion(1)
    public ResultInfo singleDelete(@Valid @NotNull(message = "id 必须填写")
                                       @RequestParam(name = "id", required = true) Long id, @NotNull(message = "userId 必须填写")
    @RequestParam(name = "userId", required = true) Long userId) {
        return this.staffDetailsInfoService.singleDelete(id, userId);
    }

    /**
     * 批量删除
     *
     * @param param 　 多个id 用 , 隔开
     * @return
     */
    @ApiOperation(value = "删除多条数据", notes = "适用于批量删除数据 请求示例：127.0.0.1:18080/api/v1/staff/batchDelete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "otherIds", value = "账户id",  required = true, dataType = "String")
    })
    @DeleteMapping(value = "verify/staff/d/b")
    @ApiVersion(1)
    public ResultInfo batchDelete(@Valid IdParamDto param) {
        return this.staffDetailsInfoService.batchDeletes(param.getIdList(), param.getOtherIdList());
    }

    /**
     * 分页列表数据
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "分页列表数据", notes = "适用于分页grid 显示数据 请求示例：127.0.0.1:18080/api/v1/staff/grid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @GetMapping(value = "table/staff/g")
    @ApiVersion(1)
    public ResultInfo findPageGrid(@Valid StaffDetailsInfoQueryDto query) {
        return this.staffDetailsInfoElasticsearchService.findPageGird(query);
    }


    /**
     *  修改数据状态
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "修改数据状态", notes = "适用于修改数据状态 请求示例：127.0.0.1:18080/api/v1/staff/status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "status",  required = true, dataType = "integer"),
            @ApiImplicitParam(name = "otherIds", value = "账户id",  required = true, dataType = "integer")
    })
    @PutMapping(value = "verify/staff/p")
    @ApiVersion(1)
    public ResultInfo updateDataStatus(@Valid IdParamDto param ) {
        return this.staffDetailsInfoService.updateStatus(param.getStatus(), param.getIdList(), param.getOtherIdList(), param.getPutParams());
    }


    /**
     * 根据id 获取详细信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id 获取详细信息", notes = "适用于根据id 获取详细信息 请求示例：127.0.0.1:18080/api/v1/staff/details/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id", paramType = "path",   required = true, dataType = "Long")
    })
    @GetMapping(value = "table/staff/g/{id}")
    @ApiVersion(1)
    public ResultInfo findById(@PathVariable(name = "id") Long id) {
        return this.staffDetailsInfoElasticsearchService.findById(id);
    }

    /**
     * 根据 账户id 获取详细信息
     * @param staffAccountsId
     * @return
     */
    @ApiOperation(value = "根据账户id  获取详细信息", notes = "适用于根据账户id  获取详细信息 请求示例：127.0.0.1:18080/api/v1/staff/details/{staffAccountsId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "staffAccountsId", value = "staffAccountsId", paramType = "path",   required = true, dataType = "Long")
    })
    @GetMapping(value = "table/staff/g/accounts/{staffAccountsId}")
    @ApiVersion(1)
    public ResultInfo findByStaffAccountsId(@PathVariable(name = "staffAccountsId") Long staffAccountsId) {
        return this.staffDetailsInfoElasticsearchService.findByStaffAccountsId(staffAccountsId);
    }

    /**
     * 设置头像
     * @param id
     * @param portrait
     * @param portraitId
     * @return
     */
    @ApiOperation(value = "根据账户id  获取详细信息", notes = "适用于根据账户id  获取详细信息 请求示例：127.0.0.1:18080/api/v1/staff/portrait/p")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id", paramType = "query",   required = true, dataType = "Long"),
            @ApiImplicitParam(name = "portrait", value = "头像地址", paramType = "query",   required = true, dataType = "String"),
            @ApiImplicitParam(name = "portraitId", value = "头像ID", paramType = "query",   required = true, dataType = "Long")
    })
    @PutMapping(value = "verify/staff/s/portrait")
    @ApiVersion(1)
    public ResultInfo setPortrait(Long id, String portrait, Long portraitId) {
        return this.staffDetailsInfoService.setPortrait(id, portrait, portraitId);
    }

    /**
     *  同步数据到es中
     * @param
     * @return
     */
    @ApiOperation(value = "同步数据", notes = "同步数据 请求示例：127.0.0.1:18080/api/v1/staff/sync")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
    })
    @PostMapping(value = "verify/staff/sync")
    @ApiVersion(1)
    public ResultInfo syncDataToElasticsearch() {
        return this.staffDetailsInfoService.syncDataToElasticsearch();
    }
}
