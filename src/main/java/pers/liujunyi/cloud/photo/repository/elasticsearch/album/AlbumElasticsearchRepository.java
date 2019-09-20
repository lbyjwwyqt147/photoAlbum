package pers.liujunyi.cloud.photo.repository.elasticsearch.album;

import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.photo.entity.album.Album;

/***
 * 文件名称: AlbumElasticsearchRepository.java
 * 文件描述: 相册 Elasticsearch Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年02月26日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface AlbumElasticsearchRepository extends BaseElasticsearchRepository<Album, Long> {

    /**
     * 统计次数
     * @param display
     * @return
     */
    long countByDisplay(Byte display);



}
