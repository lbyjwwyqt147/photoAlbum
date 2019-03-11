package pers.liujunyi.cloud.photo.domain.album;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liujunyi.common.query.elasticsearch.BaseEsQuery;

import javax.validation.constraints.Min;


/***
 * 文件名称: AlbumQueryDto.java
 * 文件描述: 相册 query dto
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年02月26日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class AlbumQueryDto extends BaseEsQuery {
    private static final long serialVersionUID = 5223845406864513819L;
    /** 相册分类 例如：人像、文艺、性感、古风、清纯、等 */
    @ApiModelProperty(value = "相册分类")
    private String albumClassify;

    /** 相册风格 例如：小清新、日系、森系、复古、婚纱 等 */
    @ApiModelProperty(value = "相册风格")
    private String albumStyle;

    /** 相册状态  0：已发布（可见）  1：不可见  2：草稿 */
    @ApiModelProperty(value = "状态")
    @Min(value = 0, message = "状态 必须是数字类型")
    private Byte albumStatus;
}
