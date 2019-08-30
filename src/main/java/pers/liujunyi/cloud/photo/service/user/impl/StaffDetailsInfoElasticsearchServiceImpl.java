package pers.liujunyi.cloud.photo.service.user.impl;

import org.apache.commons.lang3.StringUtils;
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
import pers.liujunyi.cloud.common.util.DictUtil;
import pers.liujunyi.cloud.common.util.DozerBeanMapperUtil;
import pers.liujunyi.cloud.photo.domain.user.StaffDetailsInfoQueryDto;
import pers.liujunyi.cloud.photo.domain.user.StaffDetailsInfoVo;
import pers.liujunyi.cloud.photo.entity.user.StaffDetailsInfo;
import pers.liujunyi.cloud.photo.repository.elasticsearch.user.StaffDetailsInfoElasticsearchRepository;
import pers.liujunyi.cloud.photo.service.user.StaffDetailsInfoElasticsearchService;
import pers.liujunyi.cloud.photo.util.Constant;
import pers.liujunyi.cloud.photo.util.DictConstant;
import pers.liujunyi.cloud.photo.util.PhotoUtils;
import pers.liujunyi.cloud.security.entity.organizations.Organizations;
import pers.liujunyi.cloud.security.entity.user.UserAccounts;
import pers.liujunyi.cloud.security.service.organizations.StaffOrgElasticsearchService;
import pers.liujunyi.cloud.security.service.user.UserAccountsElasticsearchService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
    @Autowired
    private StaffOrgElasticsearchService staffOrgElasticsearchService;

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
            List<Long> staffIds =  searchDataList.stream().map(StaffDetailsInfo::getId).distinct().collect(Collectors.toList());
            // 获取行政区划
            List<Long> districtList = searchDataList.stream().map(StaffDetailsInfo::getDistrict).distinct().collect(Collectors.toList());
            Map<Long, String> districtMap = this.dictUtil.getAreaNameToMap(districtList);
            // 获取账户信息
            List<Long> accountIdList = searchDataList.stream().map(StaffDetailsInfo::getStaffAccountsId).distinct().collect(Collectors.toList());
            Map<Long, UserAccounts> userAccountsMap = this.userAccountsElasticsearchService.getUserAccountInfoToMap(accountIdList);
            // 获取组织机构信息
            Map<Long, List<Organizations>> orgInfoMap = this.staffOrgElasticsearchService.getOrgInfoByStaffIdIn(staffIds);
            searchDataList.stream().forEach(item -> {
                StaffDetailsInfoVo staffDetailsInfo = DozerBeanMapperUtil.copyProperties(item, StaffDetailsInfoVo.class);
                if (!CollectionUtils.isEmpty(dictMap)) {
                    Map<String, String> positionMap = dictMap.get(DictConstant.STAFF_POSITION);
                    staffDetailsInfo.setStaffPositionText(!CollectionUtils.isEmpty(positionMap) ? positionMap.get(item.getStaffPosition()) : "");
                }
                if (!CollectionUtils.isEmpty(orgInfoMap)) {
                    List<Organizations> organizationsList = orgInfoMap.get(item.getId());
                    if (!CollectionUtils.isEmpty(organizationsList)) {
                        Organizations organizations = organizationsList.get(0);
                        staffDetailsInfo.setStaffOrgName(organizations.getFullName());
                        staffDetailsInfo.setStaffOrgId(organizations.getId());
                    }
                }
                staffDetailsInfo.setAddressText(!CollectionUtils.isEmpty(districtMap) ? districtMap.get(item.getDistrict()) : "");
                staffDetailsInfo.setDataVersion(userAccountsMap.get(item.getStaffAccountsId()).getDataVersion());
                staffDetailsInfo.setUserId(item.getStaffAccountsId());
                if (StringUtils.isBlank(item.getWorkingYears())) {
                    staffDetailsInfo.setWorkingYears(PhotoUtils.getWorkingYears(item.getEntryDate(), item.getDimissionDate()));
                }
                // 计算年龄
                if (item.getBirthday() != null && item.getStaffAge() == null) {
                    staffDetailsInfo.setStaffAge(PhotoUtils.getAge(item.getBirthday()));
                }
                resultDataList.add(staffDetailsInfo);
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
        Optional<StaffDetailsInfo> optional  = this.staffDetailsInfoElasticsearchRepository.findById(id);
        if (optional.isPresent()) {
            StaffDetailsInfoVo detailsInfoVo = DozerBeanMapperUtil.copyProperties(optional.get(), StaffDetailsInfoVo.class);
            // 获取数据字典值
            List<String> dictCodeList = new LinkedList<>();
            dictCodeList.add(DictConstant.STAFF_SKILL);
            dictCodeList.add(DictConstant.STAFF_POSITION);
            Map<String, Map<String, String>> dictMap = this.dictUtil.getDictNameToMapList(dictCodeList);
            // 获取行政区划
            String district = this.dictUtil.getAreaName(detailsInfoVo.getDistrict());
            // 获取账户信息
            UserAccounts userAccounts = this.userAccountsElasticsearchService.getOne(detailsInfoVo.getStaffAccountsId());
            // 获取组织机构信息
            List<Organizations> orgInfo = this.staffOrgElasticsearchService.getOrgInfoByStaffId(id);
            if (!CollectionUtils.isEmpty(dictMap)) {
                Map<String, String> positionMap = dictMap.get(DictConstant.STAFF_POSITION);
                detailsInfoVo.setStaffPositionText(!CollectionUtils.isEmpty(positionMap) ? positionMap.get(detailsInfoVo.getStaffPosition()) : "");
                Map<String, String> skillMap = dictMap.get(DictConstant.STAFF_SKILL);
                Set<String> skillBuffer =  new HashSet<>();
                String[] skillList = detailsInfoVo.getSkill().split(",");
                if (!CollectionUtils.isEmpty(skillMap)) {
                    for (String skill : skillList) {
                        skillBuffer.add(skillMap.get(skill));
                    }
                }
                detailsInfoVo.setSkillText(StringUtils.join(skillBuffer, ","));
            }
            if (!CollectionUtils.isEmpty(orgInfo)) {
                Organizations organizations = orgInfo.get(0);
                detailsInfoVo.setStaffOrgName(organizations.getFullName());
                detailsInfoVo.setStaffOrgId(organizations.getId());
            }
            if (StringUtils.isBlank(detailsInfoVo.getWorkingYears())) {
                detailsInfoVo.setWorkingYears(PhotoUtils.getWorkingYears(detailsInfoVo.getEntryDate(), detailsInfoVo.getDimissionDate()));
            }
            if (detailsInfoVo.getBirthday() != null && detailsInfoVo.getStaffAge() == null) {
                detailsInfoVo.setStaffAge(PhotoUtils.getAge(detailsInfoVo.getBirthday()));
            }
            detailsInfoVo.setAddressText(district + detailsInfoVo.getStreet());
            detailsInfoVo.setDataVersion(userAccounts.getDataVersion());
            detailsInfoVo.setUserId(detailsInfoVo.getStaffAccountsId());
            return detailsInfoVo;
        }
        return null;
    }

    @Override
    public List<Map<String, String>> staffSelect(StaffDetailsInfoQueryDto query) {
        query.setStaffStatus(Constant.ENABLE_STATUS);
        List<Map<String, String>> result = new LinkedList<>();
        //分页参数
        Pageable pageable = this.allPageable;
        // 查询数据
        SearchQuery searchQuery = query.toSpecPageable(pageable);
        Page<StaffDetailsInfo> searchPageResults = this.staffDetailsInfoElasticsearchRepository.search(searchQuery);
        List<StaffDetailsInfo> staffDetailsInfoList = searchPageResults.getContent();
        if (!CollectionUtils.isEmpty(staffDetailsInfoList)) {
            staffDetailsInfoList.stream().forEach(item -> {
                Map<String, String> map = new ConcurrentHashMap<>();
                map.put("id", item.getId().toString());
                map.put("text", item.getStaffName());
                result.add(map);
            });
        }
        return result;
    }

    @Override
    public Map<Long, String> getStaffNameMap(List<Long> ids) {
        List<StaffDetailsInfo> staffDetailsInfoList = this.findAllByIdIn(ids);
        if (!CollectionUtils.isEmpty(staffDetailsInfoList)) {
            Map<Long, String> nameMap = new ConcurrentHashMap<>();
            staffDetailsInfoList.stream().forEach(item -> {
                nameMap.put(item.getId(), item.getStaffName());
            });
            return nameMap;
        }
        return null;
    }

    @Override
    public Map<Long, StaffDetailsInfo> getDetailsInfoMap(List<Long> ids) {
        List<StaffDetailsInfo> staffDetailsInfoList = this.findAllByIdIn(ids);
        if (!CollectionUtils.isEmpty(staffDetailsInfoList)) {
            return staffDetailsInfoList.stream().collect(Collectors.toMap(StaffDetailsInfo::getId, detailsInfo -> detailsInfo));
        }
        return null;
    }


}
