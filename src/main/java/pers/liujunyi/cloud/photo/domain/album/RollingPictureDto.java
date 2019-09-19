package pers.liujunyi.cloud.photo.domain.album;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import pers.liujunyi.cloud.common.dto.BaseDto;

import javax.validation.constraints.NotBlank;

/**
 * 轮播图片
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class RollingPictureDto extends BaseDto {
    private static final long serialVersionUID = 3811203711019099560L;


    /** 业务代码 例如：1000：登录页面  1001：首页 等 */
    @ApiModelProperty(value = "所属页面")
    @NotBlank(message = "所属页面必须填写")
    private String businessCode;

    /** 页面中所在位置 */
    @ApiModelProperty(value = "页面位置")
    @NotBlank(message = "页面位置必须填写")
    private String position;

    /** 优先级 数字从小到大排列 */
    private Byte priority;

    /** href */
    @Length(min = 0, max = 255, message = "图片外链接 最多可以输入255个字符")
    private String hrefLink;

    /** 照片说明 */
    @Length(min = 0, max = 100, message = "备注 最多可以输入100个字符")
    private String description;

    @ApiModelProperty(value = "图片")
    @NotBlank(message = "缺少上传图片")
    private String pictures;

    @ApiModelProperty(value = "类型 例如：1：活动图片  2：普通图片 ")
    private String variety;

    @ApiModelProperty(value = "风格：  写真   婚纱 ")
    private String maniyerizm;
}
