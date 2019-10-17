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
     * 根据页面和位置删除
     * @param businessCode  页面
     * @param position 位置
     * @return
     */
    int deleteByBusinessCodeAndAndPosition(String businessCode, String position);

    /**
     * 根据业务ID删除数据
     * @param variety 1：活动图片  2：写真图片 3：婚纱图片
     * @param businessId  业务数据ID
     * @return
     */
    int deleteByBusinessIdAndVariety(Long businessId, String variety);
}
