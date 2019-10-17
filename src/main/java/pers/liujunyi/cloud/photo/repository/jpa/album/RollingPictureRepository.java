package pers.liujunyi.cloud.photo.repository.jpa.album;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.photo.entity.album.RollingPicture;


/***
 * 文件名称: RollingPictureRepository.java
 * 文件描述: 滚动图片管理 Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年02月21日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface RollingPictureRepository extends BaseRepository<RollingPicture, Long> {

    /**
     * 修改状态
     * @param status  0：展示 1：不展示
     * @param id
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query("update RollingPicture u set u.status = ?1 where u.id = ?2")
    int setStatusByIds(Byte status, Long id);


    /**
     * 根据页面和位置删除数据
     * @param businessCode
     * @param position
     * @return
     */
    int deleteByBusinessCodeAndAndPosition(String businessCode, String position);

    /**
     * 根据业务ID删除数据
     * @param variety 1：活动图片  2：写真图片 3：婚纱图片
     * @param businessId  业务数据ID
     * @return
     */
    int deleteByBusinessIdAndVariety(Long businessId, String variety);
}
