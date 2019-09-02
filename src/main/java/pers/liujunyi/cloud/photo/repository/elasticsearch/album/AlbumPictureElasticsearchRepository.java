package pers.liujunyi.cloud.photo.repository.elasticsearch.album;

import org.springframework.data.domain.Pageable;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.photo.entity.album.AlbumPicture;

import java.util.List;

/***
 * 文件名称: AlbumPictureElasticsearchRepository.java
 * 文件描述: 相册图片 Elasticsearch Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年02月26日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface AlbumPictureElasticsearchRepository extends BaseElasticsearchRepository<AlbumPicture, Long> {

    /**
     * 根据相册ID 获取相册图片信息
     * @param albumId
     * @param pageable
     * @return
     */
    List<AlbumPicture> findByAlbumId(Long albumId,  Pageable pageable);

    /**
     * 根据相册ID 获取相册封面图片信息
     * @param albumId
     * @param cover
     * @param pageable
     * @return
     */
    List<AlbumPicture> findByAlbumIdAndCover(Long albumId, Byte cover, Pageable pageable);

    /**
     * 根据相册ID 获取相册图片信息
     * @param albumIds
     * @param pageable
     * @return
     */
    List<AlbumPicture> findByAlbumIdIn(List<Long> albumIds, Pageable pageable);

}
