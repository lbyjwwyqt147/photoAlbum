package pers.liujunyi.cloud.photo.service.album;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseService;
import pers.liujunyi.cloud.photo.domain.album.RollingPictureDto;
import pers.liujunyi.cloud.photo.entity.album.RollingPicture;

import java.util.List;

/***
 * 文件名称: RollingPicture.java
 * 文件描述:  轮播图 Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年02月27日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface RollingPictureService extends BaseService<RollingPicture, Long> {

    /**
     * 保存数据
     * @param record
     * @return
     */
    ResultInfo saveRecord(RollingPictureDto record);

    /**
     * 修改状态
     * @param status   0：已发布（可见）  1：不可见  2：草稿
     * @param id
     * @return
     */
    ResultInfo updateStatus(Byte status, Long id);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    ResultInfo deleteBatch(List<Long> ids);

    /**
     * 单条删除
     * @param id
     * @return
     */
    ResultInfo deleteSingle(Long id);

    /**
     * 删除数据
     * @param businessCode
     * @param position
     * @return
     */
    ResultInfo deleteByBusinessCodeAndAndPosition(String businessCode, String position);

    /**
     * 同步数据到es中
     * @return
     */
    ResultInfo syncDataToElasticsearch();


}
