package pers.liujunyi.cloud.photo.repository.jpa.activities;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.photo.entity.activities.NewActivities;

/***
 * 文件名称: NewActivitiesRepository.java
 * 文件描述: 最新活动 Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年09月23日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface NewActivitiesRepository extends BaseRepository<NewActivities, Long> {

    /**
     * 修改状态
     * @param activityStatus  活动状态 0：上架   1：草稿  2：下架
     * @param id
     * @param dataVersion
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query(value = "update new_activities u set u.activity_status = ?1, u.update_time = now(), u.data_version = data_version+1 where u.id = ?2 and u.data_version = ?3 ", nativeQuery = true)
    int setStatusByIds(Byte activityStatus, Long id, Long dataVersion);


}
