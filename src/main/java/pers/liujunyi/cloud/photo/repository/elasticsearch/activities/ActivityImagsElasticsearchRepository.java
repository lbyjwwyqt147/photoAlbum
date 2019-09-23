package pers.liujunyi.cloud.photo.repository.elasticsearch.activities;

import org.springframework.data.domain.Pageable;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.photo.entity.activities.ActivityImags;

import java.util.List;

/***
 * 文件名称: ActivityImagsElasticsearchRepository.java
 * 文件描述: 活动图片 Elasticsearch Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年09月23日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface ActivityImagsElasticsearchRepository extends BaseElasticsearchRepository<ActivityImags, Long> {

    /**
     * 根据活动ID 获取活动图片信息
     * @param activityId
     * @param pageable
     * @return
     */
    List<ActivityImags> findByActivityId(Long activityId, Pageable pageable);


    /**
     * 根据活动ID 获取活动图片信息
     * @param activityIds
     * @param pageable
     * @return
     */
    List<ActivityImags> findByActivityIdIn(List<Long> activityIds, Pageable pageable);

}
