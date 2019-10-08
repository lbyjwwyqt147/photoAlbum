package pers.liujunyi.cloud.photo.service.activities.impl;

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
import pers.liujunyi.cloud.photo.domain.activities.NewActivitiesQueryDto;
import pers.liujunyi.cloud.photo.domain.activities.NewActivitiesVo;
import pers.liujunyi.cloud.photo.entity.activities.ActivityImags;
import pers.liujunyi.cloud.photo.entity.activities.NewActivities;
import pers.liujunyi.cloud.photo.repository.elasticsearch.activities.ActivityImagsElasticsearchRepository;
import pers.liujunyi.cloud.photo.repository.elasticsearch.activities.NewActivitiesElasticsearchRepository;
import pers.liujunyi.cloud.photo.service.activities.NewActivitiesElasticsearchService;
import pers.liujunyi.cloud.photo.util.Constant;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/***
 * 文件名称: NewActivitiesElasticsearchServiceImpl.java
 * 文件描述: 最新活动 Elasticsearch Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年09月23日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class NewActivitiesElasticsearchServiceImpl extends BaseElasticsearchServiceImpl<NewActivities, Long> implements NewActivitiesElasticsearchService {

    @Autowired
    private NewActivitiesElasticsearchRepository newActivitiesElasticsearchRepository;
    @Autowired
    private ActivityImagsElasticsearchRepository activityImagsElasticsearchRepository;

    @Autowired
    private DictUtil dictUtil;

    public NewActivitiesElasticsearchServiceImpl(BaseElasticsearchRepository<NewActivities, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }

    @Override
    public ResultInfo findPageGird(NewActivitiesQueryDto query) {
        List<NewActivitiesVo> datas = new CopyOnWriteArrayList<>();
        // 排序方式
        Sort sort =  new Sort(Sort.Direction.ASC, "createTime");
        //分页参数
        Pageable pageable = query.toPageable(sort);
        // 查询数据
        SearchQuery searchQuery = query.toSpecPageable(pageable);
        Page<NewActivities> searchPageResults = this.newActivitiesElasticsearchRepository.search(searchQuery);
        List<NewActivities> searchList = searchPageResults.getContent();
        searchList.stream().forEach(item ->{
            NewActivitiesVo newActivitiesVo = DozerBeanMapperUtil.copyProperties(item, NewActivitiesVo.class);
            //获取相册图片信息
            List<ActivityImags> newActivitiesPictures = this.activityImagsElasticsearchRepository.findByActivityId(item.getId(), this.allPageable);
            newActivitiesVo.setTotal(newActivitiesPictures.size());
            newActivitiesVo.setActivityPictureData(newActivitiesPictures);
            datas.add(newActivitiesVo);
        });
        Long totalElements =  searchPageResults.getTotalElements();
        ResultInfo result = ResultUtil.success(datas);
        result.setTotal(totalElements);
        return  result;
    }

    @Override
    public ResultInfo details(Long id) {
        NewActivitiesVo newActivitiesVo = this.detailsById(id);
        if (newActivitiesVo != null) {
            //获取相册图片信息
            List<ActivityImags> newActivitiesPictures = this.activityImagsElasticsearchRepository.findByActivityId(newActivitiesVo.getId(), this.allPageable);
            newActivitiesVo.setTotal(newActivitiesPictures.size());
            newActivitiesVo.setActivityPictureData(newActivitiesPictures);
        }
        return ResultUtil.success(newActivitiesVo);
    }

    @Override
    public NewActivitiesVo detailsById(Long id) {
        NewActivitiesVo newActivitiesVo = null;
        NewActivities newActivities = this.findById(id);
        if (newActivities != null) {
            newActivitiesVo = DozerBeanMapperUtil.copyProperties(newActivities, NewActivitiesVo.class);
        }
        return newActivitiesVo;
    }

    @Override
    public NewActivities findById(Long id) {
        Optional<NewActivities> optional  = this.newActivitiesElasticsearchRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public ResultInfo findActivitiesPictureByActivityId(Long activityId) {
        List<ActivityImags> NewActivitiesPictures = this.activityImagsElasticsearchRepository.findByActivityId(activityId, this.allPageable);
        return ResultUtil.success(NewActivitiesPictures);
    }

    @Override
    public ResultInfo activitiesComboBox() {
        NewActivitiesQueryDto query = new NewActivitiesQueryDto();
        query.setPageNumber(1);
        query.setPageSize(20);
        query.setMaturity(Constant.ENABLE_STATUS);
        query.setActivityStatus(Constant.ENABLE_STATUS);
        // 排序方式
        Sort sort =  new Sort(Sort.Direction.DESC, "createTime");
        //分页参数
        Pageable pageable = query.toPageable(sort);
        // 查询数据
        SearchQuery searchQuery = query.toSpecPageable(pageable);
        Page<NewActivities> searchPageResults = this.newActivitiesElasticsearchRepository.search(searchQuery);
        List<NewActivities> searchList = searchPageResults.getContent();
        List<Map<String, String>> resultData = new CopyOnWriteArrayList<>();
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("id", "");
        map.put("text", "--请选择--");
        resultData.add(map);
        if (!CollectionUtils.isEmpty(searchList)) {
            searchList.stream().forEach(item -> {
                Map<String, String> activitiesMap = new ConcurrentHashMap<>();
                activitiesMap.put("id", item.getId().toString());
                activitiesMap.put("text", item.getActivityTheme() + " 价格:¥" + item.getActivityPrice());
                resultData.add(activitiesMap);
            });
        }
        return ResultUtil.success(resultData);
    }

}
