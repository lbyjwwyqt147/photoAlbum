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
import pers.liujunyi.cloud.photo.domain.album.AlbumDto;
import pers.liujunyi.cloud.photo.entity.album.Album;
import pers.liujunyi.cloud.photo.entity.album.AlbumPicture;
import pers.liujunyi.cloud.photo.repository.elasticsearch.album.AlbumElasticsearchRepository;
import pers.liujunyi.cloud.photo.repository.elasticsearch.album.AlbumPictureElasticsearchRepository;
import pers.liujunyi.cloud.photo.repository.jpa.album.AlbumPictureRepository;
import pers.liujunyi.cloud.photo.repository.jpa.album.AlbumRepository;
import pers.liujunyi.cloud.photo.service.album.AlbumService;
import pers.liujunyi.cloud.photo.util.Constant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/***
 * 文件名称: AlbumServiceImpl.java
 * 文件描述: 相册管理 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月04日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class AlbumServiceImpl extends BaseServiceImpl<Album, Long> implements AlbumService {

    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private AlbumElasticsearchRepository albumElasticsearchRepository;
    @Autowired
    private AlbumPictureRepository albumPictureRepository;
    @Autowired
    private AlbumPictureElasticsearchRepository albumPictureElasticsearchRepository;
    @Autowired
    private FileManageUtil fileManageUtil;

    public AlbumServiceImpl(BaseRepository<Album, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(AlbumDto record) {
        Album album = DozerBeanMapperUtil.copyProperties(record, Album.class);
        boolean add = album.getId() != null ? false : true;
        if (add) {
            album.setAlbumNumber(String.valueOf(System.currentTimeMillis()));
        }
        if (record.getAlbumPriority() == null) {
            album.setAlbumPriority((byte) 10);
        }
        Album saveObject = this.albumRepository.save(album);
        if (saveObject == null || saveObject.getId() == null) {
            return ResultUtil.fail();
        }
        if (!add) {
            saveObject.setDataVersion(record.getDataVersion() + 1);
        }
        List<AlbumPicture> albumPictureList = new LinkedList<>();
        JSONArray jsonArray = JSON.parseArray(record.getPictures());
        byte i = 1;
        for (Object json : jsonArray) {
            JSONObject jsonObject = (JSONObject) json;
            AlbumPicture albumPicture = new AlbumPicture();
            albumPicture.setAlbumId(saveObject.getId());
            albumPicture.setPictureId(jsonObject.getLong("id"));
            albumPicture.setPictureLocation(jsonObject.getString("fileCallAddress"));
            albumPicture.setPictureName(jsonObject.getString("fileName"));
            albumPicture.setPictureCategory(jsonObject.getByte("fileCategory"));
            albumPicture.setPictureSize(jsonObject.getLong("fileSize"));
            albumPicture.setPictureType(jsonObject.getString("fileSuffix"));
            albumPicture.setStatus(Constant.ENABLE_STATUS);
            albumPicture.setPriority(i);
            if (i == 1) {
                albumPicture.setCover((byte) 0);
            } else {
                albumPicture.setCover((byte) 1);
            }
            i++;
            albumPictureList.add(albumPicture);
        }
        List<Long> pictureIds = albumPictureList.stream().map(AlbumPicture::getPictureId).collect(Collectors.toList());
        List<AlbumPicture> pictureList = this.albumPictureRepository.findByPictureIdIn(pictureIds);
        if (!CollectionUtils.isEmpty(pictureList)) {
            Map<Long, List<AlbumPicture>> pictureMap = pictureList.stream().collect(Collectors.groupingBy(AlbumPicture::getPictureId));
            Iterator<AlbumPicture> albumPictureIterator = albumPictureList.iterator();
            while (albumPictureIterator.hasNext()) {
                AlbumPicture albumPicture = albumPictureIterator.next();
                if (pictureMap.get(albumPicture.getPictureId()) != null) {
                    albumPictureIterator.remove();
                }
            }
        }
        List<AlbumPicture> albumPictures =  this.albumPictureRepository.saveAll(albumPictureList);
        this.albumPictureElasticsearchRepository.saveAll(albumPictures);
        this.albumElasticsearchRepository.save(saveObject);
        return ResultUtil.success(saveObject.getId());
    }

    @Override
    public ResultInfo updateStatus(Byte status, Long id, Long dataVersion) {
        int count = this.albumRepository.setStatusByIds(status, id, dataVersion);
        if (count > 0) {
            Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            Map<String, Object> docDataMap = new HashMap<>();
            docDataMap.put("albumStatus", status);
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
        this.albumRepository.deleteById(id);
        List<AlbumPicture> pictureList = this.albumPictureElasticsearchRepository.findByAlbumId(id, this.allPageable);
        if (!CollectionUtils.isEmpty(pictureList)) {
            this.albumElasticsearchRepository.deleteById(id);
            List<Long> fileIds = pictureList.stream().map(AlbumPicture::getId).collect(Collectors.toList());
            this.albumPictureRepository.deleteByIdIn(fileIds);
            this.albumPictureElasticsearchRepository.deleteByIdIn(fileIds);
            List<Long> uploadFileIds = pictureList.stream().map(AlbumPicture::getPictureId).collect(Collectors.toList());
            // 删除服务器上的文件
            this.fileManageUtil.batchDeleteById(StringUtils.join(uploadFileIds, ","));
        }
        return ResultUtil.success();
    }

    @Override
    public ResultInfo syncDataToElasticsearch() {
        // 先同步相册信息
        Sort sort =  new Sort(Sort.Direction.ASC, "id");
        List<Album> albumList = this.albumRepository.findAll(sort);
        if (!CollectionUtils.isEmpty(albumList)) {
            this.albumElasticsearchRepository.deleteAll();
            // 限制条数
            int pointsDataLimit = 1000;
            int size = albumList.size();
            //判断是否有必要分批
            if(pointsDataLimit < size){
                //分批数
                int part = size/pointsDataLimit;
                for (int i = 0; i < part; i++) {
                    //1000条
                    List<Album> partList = new LinkedList<>(albumList.subList(0, pointsDataLimit));
                    //剔除
                    albumList.subList(0, pointsDataLimit).clear();
                    this.albumElasticsearchRepository.saveAll(partList);
                }
                //表示最后剩下的数据
                if (!CollectionUtils.isEmpty(albumList)) {
                    this.albumElasticsearchRepository.saveAll(albumList);
                }
            } else {
                this.albumElasticsearchRepository.saveAll(albumList);
            }
        } else {
            this.albumElasticsearchRepository.deleteAll();
        }
        // 同步相册照片信息
        List<AlbumPicture> pictureList = this.albumPictureRepository.findAll(sort);
        if (!CollectionUtils.isEmpty(pictureList)) {
            this.albumPictureElasticsearchRepository.deleteAll();
            // 限制条数
            int pointsDataLimit = 1000;
            int size = pictureList.size();
            //判断是否有必要分批
            if(pointsDataLimit < size){
                //分批数
                int part = size/pointsDataLimit;
                for (int i = 0; i < part; i++) {
                    //1000条
                    List<AlbumPicture> partList = new LinkedList<>(pictureList.subList(0, pointsDataLimit));
                    //剔除
                    pictureList.subList(0, pointsDataLimit).clear();
                    this.albumPictureElasticsearchRepository.saveAll(partList);
                }
                //表示最后剩下的数据
                if (!CollectionUtils.isEmpty(pictureList)) {
                    this.albumPictureElasticsearchRepository.saveAll(pictureList);
                }
            } else {
                this.albumPictureElasticsearchRepository.saveAll(pictureList);
            }
        } else {
            this.albumPictureElasticsearchRepository.deleteAll();
        }
        return ResultUtil.success();
    }

    @Override
    public ResultInfo deleteAlbumPictureById(Long pictureId) {
        AlbumPicture albumPicture = null;
        Optional<AlbumPicture> optional   = this.albumPictureElasticsearchRepository.findById(pictureId);
        if (optional.isPresent()) {
            albumPicture = optional.get();
        }
        this.albumPictureRepository.deleteById(pictureId);
        if (albumPicture != null) {
            this.albumPictureElasticsearchRepository.deleteById(pictureId);
            // 删除服务器上的文件
            this.fileManageUtil.batchDeleteById(albumPicture.getPictureId().toString());
        }
        return ResultUtil.success();
    }
}
