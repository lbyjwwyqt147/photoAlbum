package pers.liujunyi.cloud.photo.controller.album;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.photo.domain.album.AlbumDto;
import pers.liujunyi.cloud.photo.domain.album.AlbumQueryDto;
import pers.liujunyi.cloud.photo.service.album.AlbumElasticsearchService;
import pers.liujunyi.cloud.photo.service.album.AlbumService;
import pers.liujunyi.cloud.security.domain.IdParamDto;

import javax.validation.Valid;

/***
 * 文件名称: AlbumController.java
 * 文件描述: 相册管理 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月04日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "相册管理 API")
@RestController
public class AlbumController  extends BaseController {

    @Autowired
    private AlbumService albumService;
    @Autowired
    private AlbumElasticsearchService albumElasticsearchService;

    /**
     * 保存数据
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "保存数据", notes = "适用于保存数据 请求示例：127.0.0.1:18081/api/v1/verify/album/s")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @PostMapping(value = "verify/album/s")
    @ApiVersion(1)
    public ResultInfo saveRecord(@Valid AlbumDto param) {
        return this.albumService.saveRecord(param);
    }

    /**
     * 单条删除数据
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "单条删除数据", notes = "适用于单条删除数据 请求示例：127.0.0.1:18081/api/v1/verify/album/d")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @DeleteMapping(value = "verify/album/d")
    @ApiVersion(1)
    public ResultInfo singleDelete(@Valid IdParamDto param) {
        this.albumService.deleteById(param.getId());
        return ResultUtil.success();
    }

    /**
     * 批量删除
     *
     * @param param 　 多个id 用 , 隔开
     * @return
     */
    @ApiOperation(value = "删除多条数据", notes = "适用于批量删除数据 请求示例：127.0.0.1:18081/api/v1/verify/album/d/b")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @DeleteMapping(value = "verify/album/d/b")
    @ApiVersion(1)
    public ResultInfo batchDelete(@Valid IdParamDto param) {
        return this.albumService.deleteBatch(param.getIdList());
    }

    /**
     * 分页列表数据
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "分页列表数据", notes = "适用于分页grid 显示数据 请求示例：127.0.0.1:18081/api/v1/table/album/g")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @GetMapping(value = "table/album/g")
    @ApiVersion(1)
    public ResultInfo findPageGrid(@Valid AlbumQueryDto query) {
        return this.albumElasticsearchService.findPageGird(query);
    }




    /**
     *  修改数据状态
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "修改数据状态", notes = "适用于修改数据状态 请求示例：127.0.0.1:18081/api/v1/verify/album/p")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
    })
    @PutMapping(value = "verify/album/p")
    @ApiVersion(1)
    public ResultInfo updateDataStatus(@Valid IdParamDto param ) {
        return this.albumService.updateStatus(param.getStatus(), param.getIdList());
    }

    /**
     *  根据ID 获取数据详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID 获取数据详情", notes = "根据ID 获取数据详情 请求示例：127.0.0.1:18081/api/v1/table/album/1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id", paramType = "path",   required = true, dataType = "Long")
    })
    @GetMapping(value = "table/album/{id}")
    @ApiVersion(1)
    public ResultInfo findById(@PathVariable(name = "id") Long id) {
        return this.albumElasticsearchService.details(id);
    }


    /**
     *  同步数据到es中
     * @param
     * @return
     */
    @ApiOperation(value = "同步数据", notes = "同步数据 请求示例：127.0.0.1:18081/api/v1/verify/album/sync")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
    })
    @PostMapping(value = "verify/album/sync")
    @ApiVersion(1)
    public ResultInfo syncDataToElasticsearch() {
        return this.albumService.syncDataToElasticsearch();
    }
}
