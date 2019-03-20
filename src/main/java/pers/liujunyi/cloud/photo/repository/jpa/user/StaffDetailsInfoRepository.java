package pers.liujunyi.cloud.photo.repository.jpa.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.photo.entity.StaffDetailsInfo;

import java.util.Date;
import java.util.List;

/***
 * 文件名称: StaffDetailsInfoRepository.java
 * 文件描述: 职工档案信息 Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface StaffDetailsInfoRepository extends BaseRepository<StaffDetailsInfo, Long> {

    /**
     * 修改状态
     * @param staffStatus  0：正常  1：冻结  2：离职
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query("update StaffDetailsInfo u set u.staffStatus = ?1, u.updateTime = ?2 where u.id in (?3)")
    int setStaffStatusByIds(Byte staffStatus, Date updateTime, List<Long> ids);


}
