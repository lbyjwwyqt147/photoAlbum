package pers.liujunyi.cloud.photo.domain.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import pers.liujunyi.cloud.common.dto.BaseDto;

import javax.validation.constraints.NotBlank;

/***
 * 文件名称: CompanySettingDto.java
 * 文件描述: 公司信息设置
 * 公 司:
 * 内容摘要:
 *
 * 完成日期:2019年09月29日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class CompanySettingDto  extends BaseDto {
    private static final long serialVersionUID = -3297010595002450705L;

    /** 公司名称 */
    @ApiModelProperty(value = "相册名称")
    @NotBlank(message = "名称必须填写")
    @Length(min = 0, max = 50, message = "名称 最多可以输入50个字符")
    private String companyName;

    /** logo */
    @ApiModelProperty(value = "logo")
    @NotBlank(message = "logo")
    private String companyLogo;

    /** 营业地址 */
    @ApiModelProperty(value = "营业地址")
    @NotBlank(message = "营业地址")
    @Length(min = 0, max = 100, message = "营业地址 最多可以输入100个字符")
    private String businessAddress;

    /** 营业时间 */
    @ApiModelProperty(value = "营业时间")
    @NotBlank(message = "营业时间")
    @Length(min = 0, max = 50, message = "营业时间 最多可以输入50个字符")
    private String businessHours;

    /** 公司联系人 */
    @ApiModelProperty(value = "公司联系人")
    @NotBlank(message = "公司联系人")
    @Length(min = 0, max = 32, message = "公司联系人 最多可以输入32个字符")
    private String companyContact;

    /** 公司电话 */
    @ApiModelProperty(value = "公司电话")
    @NotBlank(message = "公司电话")
    private String companyPhone;

    /** 公司介绍 */
    @ApiModelProperty(value = "公司介绍")
    @Length(min = 0, max = 500, message = "公司介绍 最多可以输入500个字符")
    private String companyProfile;

    /** QQ二维码图片 */
    @ApiModelProperty(value = "QQ二维码图片")
    private String qqImage;

    /** 微信二维码图片 */
    @ApiModelProperty(value = "微信二维码图片")
    private String weixinImage;

    /** 公众号二维码图片 */
    @ApiModelProperty(value = "公众号二维码图片")
    private String bjnewsImage;

    /** 微博二维码图片 */
    @ApiModelProperty(value = "微博二维码图片")
    private String weiboImage;

    /** 备案信息 */
    @ApiModelProperty(value = "备案信息")
    @Length(min = 0, max = 50, message = "备案信息 最多可以输入50个字符")
    private String filing;

}
