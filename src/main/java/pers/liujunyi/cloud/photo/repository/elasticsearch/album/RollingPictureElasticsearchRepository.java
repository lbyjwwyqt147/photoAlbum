package pers.liujunyi.cloud.photo.repository.elasticsearch.album;

import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.photo.entity.album.RollingPicture;

import java.util.List;

/***
 * 文件名称: RollingPictureElasticsearchRepository.java
 * 文件描述: 轮播图片 Elasticsearch Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年02月26日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface RollingPictureElasticsearchRepository extends BaseElasticsearchRepository<RollingPicture, Long> {

    /***
     *
     * @param businessCode
     * @param position
     * @return
     */
    List<RollingPicture> findByBusinessCodeAndPositionOrderByPriority(String businessCode, String position);

    /**
     *
     * @param businessCode
     * @param position
     * @return
     */
    int deleteByBusinessCodeAndAndPosition(String businessCode, String position);
}
