package pers.liujunyi.cloud.photo.service.album.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.photo.domain.album.AlbumDto;
import pers.liujunyi.cloud.photo.entity.Album;
import pers.liujunyi.cloud.photo.repository.jpa.album.AlbumRepository;
import pers.liujunyi.cloud.photo.service.album.AlbumService;
import pers.liujunyi.common.repository.jpa.BaseRepository;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.service.impl.BaseServiceImpl;

import java.util.LinkedList;
import java.util.List;

@Service
public class AlbumServiceImpl extends BaseServiceImpl<Album, Long> implements AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    public AlbumServiceImpl(BaseRepository<Album, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(AlbumDto record) {
        List<Album> list = new LinkedList<>();
        JSONArray jsonArray = JSON.parseArray(record.getPictures());
        for (Object json : jsonArray) {

        }
        return null;
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
