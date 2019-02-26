package pers.liujunyi.cloud.photo.repository.elasticsearch.album;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pers.liujunyi.cloud.photo.entity.Album;
import pers.liujunyi.common.repository.elasticsearch.BaseElasticsearchRepository;

import java.util.List;

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
     * 修改状态
     * @param albumStatus  0：已发布（可见）  1：不可见  2：草稿
     * @param ids
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query("update Album u set u.albumStatus = ?1 where u.id in (?2)")
    int setStatusByIds(Byte albumStatus, List<Long> ids);


}
