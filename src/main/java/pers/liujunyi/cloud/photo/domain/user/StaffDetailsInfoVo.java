package pers.liujunyi.cloud.photo.domain.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liujunyi.cloud.common.vo.BaseVo;

import javax.validation.constraints.NotNull;
import java.util.Date;

/***
 * 文件名称: StaffDetailsInfoVo.java
 * 文件描述: 员工档案 vo
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
public class StaffDetailsInfoVo extends BaseVo {

    /** 员工编号 */
    @ApiModelProperty(value = "员工编号")
    private String staffNumber;

    /** 员工帐号 */
    @NotNull(message = "必须关联员工帐号")
    private Long staffAccountsId;

    /** 员工真实姓名 */
    @ApiModelProperty(value = "真实姓名")
    private String staffName;

    /** 员工昵称 */
    @ApiModelProperty(value = "昵称")
    private String staffNickName;

    /** 性别 0:男  1:女 */
    @ApiModelProperty(value = "性别")
    private Byte staffSex;

    /** 绑定的手机号 */
    @ApiModelProperty(value = "手机号")
    private String mobilePhone;

    /** 员工类别   0：超级管理员 1：普通管理员  2：员工   */
    @ApiModelProperty(value = "类别")
    private Byte staffCategory;

    /** 员工职务   1: 摄影师 2：数码师（后期）  3：化妆师 等 */
    @ApiModelProperty(value = "职务")
    private Byte staffPosition;
    @ApiModelProperty(value = "职务")
    private String staffPositionText;

    /** 所在省份 */
    @ApiModelProperty(value = "所在省份")
    private Integer province;

    /** 所在城市 */
    @ApiModelProperty(value = "所在城市")
    private Integer city;

    /** 所在行政区 */
    @ApiModelProperty(value = "所在行政区")
    private Integer district;

    /** 所在行政区街道 */
    @ApiModelProperty(value = "所在行政区街道")
    private String street;
    @ApiModelProperty(value = "地址")
    private String addressText;

    /**  身份证号  */
    @ApiModelProperty(value = "身份证号")
    private String staffIdentiyCard;

    /** 员工联系电话 */
    @ApiModelProperty(value = "联系电话")
    private String staffPhone;

    /** 电子邮箱 */
    @ApiModelProperty(value = "电子邮箱")
    private String staffEmail;

    /** 联系QQ号 */
    @ApiModelProperty(value = "QQ号")
    private String staffQq;

    /** 联系微信号 */
    @ApiModelProperty(value = "微信号")
    private String staffWechat;

    /** 微博 */
    @ApiModelProperty(value = "新浪微博")
    private String staffWeiBo;

    /** 生日 */
    @ApiModelProperty(value = "出生日期")
    private Date birthday;

    /** 年龄 */
    @ApiModelProperty(value = "年龄")
    private Byte staffAge;

    /** 器材装备 */
    @ApiModelProperty(value = "器材装备")
    private String staffEquipment;

    /** 描述说明 */
    @ApiModelProperty(value = "描述说明")
    private String description;

    /** 个人介绍 */
    @ApiModelProperty(value = "个人介绍")
    private String staffIntro;

    /** 状态：0：正常  1：冻结  2：离职 */
    @ApiModelProperty(value = "年龄")
    private Byte staffStatus;

    /** 头像 */
    @ApiModelProperty(value = "头像")
    private String staffPortrait;

    /** 头像id  */
    private Long staffPortraitId;

    /** 组织机构 */
    @ApiModelProperty(value = "组织机构")
    private String orgName;

}
