package pers.liujunyi.cloud.photo.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseElasticsearchServiceImpl;
import pers.liujunyi.cloud.common.util.DateTimeUtils;
import pers.liujunyi.cloud.common.util.DayCompare;
import pers.liujunyi.cloud.common.util.DictUtil;
import pers.liujunyi.cloud.common.util.DozerBeanMapperUtil;
import pers.liujunyi.cloud.photo.domain.user.StaffDetailsInfoQueryDto;
import pers.liujunyi.cloud.photo.domain.user.StaffDetailsInfoVo;
import pers.liujunyi.cloud.photo.entity.StaffDetailsInfo;
import pers.liujunyi.cloud.photo.repository.elasticsearch.user.StaffDetailsInfoElasticsearchRepository;
import pers.liujunyi.cloud.photo.service.user.StaffDetailsInfoElasticsearchService;
import pers.liujunyi.cloud.photo.util.DictConstant;
import pers.liujunyi.cloud.security.entity.user.UserAccounts;
import pers.liujunyi.cloud.security.service.user.UserAccountsElasticsearchService;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


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
    private UserAccountsElasticsearchService userAccountsElasticsearchService;
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
        Sort sort =  new Sort(Sort.Direction.DESC, "entryDate");
        //分页参数
        Pageable pageable = query.toPageable(sort);
        // 查询数据
        SearchQuery searchQuery = query.toSpecPageable(pageable);
        Page<StaffDetailsInfo> searchPageResults = this.staffDetailsInfoElasticsearchRepository.search(searchQuery);
        List<StaffDetailsInfo> searchDataList = searchPageResults.getContent();
        List<StaffDetailsInfoVo> resultDataList = new CopyOnWriteArrayList<>();
        if (!CollectionUtils.isEmpty(searchDataList)) {
            // 获取数据字典值
            List<String> dictCodeList = new LinkedList<>();
            dictCodeList.add(DictConstant.STAFF_POSITION);
            Map<String, Map<String, String>> dictMap = this.dictUtil.getDictNameToMapList(dictCodeList);
            // 获取行政区划
            List<Long> districtList = searchDataList.stream().map(StaffDetailsInfo::getDistrict).distinct().collect(Collectors.toList());
            Map<Long, String> districtMap = this.dictUtil.getAreaNameToMap(districtList);
            // 获取账户信息
            List<Long> accountIdList = searchDataList.stream().map(StaffDetailsInfo::getStaffAccountsId).distinct().collect(Collectors.toList());
            Map<Long, UserAccounts> userAccountsMap = this.userAccountsElasticsearchService.getUserAccountInfoToMap(accountIdList);
            searchDataList.stream().forEach(item -> {
                if (!CollectionUtils.isEmpty(dictMap)) {
                    StaffDetailsInfoVo staffDetailsInfo = DozerBeanMapperUtil.copyProperties(item, StaffDetailsInfoVo.class);
                    Map<String, String> positionMap = dictMap.get(DictConstant.STAFF_POSITION);
                    staffDetailsInfo.setStaffPositionText(!CollectionUtils.isEmpty(positionMap) ? positionMap.get(item.getStaffPosition()) : "");
                    staffDetailsInfo.setAddressText(!CollectionUtils.isEmpty(districtMap) ? districtMap.get(item.getDistrict()) : "");
                    // 计算在职年限
                    DayCompare dayCompare = DateTimeUtils.dayCompare(item.getEntryDate(), new Date());
                    staffDetailsInfo.setDuration(dayCompare.getMonth());
                    staffDetailsInfo.setDataVersion(userAccountsMap.get(item.getStaffAccountsId()).getDataVersion());
                    resultDataList.add(staffDetailsInfo);
                }
            });
        }
        Long totalElements =  searchPageResults.getTotalElements();
        ResultInfo result = ResultUtil.success(resultDataList);
        result.setTotal(totalElements);
        return  result;
    }

    @Override
    public ResultInfo findByStaffAccountsId(Long staffAccountsId) {
        return ResultUtil.success(this.getStaffDetailsByStaffAccountsId(staffAccountsId));
    }

    @Override
    public StaffDetailsInfoVo getStaffDetailsByStaffAccountsId(Long staffAccountsId) {
        return null;
    }

    @Override
    public ResultInfo findById(Long id) {
        return ResultUtil.success(this.getStaffDetailsById(id));
    }

    @Override
    public StaffDetailsInfoVo getStaffDetailsById(Long id) {
        return null;
    }
}
