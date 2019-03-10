package pers.liujunyi.cloud.photo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.elasticsearch.annotations.Document;
import pers.liujunyi.common.entity.BaseEntity;

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
    private String staffNickname;

    /** 性别 0:男  1:女 */
    private Byte staffSex;

    /** 绑定的手机号 */
    private String mobilePhone;

    /** 员工类别   0：超级管理员 1：普通管理员  2：员工   */
    private Byte staffCategory;

    /** 员工职务   1: 摄影师 2：数码师（后期）  3：化妆师 等 */
    private Byte staffPosition;

    /** 所在省份 */
    private Integer province;

    /** 所在城市 */
    private Integer city;

    /** 所在行政区 */
    private Integer district;

    /** 所在行政区街道 */
    private String street;

    /** 员工联系电话 */
    private String staffPhone;

    /** 电子邮箱 */
    private String staffEmail;

    /** 联系QQ号 */
    private String staffQq;

    /** 联系微信号 */
    private String staffWechat;

    /** 生日 */
    private Date birthday;

    /** 年龄 */
    private Byte staffAge;

    /** 器材装备 */
    private String staffEquipment;

    /** 描述说明 */
    private String description;

    /** 个人介绍 */
    private String staffIntro;

    /** 状态：0：正常  1：冻结  2：离职 */
    private Byte staffStatus;

    /** 头像 */
    private String staffPortrait;

    /** 头像id  */
    private Long staffPortraitId;
}