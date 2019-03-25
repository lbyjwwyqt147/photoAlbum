package pers.liujunyi.cloud.photo.domain.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liujunyi.cloud.common.query.elasticsearch.BaseEsQuery;
import pers.liujunyi.cloud.common.query.jpa.annotation.MatchType;
import pers.liujunyi.cloud.common.query.jpa.annotation.QueryCondition;

import java.util.Date;

/***
 * 文件名称: CustomerDetailInfoQueryDto.java
 * 文件描述: 顾客档案 query dto
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月25日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDetailInfoQueryDto extends BaseEsQuery {

    private static final long serialVersionUID = 3606019268248844302L;
    /** 顾客昵称 */
    @ApiModelProperty(value = "顾客昵称")
    @QueryCondition(func = MatchType.like)
    private String customerNickName;

    /** 绑定的手机号 */
    @ApiModelProperty(value = "手机号")
    @QueryCondition(func = MatchType.like)
    private String mobilePhone;

    /** 顾客等级   0：普通顾客 1：VIP1  2：VIP2 等  */
    @ApiModelProperty(value = "顾客等级")
    @QueryCondition()
    private Byte customerGrade;

    /** 所在省份 */
    @ApiModelProperty(value = "省")
    @QueryCondition()
    private Integer province;

    /** 所在城市 */
    @ApiModelProperty(value = "市")
    @QueryCondition()
    private Integer city;

    /** 所在行政区 */
    @ApiModelProperty(value = "区")
    @QueryCondition()
    private Integer district;

    /** 状态：0：正常  1：禁用   */
    @ApiModelProperty(value = "状态")
    @QueryCondition()
    private Byte customerStatus;

    /** 生日 开始日期 */
    @ApiModelProperty(value = "生日 开始日期")
    @QueryCondition(func = MatchType.greaterThanOrEqualTo)
    private Date birthdayStart;

    /** 生日 结束日期 */
    @ApiModelProperty(value = "生日 结束日期")
    @QueryCondition(func = MatchType.lessThanOrEqualTo)
    private Date birthdayEnd;

}
