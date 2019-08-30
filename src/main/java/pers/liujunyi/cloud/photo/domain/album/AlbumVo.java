package pers.liujunyi.cloud.photo.domain.album;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liujunyi.cloud.photo.entity.album.AlbumPicture;

import java.util.List;

/**
 * 相册信息 vo
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class AlbumVo extends AlbumDto {

    private static final long serialVersionUID = 7791998095818472113L;
    /** 相册照片数量 */
    @ApiModelProperty(value = "相册照片数量")
    private Integer total;
    /** 相册图片 */
    @ApiModelProperty(value = "相册图片")
    private List<AlbumPicture> albumPictureData;
    /** 摄影师名称 */
    @ApiModelProperty(value = "摄影师名称")
    private String albumPhotographyAuthorText;
    /** 后期设计师名称 */
    @ApiModelProperty(value = "后期设计师名称")
    private String albumAnaphasisAuthorText;
    /** 化妆师名称 */
    @ApiModelProperty(value = "化妆师名称")
    private String albumDresserText;
    /** 拍摄地点名称 */
    @ApiModelProperty(value = "拍摄地点名称")
    private String spotForPhotographyText;
    /** 封面 */
    private String cover;

}
