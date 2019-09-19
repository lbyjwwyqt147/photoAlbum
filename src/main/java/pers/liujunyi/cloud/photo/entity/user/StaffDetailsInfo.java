package pers.liujunyi.cloud.photo.entity.user;

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
import java.util.Date;

/***
 * 文件名称: StaffDetailsInfo.java
 * 文件描述: 职工档案信息
 * 公 司:
 * 内容摘要:
 *
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Data
@Entity
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(indexName = "photo_manage_staff_info", type = "staffDetailsInfo", shards = 1, replicas = 0)
@DynamicInsert
@DynamicUpdate
public class StaffDetailsInfo extends BaseEntity {

    /** 员工编号 */
    private String staffNumber;

    /** 员工帐号 */
    private Long staffAccountsId;

    /** 员工真实姓名 */
    private String staffName;

    /** 员工昵称 */
    private String staffNickName;

    /** 性别 0:男  1:女 */
    private Byte staffSex;

    /** 绑定的手机号 */
    private String mobilePhone;

    /** 入职日期 */
    private Date entryDate;

    /** 员工类别   0：超级管理员 1：普通管理员  2：员工   */
    private Byte staffCategory;

    /** 员工职务   1: 摄影师 2：数码师（后期）  3：化妆师 等 */
    private Byte staffPosition;

    /** 所在省份 */
    private Long province;

    /** 所在城市 */
    private Long city;

    /** 所在行政区 */
    private Long district;

    /**  身份证号  */
    private String staffIdentiyCard;

    /** 所在行政区街道 */
    @Field(type = FieldType.Keyword, index = false)
    private String street;

    /** 员工联系电话 */
    @Field(type = FieldType.Keyword, index = false)
    private String staffPhone;

    /** 电子邮箱 */
    @Field(type = FieldType.Keyword, index = false)
    private String staffEmail;

    /** 联系QQ号 */
    @Field(type = FieldType.Keyword, index = false)
    private String staffQq;

    /** 联系微信号 */
    @Field(type = FieldType.Keyword, index = false)
    private String staffWechat;

    /** 微博 */
    @Field(type = FieldType.Keyword, index = false)
    private String staffWeiBo;


    /** 生日 */
    private Date birthday;

    /** 年龄 */
    @Field(type = FieldType.Integer, index = false)
    private Byte staffAge;

    /** 器材装备 */
    @Field(type = FieldType.Keyword, index = false)
    private String staffEquipment;

    /** 个人介绍 */
    @Field(type = FieldType.Keyword, index = false)
    private String staffIntro;

    /** 状态：0：正常  1：冻结  2：离职 */
    private Byte staffStatus;

    /** 头像 */
    @Field(type = FieldType.Keyword, index = false)
    private String staffPortrait;

    /** 头像id  */
    @Field(type = FieldType.Keyword, index = false)
    private Long staffPortraitId;

    /** 技能 多个用;隔开 */
    private String skill;

    /** 试用期 状态 1：试用期职工   2：正式职工 */
    private String probationStatus;

    /** 在职年限 (月) */
    private Integer workingAge;

    /** 在职年限 年月日 格式 */
    private String  workingYears;

    /** 离职日期 */
    private Date dimissionDate;

    /** 离职原因 */
    private String dimissionReason;

    private Long dataVersion;

    /** 是否在网页展示 0：展示  1：不展示  */
    private Byte display;

}