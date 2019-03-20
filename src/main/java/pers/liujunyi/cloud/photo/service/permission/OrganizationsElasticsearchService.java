package pers.liujunyi.cloud.photo.service.permission;

import pers.liujunyi.cloud.photo.domain.permission.OrganizationsQueryDto;
import pers.liujunyi.cloud.photo.entity.permission.Organizations;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.service.BaseElasticsearchService;
import pers.liujunyi.common.vo.tree.ZtreeNode;

import java.util.List;
import java.util.Map;

/***
 * 文件名称: OrganizationsElasticsearchService.java
 * 文件描述: 组织机构信息 Elasticsearch Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface OrganizationsElasticsearchService extends BaseElasticsearchService<Organizations, Long> {

    /**
     *  根据 pid 符合 ztree 结构的数据
     * @param pid
     * @param status
     * @return
     */
    List<ZtreeNode> orgTree(Long pid, Byte status);

    /**
     * 根据 fullParentCode 获取 符合 ztree 结构的数据
     * @param fullParentCode
     * @return
     */
    List<ZtreeNode> orgFullParentCodeTree(String fullParentCode);

    /**
     * 分页列表
     * @param query
     * @return
     */
    ResultInfo findPageGird(OrganizationsQueryDto query);

    /**
     * 根据ID获取机构全名称
     * @param id
     * @return
     */
    String getOrgFullName(Long id);

    /**
     * 根据ID 获取机构名称
     * @param id
     * @return
     */
    String getOrgName(Long id);

    /**
     * 根据一组id 获取数据
     * @param ids
     * @return key = id  value = name
     */
    Map<Long, String> findKeyIdValueNameByIdIn(List<Long> ids);

    /**
     * 根据ID获取详细信息
     * @param id
     * @return
     */
    ResultInfo selectById(Long id);
}
