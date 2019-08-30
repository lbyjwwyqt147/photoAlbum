package pers.liujunyi.cloud.photo.domain.album;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liujunyi.cloud.common.dto.BaseDto;

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
    private String businessCode;


}
