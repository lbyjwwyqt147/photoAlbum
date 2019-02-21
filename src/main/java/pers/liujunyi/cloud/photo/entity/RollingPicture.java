package pers.liujunyi.cloud.photo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import pers.liujunyi.common.entity.BaseEntity;

import javax.persistence.Entity;
/***
 * 文件名称: RollingPicture.java
 * 文件描述: 滚动图片管理
 * 公 司:
 * 内容摘要:
 * 完成日期:2019年02月21日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Data
@Entity
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(indexName = "photo_manage", type = "rollingPicture", shards = 1, replicas = 0)
@DynamicInsert
@DynamicUpdate
public class RollingPicture extends BaseEntity {
    private static final long serialVersionUID = 5388585049076603542L;
    /** 照片ID */
    private Long pictureId;

    /** 照片地址 */
    @Field(index = false)
    private String pictureLocation;

    /** 状态 0：展示 1：不展示 */
    private Byte status;

    /** 业务代码 例如：1000：登录页面  1001：首页 等 */
    private String businessCode;

    /** 优先级 数字从小到大排列 */
    private Byte priority;

    /** href */
    @Field(index = false)
    private String hrefLink;

    /** 照片说明 */
    @Field(index = false)
    private String description;
}