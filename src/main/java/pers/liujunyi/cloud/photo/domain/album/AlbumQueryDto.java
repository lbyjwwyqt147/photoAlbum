package pers.liujunyi.cloud.photo.domain.album;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liujunyi.cloud.common.query.elasticsearch.BaseEsQuery;
import pers.liujunyi.cloud.common.query.jpa.annotation.MatchType;
import pers.liujunyi.cloud.common.query.jpa.annotation.QueryCondition;

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

    /** 相册归类 例如：样片、客片 等 */
    @ApiModelProperty(value = "相册分类")
    private String albumClassification;

    /** 相册分类 例如：写真、婚纱、旅拍 等、等 */
    @ApiModelProperty(value = "相册分类")
    @QueryCondition()
    private String albumClassify;

    /** 相册风格 例如：小清新、日系、森系、复古、婚纱 等 */
    @ApiModelProperty(value = "相册风格")
    @QueryCondition(func = MatchType.equals)
    private String albumStyle;

    /** 相册状态  0：已发布（可见）  1：不可见  2：草稿 */
    @ApiModelProperty(value = "状态")
    @Min(value = 0, message = "状态 必须是数字类型")
    @QueryCondition()
    private Byte albumStatus;

    /** 是否在首页展示 0：展示  1：不展示  */
    @QueryCondition()
    private Byte display;
}
