package pers.liujunyi.cloud.photo.domain.activities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liujunyi.cloud.common.query.elasticsearch.BaseEsQuery;
import pers.liujunyi.cloud.common.query.jpa.annotation.MatchType;
import pers.liujunyi.cloud.common.query.jpa.annotation.QueryCondition;

import java.util.Date;

/***
 * 文件名称: NewActivitiesQueryDto.java
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
public class NewActivitiesQueryDto extends BaseEsQuery {


    private static final long serialVersionUID = -4249068536306247689L;

    /** 开始时间 */
    @ApiModelProperty(value = "开始时间 ")
    private Date startDateTime;

    /** 结束时间 */
    @ApiModelProperty(value = "结束时间 ")
    @QueryCondition(column = "endDateTime", func = MatchType.leRange, rangeField = {"startDateTime", "endDateTime"})
    private Date endDateTime;

    /** 是否到期  0：未到期  1：到期 */
    @ApiModelProperty(value = "是否到期  0：未到期  1：到期  ")
    @QueryCondition()
    private Byte maturity;

    /** 活动状态 0：上架   1：草稿  2：下架 */
    @ApiModelProperty(value = "活动状态 0：上架   1：草稿  2：下架")
    @QueryCondition()
    private Byte activityStatus;


}
