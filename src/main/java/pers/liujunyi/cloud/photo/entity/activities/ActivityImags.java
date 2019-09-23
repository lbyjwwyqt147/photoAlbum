package pers.liujunyi.cloud.photo.entity.activities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.elasticsearch.annotations.Document;
import pers.liujunyi.cloud.common.entity.BaseEntity;

import javax.persistence.Entity;

/***
 * 文件名称: ActivityImags.java
 * 文件描述: 活动图片
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
@Document(indexName = "photo_manage_activity_imags", type = "ActivityImags", shards = 1, replicas = 0)
@DynamicInsert
@DynamicUpdate
public class ActivityImags  extends BaseEntity {
    private static final long serialVersionUID = 5464758823621572178L;
    /** 活动ID */
    private Long activityId;

    /** 照片ID */
    private Long pictureId;

    /** 照片地址 */
    private String pictureLocation;

    /** 照片名称 */
    private String pictureName;

    /** 优先级 数字从小到大排列 */
    private Byte priority;

    /** 文件分类 0：图片 1：文档  2：视频  5：其他 */
    private Byte pictureCategory;

    /** 状态 0：展示 1：不展示 */
    private Byte status;
}