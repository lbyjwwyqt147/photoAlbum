package pers.liujunyi.cloud.photo.domain.activities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liujunyi.cloud.common.dto.BaseDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/***
 * 文件名称: NewActivitiesDto.java
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
public class NewActivitiesDto extends BaseDto {


    private static final long serialVersionUID = -8817692200781584230L;
    /** 活动主题 */
    @ApiModelProperty(value = "活动主题 ")
    @NotBlank(message = "活动主题必须填写")
    private String activityTheme;

    /** 开始时间 */
    @ApiModelProperty(value = "开始时间 ")
    @NotNull(message = "开始时间必须填写")
    private Date startDateTime;

    /** 结束时间 */
    @ApiModelProperty(value = "结束时间 ")
    @NotNull(message = "结束时间必须填写")
    private Date endDateTime;

    /** 是否到期  0：未到期  1：到期 */
    @ApiModelProperty(value = "是否到期  0：未到期  1：到期  ")
    private Byte maturity;

    /** 原价 */
    @ApiModelProperty(value = "原价价格")
    @NotNull(message = "原价价格必须填写")
    private Float originalPrice;

    /** 活动价格 */
    @ApiModelProperty(value = "活动价格")
    @NotNull(message = "活动价格必须填写")
    private Float activityPrice;

    /** 折扣 */
    @ApiModelProperty(value = "折扣")
    @NotNull(message = "折扣必须填写")
    private Float discount;

    /** 活动联系人 */
    @ApiModelProperty(value = "活动联系人")
    private String contactPerson;

    /** 联系人电话 */
    @ApiModelProperty(value = "联系人电话")
    private String contactNumber;

    /** 营业时间 */
    @ApiModelProperty(value = "营业时间")
    private String businessHours;

    /** 活动描述 */
    @ApiModelProperty(value = "活动描述")
    private String activityDescription;

    /** 活动状态 0：上架   1：草稿  2：下架 */
    @ApiModelProperty(value = "活动状态 0：上架   1：草稿  2：下架")
    private Byte activityStatus;

    /** 封面图 */
    @ApiModelProperty(value = "封面图")
    @NotBlank(message = "封面图必须上传")
    private String surfacePlot;

    /** 短视频 */
    @ApiModelProperty(value = "短视频")
    private String shortVideo;

    /** 序号  */
    private Byte activityPriority;

    /** 活动编号 */
    private String activityNumber;

    @ApiModelProperty(value = "图片")
    @NotBlank(message = "缺少上传图片")
    private String pictures;

    /** 封面图ID */
    private Long surfacePlotId;

    /** 短视频ID */
    private Long shortVideoId;
}
