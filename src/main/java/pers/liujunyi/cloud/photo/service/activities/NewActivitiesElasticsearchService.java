package pers.liujunyi.cloud.photo.service.activities;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseElasticsearchService;
import pers.liujunyi.cloud.photo.domain.activities.NewActivitiesQueryDto;
import pers.liujunyi.cloud.photo.domain.activities.NewActivitiesVo;
import pers.liujunyi.cloud.photo.entity.activities.NewActivities;


/***
 * 文件名称: NewActivitiesElasticsearchService.java
 * 文件描述: 最新活动 Elasticsearch Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年09月23日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface NewActivitiesElasticsearchService extends BaseElasticsearchService<NewActivities, Long> {

    /**
     * 分页列表
     * @param query
     * @return
     */
    ResultInfo findPageGird(NewActivitiesQueryDto query);

    /**
     * 根据ID 获取详细数据(包含活动图片信息)
     * @param id
     * @return
     */
    ResultInfo details(Long id);

    /**
     * 根据ID 获取详细数据(不包含活动图片信息)
     * @param id
     * @return
     */
    NewActivitiesVo detailsById(Long id);

    /**
     * 根据ID 获取详细数据
     * @param id
     * @return
     */
    NewActivities findById(Long id);

    /**
     * 活动图片信息
     * @param activityId
     * @return
     */
    ResultInfo findActivitiesPictureByActivityId(Long activityId);

}
