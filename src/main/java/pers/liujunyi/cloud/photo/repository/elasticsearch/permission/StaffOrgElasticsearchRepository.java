package pers.liujunyi.cloud.photo.repository.elasticsearch.permission;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.jpa.repository.Modifying;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.photo.entity.permission.StaffOrg;

import java.util.Date;
import java.util.List;

/***
 * 文件名称: StaffOrgElasticsearchRepository.java
 * 文件描述: 职工关联组织机构 Elasticsearch Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface StaffOrgElasticsearchRepository extends BaseElasticsearchRepository<StaffOrg, Long> {
    /**
     * 修改状态
     * @param status  0：正常  1：禁用
     * @param ids
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query("update StaffOrg u set u.status = ?1, u.updateTime = ?2 where u.id in (?3)")
    int setStatusByIds(Byte status, Date updateTime, List<Long> ids);


    /**
     * 根据 机构id 获取数据
     * @param orgId
     * @return
     */
    List<StaffOrg> findAllByOrgId(Long orgId);

    /**
     * 根据 职工id 获取数据
     * @param staffId
     * @return
     */
    List<StaffOrg> findAllByStaffId(Long staffId);
}
