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
 * 文件名称: Album.java
 * 文件描述: 相册实体
 * 公 司:
 * 内容摘要:
 *      elasticsearch 配置信息
 *     document  ==》文档 ==》一个文档相当于Mysql一行的数据
 *  　indexName ==》索引 一个文档一个索引  不能 一个索引指定多个type
 *   field ==》列 ==》相当于mysql中的列，也就是一个属性
 * 其他说明: @Document(indexName = "photo_manage_album", type = "album", shards = 1, replicas = 0) 默认情况下添加@Document注解会对实体中的所有属性建立索引  indexName elasticsearch 的索引名称 可以理解为数据库名 必须为小写 不然会报org.elasticsearch.indices.InvalidIndexNameException异常
 *          @DynamicInsert属性:设置为true,设置为true,表示insert对象的时候,生成动态的insert语句,如果这个字段的值是null就不会加入到insert语句当中.默认true。
 *          比如希望数据库插入日期或时间戳字段时，在对象字段为空的情况下，表字段能自动填写当前的sysdate。
 *          @DynamicUpdate属性:设置为true,设置为true,表示update对象的时候,生成动态的update语句,如果这个字段的值是null就不会被加入到update语句中,默认true。
 *          比如只想更新某个属性，但是却把整个对象的属性都更新了，这并不是我们希望的结果，我们希望的结果是：我更改了哪些字段，只要更新我修改的字段就够了
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
@Document(indexName = "photo_manage_album", type = "album", shards = 1, replicas = 0)
@DynamicInsert
@DynamicUpdate
public class Album extends BaseEntity {

    private static final long serialVersionUID = 6100736012740286478L;
    /** 相册编号 */
    private String albumNumber;

    /** 相册名称 */
    private String albumName;

    /** 相册主题 */
    private String albumTitle;

    /** 相册风格 例如：小清新、日系、森系、复古、婚纱 等 */
    private String albumStyle;

    /** 相册分类 例如：文艺、性感、古风、清纯、等 */
    private String albumClassify;

    /** 相册描述 */
    @Field(index = false)
    private String albumDescription;

    /**  */
    @Field(index = false)
    private String remarks;

    /** 相册状态  0：已发布（可见）  1：不可见  2：草稿 */
    private Byte albumStatus;

    /** 标签 */
    @Field(index = false)
    private String albumLabel;

    /** 背景音乐地址 */
    @Field(index = false)
    private String albumMusicAddress;

    /** 版权设置 */
    @Field(index = false)
    private String albumCopyright;

    /** 优先级 数字从小到大排列 */
    private Byte albumPriority;
}