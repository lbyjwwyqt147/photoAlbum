package pers.liujunyi.cloud.photo.service.album;

import pers.liujunyi.cloud.photo.domain.album.AlbumQueryDto;
import pers.liujunyi.cloud.photo.entity.album.Album;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.service.BaseElasticsearchService;

/***
 * 文件名称: AlbumElasticsearchService.java
 * 文件描述: 相册 Elasticsearch Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年02月27日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface AlbumElasticsearchService extends BaseElasticsearchService<Album, Long> {

    /**
     * 分页列表
     * @param query
     * @return
     */
    ResultInfo findPageGird(AlbumQueryDto query);

}
