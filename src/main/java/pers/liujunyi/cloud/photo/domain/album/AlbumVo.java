package pers.liujunyi.cloud.photo.domain.album;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liujunyi.cloud.photo.entity.AlbumPicture;
import pers.liujunyi.common.vo.BaseVo;

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
    /** 相册分类 例如：文艺、性感、古风、清纯、等 */
    private String classify;
    /** 相册描述 */
    private String description;
    /** 标签 */
    private String albumLabel;
    /** 背景音乐地址 */
    private String musicAddress;
    /** 封面 */
    private String cover;
    /** 相册照片数量 */
    private Integer total;
    /** 相册图片 */
    private List<AlbumPicture> albumPictureDatas;
}
