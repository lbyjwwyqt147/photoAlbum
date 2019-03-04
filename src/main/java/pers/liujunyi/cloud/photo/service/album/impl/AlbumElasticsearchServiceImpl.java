package pers.liujunyi.cloud.photo.service.album.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.photo.domain.album.AlbumQueryDto;
import pers.liujunyi.cloud.photo.entity.Album;
import pers.liujunyi.cloud.photo.repository.elasticsearch.album.AlbumElasticsearchRepository;
import pers.liujunyi.cloud.photo.service.album.AlbumElasticsearchService;
import pers.liujunyi.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.service.impl.BaseElasticsearchServiceImpl;

/***
 * 文件名称: AlbumElasticsearchServiceImpl.java
 * 文件描述: 相册管理 Elasticsearch Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月04日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class AlbumElasticsearchServiceImpl extends BaseElasticsearchServiceImpl<Album, Long> implements AlbumElasticsearchService {

    @Autowired
    private AlbumElasticsearchRepository albumElasticsearchRepository;

    public AlbumElasticsearchServiceImpl(BaseElasticsearchRepository<Album, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }

    @Override
    public ResultInfo findPageGird(AlbumQueryDto query) {
        return null;
    }
}
