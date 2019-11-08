package pers.liujunyi.cloud.photo.service.user.impl;

import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseElasticsearchServiceImpl;
import pers.liujunyi.cloud.photo.domain.user.CustomerDetailInfoQueryDto;
import pers.liujunyi.cloud.photo.domain.user.CustomerDetailInfoVo;
import pers.liujunyi.cloud.photo.entity.user.CustomerDetailInfo;
import pers.liujunyi.cloud.photo.repository.elasticsearch.user.CustomerDetailInfoElasticsearchRepository;
import pers.liujunyi.cloud.photo.service.user.CustomerDetailInfoElasticsearchService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/***
 * 文件名称: CustomerDetailInfoElasticsearchServiceImpl.java
 * 文件描述: 顾客档案信息 Elasticsearch Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class CustomerDetailInfoElasticsearchServiceImpl extends BaseElasticsearchServiceImpl<CustomerDetailInfo, Long> implements CustomerDetailInfoElasticsearchService {

    @Autowired
    private CustomerDetailInfoElasticsearchRepository customerDetailInfoElasticsearchRepository;

    public CustomerDetailInfoElasticsearchServiceImpl(BaseElasticsearchRepository<CustomerDetailInfo, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }


    @Override
    public CustomerDetailInfo findFirstByCustomerAccountsIdAndCustomerStatus(Long customerAccountsId, Byte customerStatus) {
        return this.customerDetailInfoElasticsearchRepository.findFirstByCustomerAccountsIdAndCustomerStatus(customerAccountsId, customerStatus);
    }

    @Override
    public CustomerDetailInfo findCustomerDetails(Long customerAccountsId) {
        return this.findFirstByCustomerAccountsIdAndCustomerStatus(customerAccountsId, null);
    }

    @Override
    public ResultInfo findPageGird(CustomerDetailInfoQueryDto query) {
        // 排序方式 解决无数据时异常 No mapping found for [createTime] in order to sort on
        SortBuilder sortBuilder = SortBuilders.fieldSort("createTime").unmappedType("date").order(SortOrder.DESC);
        // 如果使用这种排序方式 如果表中数据为空时,会报异常 No mapping found for [createTime] in order to sort on
        //Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        // 查询数据
        SearchQuery searchQuery = query.toSpecSortPageable(sortBuilder);
        Page<CustomerDetailInfo> searchPageResults = this.customerDetailInfoElasticsearchRepository.search(searchQuery);
        List<CustomerDetailInfo> searchDataList = searchPageResults.getContent();
        List<CustomerDetailInfoVo> resultDataList = new CopyOnWriteArrayList<>();
        if (!CollectionUtils.isEmpty(searchDataList)) {

            searchDataList.stream().forEach(item -> {

            });
        }
        Long totalElements =  searchPageResults.getTotalElements();
        ResultInfo result = ResultUtil.success(resultDataList);
        result.setTotal(totalElements);
        return  result;
    }

    @Override
    public ResultInfo findByCustomerAccountsId(Long customerAccountsId) {
        return ResultUtil.success(this.getCustomerDetailsByCustomerAccountsId(customerAccountsId));
    }

    @Override
    public CustomerDetailInfoVo getCustomerDetailsByCustomerAccountsId(Long customerAccountsId) {
        return null;
    }

    @Override
    public ResultInfo findById(Long id) {
        return ResultUtil.success(this.getCustomerDetailsById(id));
    }

    @Override
    public CustomerDetailInfoVo getCustomerDetailsById(Long id) {
        return null;
    }
}
