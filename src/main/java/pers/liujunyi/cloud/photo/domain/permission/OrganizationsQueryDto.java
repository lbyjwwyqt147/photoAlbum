package pers.liujunyi.cloud.photo.domain.permission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liujunyi.common.query.elasticsearch.BaseEsQuery;

import javax.validation.constraints.Min;

/***
 * 文件名称: OrganizationsQueryDto.java
 * 文件描述: 组织机构 query dto
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月11日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class OrganizationsQueryDto extends BaseEsQuery {

    private static final long serialVersionUID = 6449395633423076411L;
    /** 机构编号 */
    @ApiModelProperty(value = "机构编号")
    private String orgNumber;

    /** 机构名称 */
    @ApiModelProperty(value = "机构名称")
    private String orgName;

    /** 父级主键id */
    @ApiModelProperty(value = "上级机构id")
    private Long parentId;

    /** 状态：0：正常  1：禁用 */
    @ApiModelProperty(value = "状态")
    @Min(value = 0, message = "状态 必须是数字类型")
    private Byte orgStatus;
}