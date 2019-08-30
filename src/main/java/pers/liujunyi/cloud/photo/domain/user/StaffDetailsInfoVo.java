package pers.liujunyi.cloud.photo.domain.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class StaffDetailsInfoVo extends StaffDetailsInfoDto {

    private static final long serialVersionUID = 562898741873474891L;


    /** 员工职务   1: 摄影师 2：数码师（后期）  3：化妆师 等 */
    @ApiModelProperty(value = "职务")
    private Byte staffPosition;


    @ApiModelProperty(value = "职务")
    private String staffPositionText;


    /** 所在行政区街道 */
    @ApiModelProperty(value = "所在行政区街道")
    private String street;
    @ApiModelProperty(value = "地址")
    private String addressText;


    /** 所属组织机构名称 */
    @ApiModelProperty(value = "组织机构")
    private String staffOrgName;

    /** 技能（特长）*/
    private String skillText;

}
