package pers.liujunyi.cloud.photo.domain.user;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liujunyi.cloud.common.vo.BaseVo;

import java.util.Date;

/***
 * 文件名称: CustomerDetailInfoVo.java
 * 文件描述: 顾客档案 vo
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
public class CustomerDetailInfoVo  extends BaseVo {


    private static final long serialVersionUID = -6395529481546243601L;

    /** 顾客编号 */
    @ApiModelProperty(value = "顾客编号")
    private String customerNumber;

    /** 顾客帐号 */
    private Long customerAccountsId;

    /** 顾客真实姓名 */
    @ApiModelProperty(value = "顾客姓名")
    private String customerName;

    /** 顾客昵称 */
    @ApiModelProperty(value = "顾客昵称")
    private String customerNickName;

    /** 性别 0:男  1:女 */
    @ApiModelProperty(value = "性别")
    private Byte customerSex;

    /** 绑定的手机号 */
    @ApiModelProperty(value = "手机号")
    private String mobilePhone;

    /** 顾客类别   0：普通顾客  1：模特   */
    @ApiModelProperty(value = "顾客类别")
    private Byte customerCategory;

    /** 顾客等级   0：普通顾客 1：VIP1  2：VIP2 等  */
    @ApiModelProperty(value = "顾客等级")
    private Byte customerGrade;

    /** 所在省份 */
    private Integer province;

    /** 所在城市 */
    private Integer city;

    /** 所在行政区 */
    private Integer district;

    /** 所在行政区街道 */
    private String street;
    @ApiModelProperty(value = "地址")
    private String addressText;

    /** 联系电话 */
    @ApiModelProperty(value = "联系电话")
    private String customerPhone;

    /** 电子邮箱 */
    @ApiModelProperty(value = "电子邮箱")
    private String customerEmail;

    /** 联系QQ号 */
    @ApiModelProperty(value = "联系QQ号")
    private String customerQq;

    /** 联系微信号 */
    @ApiModelProperty(value = "联系微信号")
    private String customerWechat;

    /** 微博 */
    @ApiModelProperty(value = "微博")
    private String customerWeiBo;

    /** 生日 */
    @ApiModelProperty(value = "生日")
    private Date birthday;

    /** 年龄 */
    @ApiModelProperty(value = "年龄")
    private Byte customerAge;

    /** 描述说明 */
    @ApiModelProperty(value = "描述")
    private String description;

    /** 状态：0：正常  1：禁用   */
    @ApiModelProperty(value = "状态")
    private Byte customerStatus;

    /** 头像 */
    @ApiModelProperty(value = "头像")
    private String customerPortrait;

    /** 头像id  */
    private Long customerPortraitId;
}
