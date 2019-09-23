package pers.liujunyi.cloud.photo.entity.activities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;
import pers.liujunyi.cloud.common.entity.BaseEntity;

import javax.persistence.Entity;
import java.util.Date;

/***
 * 文件名称: NewActivities.java
 * 文件描述: 最新活动
 * 公 司:
 * 内容摘要:
 *
 * 完成日期:2019年09月23日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Data
@Entity
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(indexName = "photo_manage_new_activities", type = "NewActivities", shards = 1, replicas = 0)
@DynamicInsert
@DynamicUpdate
public class NewActivities extends BaseEntity {
    private static final long serialVersionUID = -7980012995134536537L;

    /** 活动编号 */
    private String activityNumber;

    /** 活动主题 */
    private String activityTheme;

    /** 开始时间 */
    private Date startDateTime;

    /** 结束时间 */
    private Date endDateTime;

    /** 是否到期  0：未到期  1：到期 */
    private Byte maturity;

    /** 原价 */
    private Float originalPrice;

    /** 活动价格 */
    private Float activityPrice;

    /** 折扣 */
    private Float discount;

    /** 活动联系人 */
    private String contactPerson;

    /** 联系人电话 */
    private String contactNumber;

    /** 营业时间 */
    private String businessHours;

    /** 活动描述 */
    private String activityDescription;

    /** 活动状态 0：上架   1：草稿  2：下架 */
    private Byte activityStatus;

    /** 封面图 */
    private String surfacePlot;

    /** 短视频 */
    private String shortVideo;

    /** 版本号 */
    @Version
    private Long dataVersion;

    /** 序号  */
    private Byte activityPriority;

    /** 封面图ID */
    private Long surfacePlotId;

    /** 短视频ID */
    private Long shortVideoId;
}