package pers.liujunyi.cloud.photo.repository.elasticsearch.user;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.jpa.repository.Modifying;
import pers.liujunyi.cloud.photo.entity.StaffDetailsInfo;
import pers.liujunyi.common.repository.elasticsearch.BaseElasticsearchRepository;

import java.util.Date;
import java.util.List;

/***
 * 文件名称: StaffDetailsInfoElasticsearchRepository.java
 * 文件描述: 职工档案信息 Elasticsearch Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface StaffDetailsInfoElasticsearchRepository extends BaseElasticsearchRepository<StaffDetailsInfo, Long> {
    /**
     * 修改状态
     * @param staffStatus   0：正常  1：冻结  2：离职
     * @param ids
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query("update StaffDetailsInfo u set u.staffStatus = ?1, u.updateTime = ?2 where u.id in (?3)")
    int setStaffStatusByIds(Byte staffStatus, Date updateTime, List<Long> ids);


    /**
     * 根据 帐号id 获取顾客详细数据
     * @param staffAccountsId
     * @return
     */
    StaffDetailsInfo findFirstByStaffAccountsId(Long staffAccountsId);
}
