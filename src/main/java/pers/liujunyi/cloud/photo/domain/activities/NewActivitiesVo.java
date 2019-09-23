package pers.liujunyi.cloud.photo.domain.activities;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liujunyi.cloud.photo.entity.activities.ActivityImags;

import java.util.List;

/***
 * 文件名称: NewActivitiesVo.java
 * 文件描述: 最新活动
 * 公 司:
 * 内容摘要:
 *
 * 完成日期:2019年09月23日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class NewActivitiesVo extends NewActivitiesDto {


    private static final long serialVersionUID = -1217202506809884361L;

    /** 活动图片 */
    private List<ActivityImags> activityPictureData;

    /** 图片总数 */
    private int total;
}
