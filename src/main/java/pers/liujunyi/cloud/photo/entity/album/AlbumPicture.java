package pers.liujunyi.cloud.photo.entity.album;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import pers.liujunyi.cloud.common.entity.BaseEntity;

import javax.persistence.Entity;

/***
 * 文件名称: AlbumPicture.java
 * 文件描述: 相册图片
 * 公 司:
 * 内容摘要:
 *
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
@Document(indexName = "photo_manage_album_picture", type = "albumPicture", shards = 1, replicas = 0)
@DynamicInsert
@DynamicUpdate
public class AlbumPicture extends BaseEntity {

    private static final long serialVersionUID = 6528758581332889489L;

    /** 相册ID */
    private Long albumId;

    /** 照片ID */
    private Long pictureId;

    /** 照片地址 */
    @Field(type = FieldType.Keyword, index = false)
    private String pictureLocation;

    /** 照片名称 */
    @Field(type = FieldType.Keyword, index = false)
    private String pictureName;

    /** 照片说明 */
    @Field(type = FieldType.Keyword, index = false)
    private String description;

    /** 备注 */
    @Field(type = FieldType.Keyword, index = false)
    private String remarks;

    /** 是否是封面 0：是封面  1：不是 */
    private Byte cover;

    /** 优先级 数字从小到大排列 */
    private Byte priority;

    /** 状态 0：展示 1：不展示 */
    private Byte status;

    /** 文件分类 0：图片 1：文档  2：视频  5：其他 */
    private Byte pictureCategory;

    /** 文件大小 */
    private Long pictureSize;

    /** 文件类型 */
    private String pictureType;

    /** 版本号 */
    private Long dataVersion;

}