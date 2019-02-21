package pers.liujunyi.cloud.photo.repository.jpa.album;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pers.liujunyi.cloud.photo.entity.AlbumPicture;
import pers.liujunyi.common.repository.jpa.BaseRepository;

import java.util.List;

/***
 * 文件名称: AlbumPictureRepository.java
 * 文件描述: 相册图片 Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年02月21日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface AlbumPictureRepository extends BaseRepository<AlbumPicture, Long> {

    /**
     * 修改状态
     * @param status  0：展示 1：不展示
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query("update AlbumPicture u set u.status = ?1 where u.id in (?2)")
    int setStatusByIds(Byte status, List<Long> ids);

}
