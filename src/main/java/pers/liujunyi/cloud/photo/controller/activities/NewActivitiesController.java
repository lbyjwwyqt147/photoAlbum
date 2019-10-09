package pers.liujunyi.cloud.photo.controller.activities;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;import pers.liujunyi.cloud.photo.domain.activities.NewActivitiesDto;
import pers.liujunyi.cloud.photo.domain.activities.NewActivitiesQueryDto;
import pers.liujunyi.cloud.photo.service.activities.NewActivitiesElasticsearchService;
import pers.liujunyi.cloud.photo.service.activities.NewActivitiesService;
import pers.liujunyi.cloud.security.domain.IdParamDto;

import javax.validation.Valid;

/***
 * 文件名称: NewActivitiesController.java
 * 文件描述: 最新活动 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年09月23日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "最新活动 API")
@RestController
public class NewActivitiesController extends BaseController {

    @Autowired
    private NewActivitiesService newActivitiesService;
    @Autowired
    private NewActivitiesElasticsearchService newActivitiesElasticsearchService;

    /**
     * 保存数据
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "保存数据", notes = "适用于保存数据 请求示例：127.0.0.1:18081/api/v1/verify/activities/s")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1")
    })
    @PostMapping(value = "verify/activities/s")
    @ApiVersion(1)
    public ResultInfo saveRecord(@Valid NewActivitiesDto param) {
        return this.newActivitiesService.saveRecord(param);
    }

    /**
     * 单条删除数据
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "单条删除数据", notes = "适用于单条删除数据 请求示例：127.0.0.1:18081/api/v1/verify/activities/d")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1")
    })
    @DeleteMapping(value = "verify/activities/d")
    @ApiVersion(1)
    public ResultInfo singleDelete(@Valid IdParamDto param) {
        return this.newActivitiesService.deleteSingle(param.getId());
    }

    /**
     * 单条删除图片数据
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "单条删单条删除图片数据除数据", notes = "适用于单条删除图片数据请求示例：127.0.0.1:18081/api/v1/verify/activities/d")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1")
    })
    @DeleteMapping(value = "verify/activities/picture/d")
    @ApiVersion(1)
    public ResultInfo singleDeleteNewActivitiesPicture(@Valid IdParamDto param) {
        return this.newActivitiesService.deletePictureById(param.getId());
    }


    /**
     * 批量删除
     *
     * @param param 　 多个id 用 , 隔开
     * @return
     */
    @ApiOperation(value = "删除多条数据", notes = "适用于批量删除数据 请求示例：127.0.0.1:18081/api/v1/verify/activities/d/b")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1")
    })
    @DeleteMapping(value = "verify/activities/d/b")
    @ApiVersion(1)
    public ResultInfo batchDelete(@Valid IdParamDto param) {
        return this.newActivitiesService.deleteBatch(param.getIdList());
    }

    /**
     * 分页列表数据
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "分页列表数据", notes = "适用于分页grid 显示数据 请求示例：127.0.0.1:18081/api/v1/table/activities/g")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1")
    })
    @GetMapping(value = "table/activities/g")
    @ApiVersion(1)
    public ResultInfo findPageGrid(@Valid NewActivitiesQueryDto query) {
        return this.newActivitiesElasticsearchService.findPageGird(query);
    }


    /**
     *  修改数据状态
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "修改数据状态", notes = "适用于修改数据状态 请求示例：127.0.0.1:18081/api/v1/verify/activities/p")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
    })
    @PutMapping(value = "verify/activities/p")
    @ApiVersion(1)
    public ResultInfo updateDataStatus(@Valid IdParamDto param ) {
        return this.newActivitiesService.updateStatus(param.getStatus(), param.getId(), param.getDataVersion());
    }


    /**
     *  根据ID 获取数据详情（包含图片信息）
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID 获取数据详情 （包含图片信息）", notes = "根据ID 获取数据详情 请求示例：127.0.0.1:18081/api/v1/table/activities/picture/1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id", paramType = "path",   required = true, dataType = "Long")
    })
    @GetMapping(value = "table/activities/picture/{id}")
    @ApiVersion(1)
    public ResultInfo detailsById(@PathVariable(name = "id") Long id) {
        return this.newActivitiesElasticsearchService.details(id);
    }

    /**
     *  根据ID 获取数据详情（不包含图片信息）
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID 获取数据详情(不包含图片信息) ", notes = "根据ID 获取数据详情 请求示例：127.0.0.1:18081/api/v1/table/activities/1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id", paramType = "path",   required = true, dataType = "Long")
    })
    @GetMapping(value = "table/activities/{id}")
    @ApiVersion(1)
    public ResultInfo findById(@PathVariable(name = "id") Long id) {
        return ResultUtil.success(this.newActivitiesElasticsearchService.detailsById(id));
    }


    /**
     *  根据活动ID 获取图片数据详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据活动ID 获取图片数据详情", notes = "根据活动ID 获取图片数据详情 请求示例：127.0.0.1:18081/api/v1/table/activities/picture")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id", paramType = "path",   required = true, dataType = "Long")
    })
    @GetMapping(value = "table/activities/picture")
    @ApiVersion(1)
    public ResultInfo findNewActivitiesPictureByNewActivitiesId(Long id) {
        return this.newActivitiesElasticsearchService.findActivitiesPictureByActivityId(id);
    }

    /**
     *  同步数据到es中
     * @param
     * @return
     */
    @ApiOperation(value = "同步数据", notes = "同步数据 请求示例：127.0.0.1:18081/api/v1/verify/activities/sync")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
    })
    @PostMapping(value = "verify/activities/sync")
    @ApiVersion(1)
    public ResultInfo syncDataToElasticsearch() {
        return this.newActivitiesService.syncDataToElasticsearch();
    }

    /**
     *  活动下拉框
     * @param
     * @return
     */
    @ApiOperation(value = "活动下拉框", notes = "活动下拉框 请求示例：127.0.0.1:18081/api/v1/table/activities/comboBox")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
    })
    @GetMapping(value = "table/activities/comboBox")
    @ApiVersion(1)
    public ResultInfo activitiesComboBox() {
        return this.newActivitiesElasticsearchService.activitiesComboBox();
    }
}
