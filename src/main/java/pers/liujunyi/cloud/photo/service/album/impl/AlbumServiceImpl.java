package pers.liujunyi.cloud.photo.service.album.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.photo.domain.album.AlbumDto;
import pers.liujunyi.cloud.photo.entity.Album;
import pers.liujunyi.cloud.photo.entity.AlbumPicture;
import pers.liujunyi.cloud.photo.repository.elasticsearch.album.AlbumElasticsearchRepository;
import pers.liujunyi.cloud.photo.repository.elasticsearch.album.AlbumPictureElasticsearchRepository;
import pers.liujunyi.cloud.photo.repository.jpa.album.AlbumPictureRepository;
import pers.liujunyi.cloud.photo.repository.jpa.album.AlbumRepository;
import pers.liujunyi.cloud.photo.service.album.AlbumService;
import pers.liujunyi.cloud.photo.util.Constant;
import pers.liujunyi.common.repository.jpa.BaseRepository;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.restful.ResultUtil;
import pers.liujunyi.common.service.impl.BaseServiceImpl;
import pers.liujunyi.common.util.DozerBeanMapperUtil;

import java.util.LinkedList;
import java.util.List;

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

    public AlbumServiceImpl(BaseRepository<Album, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(AlbumDto record) {
        Album album = DozerBeanMapperUtil.copyProperties(record, Album.class);
        album.setAlbumNumber(String.valueOf(System.currentTimeMillis()));
        album.setAlbumPriority((byte) 10);
        Album saveObject = this.albumRepository.save(album);
        if (saveObject == null || saveObject.getId() == null) {
            return ResultUtil.fail();
        }
        this.albumElasticsearchRepository.save(saveObject);
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
        List<AlbumPicture> albumPictures =  this.albumPictureRepository.saveAll(albumPictureList);
        this.albumPictureElasticsearchRepository.saveAll(albumPictures);
        return ResultUtil.success();
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids) {
        return null;
    }

    @Override
    public ResultInfo batchDeletes(List<Long> ids) {
        return null;
    }

    @Override
    public ResultInfo syncDataToElasticsearch() {
        return null;
    }
}
