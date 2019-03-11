package pers.liujunyi.cloud.photo.domain.album;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import pers.liujunyi.common.dto.BaseDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/***
 * 文件名称: AlbumDto.java
 * 文件描述: 相册
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
public class AlbumDto extends BaseDto {

    private static final long serialVersionUID = -1197543425348740669L;
    /** 相册名称 */
    @ApiModelProperty(value = "相册名称")
    @NotBlank(message = "名称必须填写")
    @Length(min = 0, max = 32, message = "名称 最多可以输入32个字符")
    private String albumName;

    /** 相册主题 */
    @ApiModelProperty(value = "相册主题")
    @Length(min = 0, max = 32, message = "主题 最多可以输入32个字符")
    private String albumTitle;

    /** 相册风格 例如：小清新、日系、森系、复古、婚纱 等 */
    @ApiModelProperty(value = "相册风格")
    @NotBlank(message = "风格必须填写")
    @Length(min = 0, max = 10, message = "风格 最多可以输入10个字符")
    private String albumStyle;

    /** 相册归类 例如：样片、客片 等 */
    @ApiModelProperty(value = "相册分类")
    @NotBlank(message = "归类必须填写")
    @Length(min = 0, max = 10, message = "归类 最多可以输入10个字符")
    private String albumClassification;

    /** 相册分类 例如：写真、婚纱、旅拍 等 */
    @ApiModelProperty(value = "相册分类")
    @NotBlank(message = "分类必须填写")
    @Length(min = 0, max = 10, message = "分类 最多可以输入10个字符")
    private String albumClassify;

    /** 相册描述 */
    @ApiModelProperty(value = "相册描述")
    @Length(min = 0, max = 255, message = "描述 最多可以输入255个字符")
    private String albumDescription;

    /**  */
    @ApiModelProperty(value = "相册备注")
    @Length(min = 0, max = 255, message = "备注 最多可以输入255个字符")
    private String remarks;

    /** 相册状态  0：已发布（可见）  1：不可见  2：草稿 */
    @NotNull(message = "状态 必须填写")
    @Min(value = 0, message = "状态 必须是数字类型")
    private Byte albumStatus;

    /** 标签 */
    @ApiModelProperty(value = "标签")
    @Length(min = 0, max = 32, message = "标签 最多可以输入32个字符")
    private String albumLabel;

    /** 背景音乐地址 */
    @ApiModelProperty(value = "背景音乐")
    @Length(min = 0, max = 255, message = "背景音乐 最多可以输入255个字符")
    private String albumMusicAddress;

    /** 版权设置 */
    @ApiModelProperty(value = "版权设置")
    @Length(min = 0, max = 10, message = "版权设置 最多可以输入10个字符")
    private String albumCopyright;

    /** 作者(摄影师)  */
    @ApiModelProperty(value = "作者(摄影师)")
    @Length(min = 0, max = 32, message = "作者(摄影师) 最多可以输入32个字符")
    private String albumPhotographyAuthor;

    /** 后期（数码师） */
    @ApiModelProperty(value = "后期（数码师）")
    @Length(min = 0, max = 32, message = "后期（数码师） 最多可以输入32个字符")
    private String albumAnaphasisAuthor;

    /** 化妆师 */
    @ApiModelProperty(value = "化妆师")
    @Length(min = 0, max = 32, message = "化妆师 最多可以输入32个字符")
    private String albumDresser;

    /** 模特 */
    @ApiModelProperty(value = "模特")
    @Length(min = 0, max = 32, message = "模特 最多可以输入32个字符")
    private String albumMannequin;


    /** 拍摄地点 */
    @ApiModelProperty(value = "拍摄地点")
    @Length(min = 0, max = 10, message = "拍摄地点 最多可以输入10个字符")
    private String spotForPhotography;

    /** 优先级 数字从小到大排列 */
    @ApiModelProperty(value = "序号")
    @Min(value = 0, message = "序号 必须是数字类型")
    private Byte albumPriority;

    @ApiModelProperty(value = "图片")
    @NotBlank(message = "缺少上传图片")
    private String pictures;

    /** 拍摄时间 */
    @ApiModelProperty(value = "拍摄时间")
    private Date shootingsDate;
}
