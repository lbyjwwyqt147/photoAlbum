package pers.liujunyi.cloud.photo.domain.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import pers.liujunyi.cloud.common.dto.BaseDto;
import pers.liujunyi.cloud.common.util.RegexpUtils;

import javax.validation.constraints.*;
import java.util.Date;

/***
 * 文件名称: CustomerDetailInfoDto.java
 * 文件描述: 顾客档案 dto
 * 公 司:
 * 内容摘要:
 *
 * 完成日期:2019年03月25日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDetailInfoDto extends BaseDto {

    private static final long serialVersionUID = -6459897214062572536L;
    /** 顾客编号 */
    @ApiModelProperty(value = "顾客编号")
    @Length(min = 1, max = 20, message = "顾客编号 最多可以输入20个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_CODE_REGEXP, message = "顾客编号 " + RegexpUtils.ALNUM_CODE_MSG)
    private String customerNumber;

    /** 顾客帐号 */
    @NotNull(message = "必须关联顾客帐号")
    private Long customerAccountsId;

    /** 顾客真实姓名 */
    @ApiModelProperty(value = "真实姓名")
    @Length(min = 0, max = 32, message = "姓名 最多可以输入32个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_NAME_REGEXP, message = "姓名 " + RegexpUtils.ALNUM_NAME_MSG)
    private String customerName;

    /** 顾客昵称 */
    @ApiModelProperty(value = "昵称")
    @NotBlank(message = "昵称 必须填写")
    @Length(min = 1, max = 32, message = "昵称 最多可以输入32个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_NAME_REGEXP, message = "昵称 " + RegexpUtils.ALNUM_NAME_MSG)
    private String customerNickName;

    /** 性别 0:男  1:女 */
    @ApiModelProperty(value = "性别")
    @NotNull(message = "性别 必须选择")
    @Min(value = 0, message = "性别 值必须大于0")
    @Max(value = 127, message = "性别 最大值不能大于127")
    private Byte customerSex;

    /** 绑定的手机号 */
    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号 必须填写")
    @Length(min = 1, max = 11, message = "手机号 位数为11位")
    @Pattern(regexp = RegexpUtils.MOBILE_PHONE_REGEXP, message = RegexpUtils.MOBILE_PHONE_MSG)
    private String mobilePhone;

    /** 顾客类别   0：普通顾客  1：模特   */
    @ApiModelProperty(value = "类别")
    @NotNull(message = "类别 必须选择")
    @Min(value = 0, message = "类别 值必须大于0")
    @Max(value = 127, message = "类别 最大值不能大于127")
    private Byte customerCategory;

    /** 顾客等级   0：普通顾客 1：VIP1  2：VIP2 等  */
    @ApiModelProperty(value = "顾客等级")
    @NotNull(message = "顾客等级 必须选择")
    @Min(value = 0, message = "顾客等级 值必须大于0")
    @Max(value = 127, message = "顾客等级 最大值不能大于127")
    private Byte customerGrade;

    /** 所在省份 */
    private Integer province;

    /** 所在城市 */
    private Integer city;

    /** 所在行政区 */
    private Integer district;

    /** 所在行政区街道 */
    @ApiModelProperty(value = "所在行政区街道")
    @Length(min = 0, max = 50, message = "街道 最多可以输入50个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_NAME_REGEXP, message = "街道 " + RegexpUtils.ALNUM_NAME_MSG)
    private String street;

    /** 联系电话 */
    @ApiModelProperty(value = "联系电话")
    @Length(min = 0, max = 11, message = "联系电话 位数为11位")
    @Pattern(regexp = RegexpUtils.MOBILE_PHONE_REGEXP, message = RegexpUtils.MOBILE_PHONE_MSG)
    private String customerPhone;

    /** 电子邮箱 */
    @ApiModelProperty(value = "电子邮箱")
    @Email(message = "电子邮箱格式错误")
    @Length(min = 0, max = 60, message = "电子邮箱 最多可以输入60个字符")
    private String customerEmail;

    /** 联系QQ号 */
    @ApiModelProperty(value = "QQ号")
    @Length(min = 0, max = 13, message = "QQ号 最多可以输入13个字符")
    @Pattern(regexp = RegexpUtils.POSITIVE_INTEGER_REGEXP, message = "QQ号" + RegexpUtils.POSITIVE_INTEGER_MSG)
    private String customerQq;

    /** 联系微信号 */
    @ApiModelProperty(value = "微信号")
    @Length(min = 0, max = 20, message = "微信号 最多可以输入20个字符")
    private String customerWechat;

    /** 微博 */
    @ApiModelProperty(value = "新浪微博")
    @Length(min = 0, max = 200, message = "微博 最多可以输入200个字符")
    private String customerWeiBo;

    /** 生日 */
    @ApiModelProperty(value = "出生日期")
    private Date birthday;

    /** 年龄 */
    @ApiModelProperty(value = "年龄")
    @Min(value = 10, message = "年龄 最小为10岁")
    @Max(value = 127, message = "年龄 不能大于127 岁")
    private Byte customerAge;

    /** 描述说明 */
    @ApiModelProperty(value = "描述说明")
    @Length(min = 0, max = 255, message = "描述 最多可以输入255个字符")
    private String description;

    /** 状态：0：正常  1：禁用   */
    @ApiModelProperty(value = "状态")
    @Min(value = 0, message = "状态 最小值为 0 ")
    @Max(value = 127, message = "状态 不能大于127")
    private Byte customerStatus;

    /** 头像 */
    @ApiModelProperty(value = "头像")
    @Length(min = 0, max = 255, message = "头像 最多可以输入255个字符")
    private String customerPortrait;

    /** 头像id  */
    private Long customerPortraitId;


}
