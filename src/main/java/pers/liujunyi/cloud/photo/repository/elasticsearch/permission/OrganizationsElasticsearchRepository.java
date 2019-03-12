package pers.liujunyi.cloud.photo.repository.elasticsearch.permission;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.jpa.repository.Modifying;
import pers.liujunyi.cloud.photo.entity.permission.Organizations;
import pers.liujunyi.common.repository.elasticsearch.BaseElasticsearchRepository;

import java.util.Date;
import java.util.List;

/***
 * 文件名称: OrganizationsElasticsearchRepository.java
 * 文件描述: 组织机构 Elasticsearch Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface OrganizationsElasticsearchRepository extends BaseElasticsearchRepository<Organizations, Long> {
    /**
     * 修改状态
     * @param orgStatus  0：正常  1：禁用
     * @param updateTime
     * @param ids
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query("update Organizations u set u.orgStatus = ?1, u.updateTime = ?2 where u.id in (?3)")
    int setOrgStatusByIds(Byte orgStatus, Date updateTime, List<Long> ids);

    /**
     * 更新机构全名称
     * @param fullName
     * @param updateTime
     * @param id
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query("update Organizations u set u.fullName = ?1,  u.updateTime = ?2 where u.id = ?3")
    int updateFullNameById(String fullName, Date updateTime, Long id);

    /**
     * 根据 机构编号 获取数据
     * @param orgNumber
     * @return
     */
    Organizations findFirstByOrgNumber(String orgNumber);

    /**
     * 根据pid 获取数据
     * @param pid
     * @param orgStatus  0：正常  1：禁用
     * @return
     */
    List<Organizations> findByParentIdAndOrgStatusOrderBySeqAsc(Long pid, Byte orgStatus, Pageable page);
}
