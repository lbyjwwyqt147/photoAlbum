package pers.liujunyi.cloud.photo.service.permission.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.photo.domain.permission.OrganizationsQueryDto;
import pers.liujunyi.cloud.photo.entity.permission.Organizations;
import pers.liujunyi.cloud.photo.repository.elasticsearch.permission.OrganizationsElasticsearchRepository;
import pers.liujunyi.cloud.photo.service.permission.OrganizationsElasticsearchService;
import pers.liujunyi.cloud.photo.util.Constant;
import pers.liujunyi.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.restful.ResultUtil;
import pers.liujunyi.common.service.impl.BaseElasticsearchServiceImpl;
import pers.liujunyi.common.vo.tree.ZTreeBuilder;
import pers.liujunyi.common.vo.tree.ZTreeNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/***
 * 文件名称: OrganizationsElasticsearchServiceImpl.java
 * 文件描述: 组织机构 Elasticsearch Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class OrganizationsElasticsearchServiceImpl extends BaseElasticsearchServiceImpl<Organizations, Long> implements OrganizationsElasticsearchService {

    @Autowired
    private OrganizationsElasticsearchRepository organizationsElasticsearchRepository;

    public OrganizationsElasticsearchServiceImpl(BaseElasticsearchRepository<Organizations, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }


    @Override
    public List<ZTreeNode> orgTree(Long pid) {
        if (pid.longValue() == 0) {
            pid = null;
        }
        List<ZTreeNode> treeNodes = new LinkedList<>();
        List<Organizations> list = this.organizationsElasticsearchRepository.findByParentIdAndOrgStatusOrderBySeqAsc(pid,  Constant.ENABLE_STATUS, super.allPageable);
        if (!CollectionUtils.isEmpty(list)){
            list.stream().forEach(item -> {
                ZTreeNode zTreeNode = new ZTreeNode(item.getId(), item.getParentId(), item.getOrgName());
                treeNodes.add(zTreeNode);
            });
        }
        return ZTreeBuilder.buildListToTree(treeNodes);
    }

    @Override
    public ResultInfo findPageGird(OrganizationsQueryDto query) {
        // 排序方式
        Sort sort =  new Sort(Sort.Direction.ASC, "seq");
        //分页参数
        Pageable pageable = query.toPageable(sort);
        // 查询数据
        SearchQuery searchQuery = query.toSpecPageable(pageable);
        Page<Organizations> searchPageResults = this.organizationsElasticsearchRepository.search(searchQuery);
        Long totalElements =  searchPageResults.getTotalElements();
        ResultInfo result = ResultUtil.success(searchPageResults.getContent());
        result.setTotal(totalElements);
        return  result;
    }

    @Override
    public String getOrgFullName(Long id) {
        Organizations organizations = getOrganizations(id);
        if (organizations != null) {
            return organizations.getFullName();
        }
        return "";
    }

    @Override
    public String getOrgName(Long id) {
        Organizations organizations = getOrganizations(id);
        if (organizations != null) {
            return organizations.getOrgName();
        }
        return "";
    }

    @Override
    public Map<Long, String> findKeyIdValueNameByIdIn(List<Long> ids) {
        List<Organizations> list = this.organizationsElasticsearchRepository.findByIdIn(ids, super.getPageable(ids.size()));
        if (!CollectionUtils.isEmpty(list)) {
            return  list.stream().collect(Collectors.toMap(Organizations::getId, Organizations::getOrgName));
        }
        return null;
    }

    @Override
    public ResultInfo selectById(Long id) {
        Organizations  search =  this.getOrganizations(id);
        if (search != null) {
            return ResultUtil.success(search);
        }
        return ResultUtil.fail();
    }

    /**
     * 根据id 获取数据
     * @param id
     * @return
     */
    private Organizations getOrganizations(Long id) {
        Optional<Organizations> organizations = this.organizationsElasticsearchRepository.findById(id);
        if (organizations.isPresent()) {
            return organizations.get();
        }
        return null;
    }
}
