package pers.liujunyi.cloud.photo.service.album.impl;

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
import pers.liujunyi.cloud.photo.domain.album.RollingPictureDto;
import pers.liujunyi.cloud.photo.entity.album.RollingPicture;
import pers.liujunyi.cloud.photo.repository.elasticsearch.album.RollingPictureElasticsearchRepository;
import pers.liujunyi.cloud.photo.repository.jpa.album.RollingPictureRepository;
import pers.liujunyi.cloud.photo.service.album.RollingPictureService;
import pers.liujunyi.cloud.photo.util.Constant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/***
 * 文件名称: RollingPictureServiceImpl.java
 * 文件描述: 轮播图 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月04日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class RollingPictureServiceImpl extends BaseServiceImpl<RollingPicture, Long> implements RollingPictureService {

    @Autowired
    private RollingPictureRepository rollingPictureRepository;
    @Autowired
    private RollingPictureElasticsearchRepository rollingPictureElasticsearchRepository;
    @Autowired
    private FileManageUtil fileManageUtil;

    public RollingPictureServiceImpl(BaseRepository<RollingPicture, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(RollingPictureDto record) {
        if (record.getPriority() == null) {
            record.setPriority((byte) 10);
        }
        List<RollingPicture> rollingPictures = new LinkedList<>();
        JSONArray jsonArray = JSON.parseArray(record.getPictures());
        for (Object json : jsonArray) {
            JSONObject jsonObject = (JSONObject) json;
            RollingPicture rollingPicture = DozerBeanMapperUtil.copyProperties(record, RollingPicture.class);
            rollingPicture.setPictureId(jsonObject.getLong("id"));
            rollingPicture.setPictureLocation(jsonObject.getString("fileCallAddress"));
            rollingPicture.setPictureCategory(jsonObject.getByte("fileCategory"));
            rollingPicture.setStatus(Constant.ENABLE_STATUS);
            rollingPictures.add(rollingPicture);
        }
        List<RollingPicture> save = this.rollingPictureRepository.saveAll(rollingPictures);
        if (save.size() > 0) {
            this.rollingPictureElasticsearchRepository.saveAll(rollingPictures);
            return ResultUtil.success();
        } else {
            return ResultUtil.fail();
        }

    }

    @Override
    public ResultInfo updateStatus(Byte status, Long id) {
        int count = this.rollingPictureRepository.setStatusByIds(status, id);
        if (count > 0) {
            Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            Map<String, Object> docDataMap = new HashMap<>();
            docDataMap.put("status", status);
            docDataMap.put("updateTime", System.currentTimeMillis());
            sourceMap.put(String.valueOf(id), docDataMap);
            super.updateBatchElasticsearchData(sourceMap);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo deleteBatch(List<Long> ids) {
        List<RollingPicture> rollingPictureList = this.rollingPictureElasticsearchRepository.findAllByIdIn(ids);
        long count = this.rollingPictureRepository.deleteByIdIn(ids);
        if (count > 0) {
            this.rollingPictureElasticsearchRepository.deleteByIdIn(ids);
            if (!CollectionUtils.isEmpty(rollingPictureList)) {
                List<Long> uploadFileIds = rollingPictureList.stream().map(RollingPicture::getPictureId).collect(Collectors.toList());
                // 删除服务器上的文件
                this.fileManageUtil.batchDeleteById(StringUtils.join(uploadFileIds, ","));
            }
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo deleteSingle(Long id) {
        RollingPicture rollingPicture = null;
        Optional<RollingPicture> optional = this.rollingPictureElasticsearchRepository.findById(id);
        if (optional.isPresent()) {
            rollingPicture = optional.get();
        }
        this.rollingPictureRepository.deleteById(id);
        if (rollingPicture != null) {
            this.rollingPictureElasticsearchRepository.deleteById(id);
            // 删除服务器上的文件
            this.fileManageUtil.singleDeleteById(rollingPicture.getPictureId());
        }
        return ResultUtil.success();
    }

    @Override
    public ResultInfo deleteByBusinessCodeAndAndPosition(String businessCode, String position) {
        List<RollingPicture> rollingPictureList = this.rollingPictureElasticsearchRepository.findByBusinessCodeAndPositionOrderByPriority(businessCode, position);
        int count = this.rollingPictureRepository.deleteByBusinessCodeAndAndPosition(businessCode, position);
        if (count > 0) {
            this.rollingPictureElasticsearchRepository.deleteByBusinessCodeAndAndPosition(businessCode, position);
            if (!CollectionUtils.isEmpty(rollingPictureList)) {
                List<Long> uploadFileIds = rollingPictureList.stream().map(RollingPicture::getPictureId).collect(Collectors.toList());
                // 删除服务器上的文件
                this.fileManageUtil.batchDeleteById(StringUtils.join(uploadFileIds, ","));
            }
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo syncDataToElasticsearch() {
        // 先同步相册信息
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<RollingPicture> albumList = this.rollingPictureRepository.findAll(sort);
        if (!CollectionUtils.isEmpty(albumList)) {
            this.rollingPictureElasticsearchRepository.deleteAll();
            // 限制条数
            int pointsDataLimit = 1000;
            int size = albumList.size();
            //判断是否有必要分批
            if(pointsDataLimit < size){
                //分批数
                int part = size/pointsDataLimit;
                for (int i = 0; i < part; i++) {
                    //1000条
                    List<RollingPicture> partList = new LinkedList<>(albumList.subList(0, pointsDataLimit));
                    //剔除
                    albumList.subList(0, pointsDataLimit).clear();
                    this.rollingPictureElasticsearchRepository.saveAll(partList);
                }
                //表示最后剩下的数据
                if (!CollectionUtils.isEmpty(albumList)) {
                    this.rollingPictureElasticsearchRepository.saveAll(albumList);
                }
            } else {
                this.rollingPictureElasticsearchRepository.saveAll(albumList);
            }
        } else {
            this.rollingPictureElasticsearchRepository.deleteAll();
        }

        return ResultUtil.success();
    }

    @Override
    public int deleteByBusinessIdAndVariety(Long businessId, String variety) {
        int count = this.rollingPictureElasticsearchRepository.deleteByBusinessIdAndVariety(businessId, variety);
        count = this.rollingPictureRepository.deleteByBusinessIdAndVariety(businessId, variety);
        return count;
    }


}
