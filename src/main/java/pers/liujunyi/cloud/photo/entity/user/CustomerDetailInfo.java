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
import pers.liujunyi.common.entity.BaseEntity;

import javax.persistence.Entity;
import java.util.Date;

/***
 * 文件名称: CustomerDetailInfo.java
 * 文件描述: 顾客档案信息
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
@Document(indexName = "photo_manage_customer_info", type = "customerDetailInfo", shards = 1, replicas = 0)
@DynamicInsert
@DynamicUpdate
public class CustomerDetailInfo extends BaseEntity {
    private static final long serialVersionUID = -6294448472527897935L;
    /** 顾客编号 */
    private String customerNumber;

    /** 顾客帐号 */
    private Long customerAccountsId;

    /** 顾客真实姓名 */
    private String customerName;

    /** 顾客昵称 */
    private String customerNickName;

    /** 性别 0:男  1:女 */
    private Byte customerSex;

    /** 绑定的手机号 */
    private String mobilePhone;

    /** 顾客类别   0：普通顾客  1：模特   */
    private Byte customerCategory;

    /** 顾客等级   0：普通顾客 1：VIP1  2：VIP2 等  */
    private Byte customerGrade;

    /** 所在省份 */
    private Integer province;

    /** 所在城市 */
    private Integer city;

    /** 所在行政区 */
    private Integer district;

    /** 所在行政区街道 */
    @Field(type = FieldType.Keyword, index = false)
    private String street;

    /** 联系电话 */
    @Field(type = FieldType.Keyword, index = false)
    private String customerPhone;

    /** 电子邮箱 */
    @Field(type = FieldType.Keyword, index = false)
    private String customerEmail;

    /** 联系QQ号 */
    @Field(type = FieldType.Keyword, index = false)
    private String customerQq;

    /** 联系微信号 */
    @Field(type = FieldType.Keyword, index = false)
    private String customerWechat;

    /** 生日 */
    private Date birthday;

    /** 年龄 */
    @Field(type = FieldType.Integer, index = false)
    private Byte customerAge;

    /** 描述说明 */
    @Field(type = FieldType.Keyword, index = false)
    private String description;

    /** 状态：0：正常  1：禁用   */
    private Byte customerStatus;

    /** 头像 */
    @Field(type = FieldType.Keyword, index = false)
    private String customerPortrait;

    /** 头像id  */
    @Field(type = FieldType.Keyword, index = false)
    private Long customerPortraitId;
}