package pers.liujunyi.cloud.photo.domain.permission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import pers.liujunyi.cloud.common.dto.BaseDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/***
 * 文件名称: OrganizationsDto.java
 * 文件描述: 组织机构
 * 公 司:
 * 内容摘要:
 *
 * 完成日期:2019年03月11日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class OrganizationsDto extends BaseDto {
    private static final long serialVersionUID = -2904937024962048531L;
    /** 机构编号 */
    @ApiModelProperty(value = "机构编号")
    @NotBlank(message = "机构编号必须填写")
    @Length(min = 0, max = 15, message = "机构编号 最多可以输入15个字符")
    private String orgNumber;

    /** 机构名称 */
    @ApiModelProperty(value = "机构名称")
    @NotBlank(message = "机构名称必须填写")
    @Length(min = 0, max = 32, message = "机构名称 最多可以输入32个字符")
    private String orgName;

    /** 机构级别 */
    @ApiModelProperty(value = "机构级别")
    private Byte orgLevel;

    /** 父级主键id */
    @ApiModelProperty(value = "上级机构ID")
    @NotNull(message = "上级机构必须填写")
    private Long parentId;

    /** 排序号 */
    @ApiModelProperty(value = "序号")
    private Integer seq;

    /** 完整的机构名称 */
    @ApiModelProperty(value = "完整的机构名称")
    @Length(min = 0, max = 100, message = "机构名称 最多可以输入100个字符")
    private String fullName;

    /** 完整的层级 */
    private String fullParent;

    /** 完整的层级代码 */
    private String fullParentCode;

    /** 描述说明 */
    @ApiModelProperty(value = "描述说明")
    @Length(min = 0, max = 100, message = "机构名称 最多可以输入100个字符")
    private String description;

    /** 状态：0：正常  1：禁用 */
    @ApiModelProperty(value = "状态")
    @Min(value = 0, message = "状态 必须是数字类型")
    private Byte orgStatus;

    /** 预留字段1 */
    @ApiModelProperty(value = "attributeOne")
    @Length(min = 0, max = 45, message = "attributeOne 最多可以输入45个字符")
    private String attributeOne;

    /** 预留字段2 */
    @ApiModelProperty(value = "attributeTwo")
    @Length(min = 0, max = 65, message = "attributeTwo 最多可以输入65个字符")
    private String attributeTwo;

    /** 预留字段3 */
    @ApiModelProperty(value = "attributeThree")
    @Length(min = 0, max = 100, message = "attributeThree 最多可以输入100个字符")
    private String attributeThree;
}