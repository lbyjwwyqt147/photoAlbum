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
 * 文件名称: StaffDetailsInfoDto.java
 * 文件描述: 员工档案 dto
 * 公 司:
 * 内容摘要:
 *
 * 完成日期:2019年03月22日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class StaffDetailsInfoDto extends BaseDto {

    private static final long serialVersionUID = -3568694500607340797L;
    /** 员工编号 */
    @ApiModelProperty(value = "员工编号")
    @NotBlank(message = "员工编号 必须填写")
    @Length(min = 1, max = 20, message = "员工编号 最多可以输入20个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_CODE_REGEXP, message = "员工编号 " + RegexpUtils.ALNUM_CODE_MSG)
    private String staffNumber;

    /** 员工帐号 */
    private Long staffAccountsId;

    /** 员工真实姓名 */
    @ApiModelProperty(value = "真实姓名")
    @NotBlank(message = "真实姓名 必须填写")
    @Length(min = 1, max = 32, message = "真实姓名 最多可以输入32个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_NAME_REGEXP, message = "真实姓名 " + RegexpUtils.ALNUM_NAME_MSG)
    private String staffName;

    /** 员工昵称 */
    @ApiModelProperty(value = "昵称")
    @Length(min = 0, max = 32, message = "昵称 最多可以输入32个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_NAME_REGEXP, message = "昵称 " + RegexpUtils.ALNUM_NAME_MSG)
    private String staffNickName;

    /** 性别 0:男  1:女 */
    @ApiModelProperty(value = "性别")
    @NotNull(message = "性别 必须选择")
    @Min(value = 0, message = "性别 值必须大于0")
    @Max(value = 127, message = "性别 最大值不能大于127")
    private Byte staffSex;

    /** 绑定的手机号 */
    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号 必须填写")
    @Length(min = 1, max = 11, message = "手机号 位数为11位")
    @Pattern(regexp = RegexpUtils.MOBILE_PHONE_REGEXP, message = RegexpUtils.MOBILE_PHONE_MSG)
    private String mobilePhone;

    /** 员工类别   0：超级管理员 1：普通管理员  2：员工   */
    @ApiModelProperty(value = "性别")
    @NotNull(message = "类别 必须选择")
    @Min(value = 0, message = "类别 值必须大于0")
    @Max(value = 127, message = "类别 最大值不能大于127")
    private Byte staffCategory;

    /** 员工职务   1: 摄影师 2：数码师（后期）  3：化妆师 等 */
    @ApiModelProperty(value = "职务")
    @NotNull(message = "职务 必须选择")
    @Min(value = 0, message = "职务 值必须大于0")
    @Max(value = 127, message = "职务 最大值不能大于127")
    private Byte staffPosition;

    /** 所在省份 */
    @ApiModelProperty(value = "所在省份")
    private Long province;

    /** 所在城市 */
    @ApiModelProperty(value = "所在城市")
    private Long city;

    /** 所在行政区 */
    @ApiModelProperty(value = "所在行政区")
    private Long district;

    /** 所在行政区街道 */
    @ApiModelProperty(value = "所在行政区街道")
    @Length(min = 0, max = 50, message = "街道 最多可以输入50个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_NAME_REGEXP, message = "街道 " + RegexpUtils.ALNUM_NAME_MSG)
    private String street;

    /**  身份证号  */
    @ApiModelProperty(value = "身份证号")
    @Pattern(regexp = RegexpUtils.IDENTIFICATIONCARD_REGEXP, message =  RegexpUtils.IDENTIFICATIONCARD_MSG)
    private String staffIdentiyCard;

    /** 入职日期 */
    @ApiModelProperty(value = "入职日期")
    private Date entryDate;

    /** 员工联系电话 */
    @ApiModelProperty(value = "联系电话")
    @Length(min = 0, max = 11, message = "联系电话 位数为11位")
    @Pattern(regexp = RegexpUtils.MOBILE_PHONE_REGEXP, message = RegexpUtils.MOBILE_PHONE_MSG)
    private String staffPhone;

    /** 电子邮箱 */
    @ApiModelProperty(value = "电子邮箱")
    @Email(message = "电子邮箱格式错误")
    @Length(min = 0, max = 60, message = "电子邮箱 最多可以输入60个字符")
    private String staffEmail;

    /** 联系QQ号 */
    @ApiModelProperty(value = "QQ号")
    @Length(min = 0, max = 13, message = "QQ号 最多可以输入13个字符")
    @Pattern(regexp = RegexpUtils.POSITIVE_INTEGER_REGEXP, message = "QQ号" + RegexpUtils.POSITIVE_INTEGER_MSG)
    private String staffQq;

    /** 联系微信号 */
    @ApiModelProperty(value = "微信号")
    @Length(min = 0, max = 20, message = "微信号 最多可以输入20个字符")
    private String staffWechat;

    /** 微博 */
    @ApiModelProperty(value = "新浪微博")
    @Length(min = 0, max = 200, message = "微博 最多可以输入200个字符")
    private String staffWeiBo;

    /** 生日 */
    @ApiModelProperty(value = "出生日期")
    @NotNull(message = "出生日期 必须填写")
    private Date birthday;

    /** 年龄 */
    @ApiModelProperty(value = "年龄")
    @Min(value = 10, message = "年龄 最小为10岁")
    @Max(value = 127, message = "年龄 不能大于127 岁")
    private Byte staffAge;

    /** 器材装备 */
    @ApiModelProperty(value = "器材装备")
    @Length(min = 0, max = 200, message = "器材装备 最多可以输入200个字符")
    private String staffEquipment;

    /** 个人介绍 */
    @ApiModelProperty(value = "个人介绍")
    @Length(min = 0, max = 350, message = "个人简介 最多可以输入400个字符")
    private String staffIntro;

    /** 状态：0：正常  1：冻结  2：离职 */
    @ApiModelProperty(value = "状态")
    @Min(value = 0, message = "状态 最小为0")
    @Max(value = 127, message = "状态 不能大于127")
    private Byte staffStatus;

    /** 头像 */
    private String staffPortrait;

    /** 头像id  */
    private Long staffPortraitId;

    /** 技能 多个用;隔开 */
    @ApiModelProperty(value = "技能 多个用;隔开")
    @Length(min = 0, max = 20, message = "技能 最多可以输入20个字符")
    private String skill;

    private Long userId;

    private Long dataVersion;
}
