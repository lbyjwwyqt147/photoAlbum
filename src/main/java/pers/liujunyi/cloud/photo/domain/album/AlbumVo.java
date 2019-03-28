package pers.liujunyi.cloud.photo.domain.album;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liujunyi.cloud.common.vo.BaseVo;
import pers.liujunyi.cloud.photo.entity.album.AlbumPicture;

import java.util.Date;
import java.util.List;

/**
 * 相册信息 vo
 * @author ljy
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AlbumVo extends BaseVo {

    /** 相册主题 */
    private String title;
    /** 相册状态  0：已发布（可见）  1：不可见  2：草稿 */
    private Byte status;
    /** 相册风格 例如：小清新、日系、森系、复古、婚纱 等 */
    private String albumStyle;
    /** 相册分类 例如：写真、婚纱、旅拍 等 */
    private String classify;
    /** 相册归类 例如：样片、客片 等 */
    private String classification;
    /** 相册描述 */
    private String description;
    /** 标签 */
    private String albumLabel;
    /** 背景音乐地址 */
    private String musicAddress;
    /** 作者(摄影师)  */
    private String albumPhotographyAuthor;
    /** 后期（数码师） */
    private String albumAnaphasisAuthor;
    /** 化妆师 */
    private String albumDresser;
    /** 拍摄地点 */
    private String spotForPhotography;
    /** 模特 */
    private String albumMannequin;
    /** 封面 */
    private String cover;
    /** 拍摄时间 */
    private Date shootingsDate;
    /** 相册照片数量 */
    private Integer total;
    /** 相册图片 */
    private List<AlbumPicture> albumPictureDatas;
    /** 版本号 */
    private Long version;
}
