package pers.liujunyi.cloud.photo.domain.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liujunyi.cloud.common.query.elasticsearch.BaseEsQuery;
import pers.liujunyi.cloud.common.query.jpa.annotation.MatchType;
import pers.liujunyi.cloud.common.query.jpa.annotation.QueryCondition;

/***
 * 文件名称: StaffDetailsInfoQueryDto.java
 * 文件描述: 员工档案 query dto
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月22日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class StaffDetailsInfoQueryDto extends BaseEsQuery {
    private static final long serialVersionUID = -1582195362858567940L;

    /** 员工编号 */
    @ApiModelProperty(value = "员工编号")
    @QueryCondition(func = MatchType.like)
    private String staffNumber;

    /** 员工真实姓名 */
    @ApiModelProperty(value = "员工姓名")
    @QueryCondition(func = MatchType.like)
    private String staffName;

    /** 手机号 */
    @ApiModelProperty(value = "手机号")
    @QueryCondition()
    private String mobilePhone;

    /** 员工职务   1: 摄影师 2：数码师（后期）  3：化妆师 等 */
    @ApiModelProperty(value = "职务")
    @QueryCondition()
    private Byte staffPosition;

    /** 状态：0：正常  1：禁用  2：离职 */
    @ApiModelProperty(value = "状态")
    @QueryCondition()
    private Byte staffStatus;

    /** 是否在网页展示 0：展示  1：不展示  */
    @QueryCondition()
    private Byte display;
}
