package pers.liujunyi.cloud.photo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


/***
 * 文件名称: UserAccounts.java
 * 文件描述: 用户帐号信息
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
@EqualsAndHashCode(callSuper = false)
@Document(indexName = "photo_manage_user_accounts", type = "userAccounts", shards = 1, replicas = 0)
@DynamicInsert
@DynamicUpdate
public class UserAccounts implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 用户帐号 */
    private String userAccounts;

    /** 用户密码 */
    private String userPassword;

    /** 绑定的手机号 */
    private String mobilePhone;

    /** 状态：0：正常  1：冻结 */
    private Byte userStatus;

    /** 用户类别   0：超级管理员 1：普通管理员  2：员工  3：顾客 */
    private Byte userCategory;

    /** 最后修改密码时间 */
    private Date changePasswordTime;

    /** 修改时间  */
    private Date updateTime;
}