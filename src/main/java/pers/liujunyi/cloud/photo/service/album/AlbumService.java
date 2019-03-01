package pers.liujunyi.cloud.photo.service.album;

import pers.liujunyi.cloud.photo.domain.album.AlbumDto;
import pers.liujunyi.cloud.photo.entity.Album;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.service.BaseService;

import java.util.List;

/***
 * 文件名称: AlbumService.java
 * 文件描述:  相册 Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年02月27日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface AlbumService extends BaseService<Album, Long> {

    /**
     * 保存数据
     * @param record
     * @return
     */
    ResultInfo saveRecord(AlbumDto record);

    /**
     * 修改状态
     * @param status   0：已发布（可见）  1：不可见  2：草稿
     * @param ids
     * @return
     */
    ResultInfo updateStatus(Byte status, List<Long> ids);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    ResultInfo batchDeletes(List<Long> ids);

    /**
     * 同步数据到es中
     * @return
     */
    ResultInfo syncDataToElasticsearch();

}
