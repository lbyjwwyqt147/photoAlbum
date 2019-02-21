package pers.liujunyi.cloud.photo.repository.jpa.album;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pers.liujunyi.cloud.photo.entity.Album;
import pers.liujunyi.common.repository.jpa.BaseRepository;

import java.util.List;

/***
 * 文件名称: AlbumRepository.java
 * 文件描述: 相册管理 Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年02月21日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface AlbumRepository extends BaseRepository<Album, Long> {

    /**
     * 修改状态
     * @param albumStatus  0：已发布（可见）  1：不可见  2：草稿
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query("update Album u set u.albumStatus = ?1 where u.id in (?2)")
    int setStatusByIds(Byte albumStatus, List<Long> ids);
}
