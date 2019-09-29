package pers.liujunyi.cloud.photo.entity.setting;

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
 * 文件名称: CompanySetting.java
 * 文件描述: 公司信息设置
 * 公 司:
 * 内容摘要:
 *
 * 完成日期:2019年09月29日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Data
@Entity
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(indexName = "photo_manage_company_setting", type = "CompanySetting", shards = 1, replicas = 0)
@DynamicInsert
@DynamicUpdate
public class CompanySetting extends BaseEntity {

    private static final long serialVersionUID = -4091115558484976089L;

    /** 公司名称 */
    private String companyName;

    /** logo */
    private String companyLogo;

    /** 营业地址 */
    private String businessAddress;

    /** 营业时间 */
    private String businessHours;

    /** 公司联系人 */
    private String companyContact;

    /** 公司电话 */
    private String companyPhone;

    /** 公司介绍 */
    private String companyProfile;

    /** QQ二维码图片 */
    private String qqImage;

    /** 微信二维码图片 */
    private String weixinImage;

    /** 公众号二维码图片 */
    private String bjnewsImage;

    /** 微博二维码图片 */
    private String weiboImage;

    /** 备案信息 */
    private String filing;

    private Long dataVersion;

}