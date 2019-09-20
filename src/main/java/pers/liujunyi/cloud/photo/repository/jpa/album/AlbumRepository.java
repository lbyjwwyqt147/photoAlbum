package pers.liujunyi.cloud.photo.repository.jpa.album;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.photo.entity.album.Album;

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
     * @param id
     * @param dataVersion
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query(value = "update album u set u.album_status = ?1, u.update_time = now(), u.data_version = data_version+1 where u.id = ?2 and u.data_version = ?3 ", nativeQuery = true)
    int setStatusByIds(Byte albumStatus, Long id, Long dataVersion);


    /**
     * 修改 是否在首页展示 状态
     * @param status  0：展示  1：不展示
     * @param id
     * @param dataVersion
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query(value = "update album u set u.display = ?1, u.update_time = now(), u.data_version = data_version+1 where u.id = ?2 and u.data_version = ?3 ", nativeQuery = true)
    int updateDataShowStatus(Byte status, Long id, Long dataVersion);


}
