package pers.liujunyi.cloud.photo.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseElasticsearchServiceImpl;
import pers.liujunyi.cloud.common.util.DictUtil;
import pers.liujunyi.cloud.photo.domain.user.StaffDetailsInfoQueryDto;
import pers.liujunyi.cloud.photo.entity.StaffDetailsInfo;
import pers.liujunyi.cloud.photo.repository.elasticsearch.user.StaffDetailsInfoElasticsearchRepository;
import pers.liujunyi.cloud.photo.service.user.StaffDetailsInfoElasticsearchService;

import java.util.List;


/***
 * 文件名称: StaffDetailsInfoElasticsearchServiceImpl.java
 * 文件描述: 职工档案信息 Elasticsearch Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class StaffDetailsInfoElasticsearchServiceImpl extends BaseElasticsearchServiceImpl<StaffDetailsInfo, Long> implements StaffDetailsInfoElasticsearchService {

    @Autowired
    private StaffDetailsInfoElasticsearchRepository staffDetailsInfoElasticsearchRepository;
    @Autowired
    private DictUtil dictUtil;

    public StaffDetailsInfoElasticsearchServiceImpl(BaseElasticsearchRepository<StaffDetailsInfo, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }


    @Override
    public StaffDetailsInfo findFirstByStaffAccountsIdAndStaffStatus(Long staffAccountsId, Byte staffStatus) {
        return this.staffDetailsInfoElasticsearchRepository.findFirstByStaffAccountsIdAndStaffStatus(staffAccountsId, staffStatus);
    }

    @Override
    public StaffDetailsInfo findStaffDetails(Long staffAccountsId) {
        return this.staffDetailsInfoElasticsearchRepository.findFirstByStaffAccountsId(staffAccountsId);
    }

    @Override
    public ResultInfo findPageGird(StaffDetailsInfoQueryDto query) {
        // 排序方式
        Sort sort =  new Sort(Sort.Direction.ASC, "id");
        //分页参数
        Pageable pageable = query.toPageable(sort);
        // 查询数据
        SearchQuery searchQuery = query.toSpecPageable(pageable);
        Page<StaffDetailsInfo> searchPageResults = this.staffDetailsInfoElasticsearchRepository.search(searchQuery);
        List<StaffDetailsInfo> searchDataList = searchPageResults.getContent();
        searchDataList.stream().forEach(item -> {
        });
        Long totalElements =  searchPageResults.getTotalElements();
        ResultInfo result = ResultUtil.success(searchDataList);
        result.setTotal(totalElements);
        return  result;
    }
}
