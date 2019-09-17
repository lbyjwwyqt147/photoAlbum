package pers.liujunyi.cloud.photo.domain.album;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 轮播图 vo
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class RollingPictureVo extends RollingPictureDto {

    private static final long serialVersionUID = 7791998095818472113L;

    /** 照片数量 */
    @ApiModelProperty(value = "照片数量")
    private Integer total;

    /** 所属页面 */
    @ApiModelProperty(value = "所属页面")
    private String pageText;

    /** 页面位置 */
    @ApiModelProperty(value = "页面位置")
    private String positionText;

    private Long pId;

    /** 照片地址 */
    private String pictureLocation;
}
