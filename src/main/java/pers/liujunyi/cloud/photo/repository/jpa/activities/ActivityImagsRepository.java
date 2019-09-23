package pers.liujunyi.cloud.photo.repository.jpa.activities;

import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.photo.entity.activities.ActivityImags;

import java.util.List;

/***
 * 文件名称: ActivityImagsRepository.java
 * 文件描述: 活动图片 Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年09月23日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface ActivityImagsRepository extends BaseRepository<ActivityImags, Long> {

    /**
     * 根据活动ID 删除活动图片
     * @param activityId
     * @return
     */
    int deleteByActivityId(Long activityId);

    /**
     * 根据文件Id 获取数据
     * @param pictureIds
     * @return
     */
    List<ActivityImags> findByPictureIdIn(List<Long> pictureIds);
}
