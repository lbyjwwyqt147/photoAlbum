package pers.liujunyi.cloud.photo.service.album;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseElasticsearchService;
import pers.liujunyi.cloud.photo.domain.album.RollingPictureQueryDto;
import pers.liujunyi.cloud.photo.entity.album.RollingPicture;


/***
 * 文件名称: RollingPictureElasticsearchService.java
 * 文件描述: 轮播图 Elasticsearch Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年02月27日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface RollingPictureElasticsearchService extends BaseElasticsearchService<RollingPicture, Long> {

    /**
     * 分页列表
     * @param query
     * @return
     */
    ResultInfo findPageGird(RollingPictureQueryDto query);

    /**
     * 根据ID 获取详细数据(包含相册图片信息)
     * @param id
     * @return
     */
    ResultInfo details(Long id);

    /**
     *
     * @param businessCode
     * @param position
     * @return
     */
    ResultInfo findByBusinessCodeAndPositionOrderByPriority(String businessCode, String position);

}
