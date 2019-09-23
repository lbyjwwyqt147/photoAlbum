package pers.liujunyi.cloud.photo.service.activities.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseServiceImpl;
import pers.liujunyi.cloud.common.util.DozerBeanMapperUtil;
import pers.liujunyi.cloud.common.util.FileManageUtil;
import pers.liujunyi.cloud.photo.domain.activities.NewActivitiesDto;
import pers.liujunyi.cloud.photo.entity.activities.ActivityImags;
import pers.liujunyi.cloud.photo.entity.activities.NewActivities;
import pers.liujunyi.cloud.photo.repository.elasticsearch.activities.ActivityImagsElasticsearchRepository;
import pers.liujunyi.cloud.photo.repository.elasticsearch.activities.NewActivitiesElasticsearchRepository;
import pers.liujunyi.cloud.photo.repository.jpa.activities.ActivityImagsRepository;
import pers.liujunyi.cloud.photo.repository.jpa.activities.NewActivitiesRepository;
import pers.liujunyi.cloud.photo.service.activities.NewActivitiesService;
import pers.liujunyi.cloud.photo.util.Constant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/***
 * 文件名称: NewActivitiesServiceImpl.java
 * 文件描述: 最新活动 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月04日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class NewActivitiesServiceImpl extends BaseServiceImpl<NewActivities, Long> implements NewActivitiesService {

    @Autowired
    private NewActivitiesRepository newActivitiesRepository;
    @Autowired
    private NewActivitiesElasticsearchRepository newActivitiesElasticsearchRepository;
    @Autowired
    private ActivityImagsRepository activityImagsRepository;
    @Autowired
    private ActivityImagsElasticsearchRepository activityImagsElasticsearchRepository;
    @Autowired
    private FileManageUtil fileManageUtil;

    public NewActivitiesServiceImpl(BaseRepository<NewActivities, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(NewActivitiesDto record) {

        NewActivities newActivities = DozerBeanMapperUtil.copyProperties(record, NewActivities.class);
        boolean add = newActivities.getId() != null ? false : true;
        if (add) {
            newActivities.setActivityNumber(String.valueOf(System.currentTimeMillis()));
        }
        if (record.getActivityPriority() == null) {
            newActivities.setActivityPriority((byte) 10);
        }
        NewActivities saveObject = this.newActivitiesRepository.save(newActivities);
        if (saveObject == null || saveObject.getId() == null) {
            return ResultUtil.fail();
        }
        if (!add) {
            saveObject.setDataVersion(record.getDataVersion() + 1);
        }
        List<ActivityImags> ActivityImagsList = new LinkedList<>();
        JSONArray jsonArray = JSON.parseArray(record.getPictures());
        byte i = 1;
        for (Object json : jsonArray) {
            JSONObject jsonObject = (JSONObject) json;
            ActivityImags activityImags = new ActivityImags();
            activityImags.setActivityId(saveObject.getId());
            activityImags.setPictureId(jsonObject.getLong("id"));
            activityImags.setPictureLocation(jsonObject.getString("fileCallAddress"));
            activityImags.setPictureName(jsonObject.getString("fileName"));
            activityImags.setPictureCategory(jsonObject.getByte("fileCategory"));
            activityImags.setStatus(Constant.ENABLE_STATUS);
            activityImags.setPriority(i);
            i++;
            ActivityImagsList.add(activityImags);
        }
        List<Long> pictureIds = ActivityImagsList.stream().map(ActivityImags::getPictureId).collect(Collectors.toList());
        List<ActivityImags> pictureList = this.activityImagsRepository.findByPictureIdIn(pictureIds);
        if (!CollectionUtils.isEmpty(pictureList)) {
            Map<Long, List<ActivityImags>> pictureMap = pictureList.stream().collect(Collectors.groupingBy(ActivityImags::getPictureId));
            Iterator<ActivityImags> ActivityImagsIterator = ActivityImagsList.iterator();
            while (ActivityImagsIterator.hasNext()) {
                ActivityImags ActivityImags = ActivityImagsIterator.next();
                if (pictureMap.get(ActivityImags.getPictureId()) != null) {
                    ActivityImagsIterator.remove();
                }
            }
        }
        List<ActivityImags> ActivityImagss =  this.activityImagsRepository.saveAll(ActivityImagsList);
        this.activityImagsElasticsearchRepository.saveAll(ActivityImagss);
        this.newActivitiesElasticsearchRepository.save(saveObject);
        return ResultUtil.success(saveObject.getId());
    }

    @Override
    public ResultInfo updateStatus(Byte status, Long id, Long dataVersion) {
        int count = this.newActivitiesRepository.setStatusByIds(status, id, dataVersion);
        if (count > 0) {
            Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            Map<String, Object> docDataMap = new HashMap<>();
            docDataMap.put("activityStatus", status);
            docDataMap.put("updateTime", System.currentTimeMillis());
            docDataMap.put("dataVersion", dataVersion + 1);
            sourceMap.put(String.valueOf(id), docDataMap);
            super.updateBatchElasticsearchData(sourceMap);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }


    @Override
    public ResultInfo deleteBatch(List<Long> ids) {
        return null;
    }

    @Override
    public ResultInfo deleteSingle(Long id) {
        this.newActivitiesRepository.deleteById(id);
        List<ActivityImags> pictureList = this.activityImagsElasticsearchRepository.findByActivityId(id, this.allPageable);
        if (!CollectionUtils.isEmpty(pictureList)) {
            this.newActivitiesElasticsearchRepository.deleteById(id);
            List<Long> fileIds = pictureList.stream().map(ActivityImags::getId).collect(Collectors.toList());
            this.activityImagsRepository.deleteByIdIn(fileIds);
            this.activityImagsElasticsearchRepository.deleteByIdIn(fileIds);
            List<Long> uploadFileIds = pictureList.stream().map(ActivityImags::getPictureId).collect(Collectors.toList());
            // 删除服务器上的文件
            this.fileManageUtil.batchDeleteById(StringUtils.join(uploadFileIds, ","));
        }
        return ResultUtil.success();
    }

    @Override
    public ResultInfo syncDataToElasticsearch() {
        // 先同步相册信息
        Sort sort =  new Sort(Sort.Direction.ASC, "id");
        List<NewActivities> NewActivitiesList = this.newActivitiesRepository.findAll(sort);
        if (!CollectionUtils.isEmpty(NewActivitiesList)) {
            this.newActivitiesElasticsearchRepository.deleteAll();
            // 限制条数
            int pointsDataLimit = 1000;
            int size = NewActivitiesList.size();
            //判断是否有必要分批
            if(pointsDataLimit < size){
                //分批数
                int part = size/pointsDataLimit;
                for (int i = 0; i < part; i++) {
                    //1000条
                    List<NewActivities> partList = new LinkedList<>(NewActivitiesList.subList(0, pointsDataLimit));
                    //剔除
                    NewActivitiesList.subList(0, pointsDataLimit).clear();
                    this.newActivitiesElasticsearchRepository.saveAll(partList);
                }
                //表示最后剩下的数据
                if (!CollectionUtils.isEmpty(NewActivitiesList)) {
                    this.newActivitiesElasticsearchRepository.saveAll(NewActivitiesList);
                }
            } else {
                this.newActivitiesElasticsearchRepository.saveAll(NewActivitiesList);
            }
        } else {
            this.newActivitiesElasticsearchRepository.deleteAll();
        }
        // 同步相册照片信息
        List<ActivityImags> pictureList = this.activityImagsRepository.findAll(sort);
        if (!CollectionUtils.isEmpty(pictureList)) {
            this.activityImagsElasticsearchRepository.deleteAll();
            // 限制条数
            int pointsDataLimit = 1000;
            int size = pictureList.size();
            //判断是否有必要分批
            if(pointsDataLimit < size){
                //分批数
                int part = size/pointsDataLimit;
                for (int i = 0; i < part; i++) {
                    //1000条
                    List<ActivityImags> partList = new LinkedList<>(pictureList.subList(0, pointsDataLimit));
                    //剔除
                    pictureList.subList(0, pointsDataLimit).clear();
                    this.activityImagsElasticsearchRepository.saveAll(partList);
                }
                //表示最后剩下的数据
                if (!CollectionUtils.isEmpty(pictureList)) {
                    this.activityImagsElasticsearchRepository.saveAll(pictureList);
                }
            } else {
                this.activityImagsElasticsearchRepository.saveAll(pictureList);
            }
        } else {
            this.activityImagsElasticsearchRepository.deleteAll();
        }
        return ResultUtil.success();
    }

    @Override
    public ResultInfo deletePictureById(Long pictureId) {
        ActivityImags ActivityImags = null;
        Optional<ActivityImags> optional   = this.activityImagsElasticsearchRepository.findById(pictureId);
        if (optional.isPresent()) {
            ActivityImags = optional.get();
        }
        this.activityImagsRepository.deleteById(pictureId);
        if (ActivityImags != null) {
            this.activityImagsElasticsearchRepository.deleteById(pictureId);
            // 删除服务器上的文件
            this.fileManageUtil.batchDeleteById(ActivityImags.getPictureId().toString());
        }
        return ResultUtil.success();
    }
}
