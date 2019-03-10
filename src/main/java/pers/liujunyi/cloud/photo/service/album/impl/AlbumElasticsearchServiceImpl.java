package pers.liujunyi.cloud.photo.service.album.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.photo.domain.album.AlbumQueryDto;
import pers.liujunyi.cloud.photo.domain.album.AlbumVo;
import pers.liujunyi.cloud.photo.entity.album.Album;
import pers.liujunyi.cloud.photo.entity.album.AlbumPicture;
import pers.liujunyi.cloud.photo.repository.elasticsearch.album.AlbumElasticsearchRepository;
import pers.liujunyi.cloud.photo.repository.elasticsearch.album.AlbumPictureElasticsearchRepository;
import pers.liujunyi.cloud.photo.service.album.AlbumElasticsearchService;
import pers.liujunyi.cloud.photo.util.DictConstant;
import pers.liujunyi.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.restful.ResultUtil;
import pers.liujunyi.common.service.impl.BaseElasticsearchServiceImpl;
import pers.liujunyi.common.util.DictUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

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
    @Autowired
    private AlbumPictureElasticsearchRepository albumPictureElasticsearchRepository;
    @Autowired
    private DictUtil dictUtil;

    public AlbumElasticsearchServiceImpl(BaseElasticsearchRepository<Album, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }

    @Override
    public ResultInfo findPageGird(AlbumQueryDto query) {
        List<AlbumVo> datas = new CopyOnWriteArrayList<>();
        // 排序方式
        Sort sort =  new Sort(Sort.Direction.ASC, "albumPriority");
        //分页参数
        Pageable pageable = query.toPageable(sort);
        // 查询数据
        SearchQuery searchQuery = query.toSpecPageable(pageable);
        Page<Album> searchPageResults = this.albumElasticsearchRepository.search(searchQuery);
        List<Album> searchList = searchPageResults.getContent();
        searchList.stream().forEach(item ->{
            AlbumVo albumVo = new AlbumVo();
            albumVo.setAlbumLabel(item.getAlbumLabel());
            albumVo.setAlbumStyle(this.dictUtil.getDictName(DictConstant.IMAGE_STYLE, item.getAlbumStyle()));
            albumVo.setDescription(item.getAlbumDescription());
            albumVo.setStatus(item.getAlbumStatus());
            albumVo.setClassify(this.dictUtil.getDictName(DictConstant.ALBUM_CLASSIFY, item.getAlbumClassify()));
            albumVo.setMusicAddress(item.getAlbumMusicAddress());
            albumVo.setTitle(item.getAlbumTitle());
            albumVo.setId(item.getId());
            //获取相册图片信息
            List<AlbumPicture> albumPictures = this.albumPictureElasticsearchRepository.findByAlbumId(item.getId(), this.page);
            albumVo.setTotal(albumPictures.size());
            albumVo.setAlbumPictureDatas(albumPictures);
            List<AlbumPicture> filterList = albumPictures.stream().filter(u -> u.getCover().byteValue() == 0).limit(1).collect(Collectors.toList());
            albumVo.setCover(!filterList.isEmpty() ? filterList.get(0).getPictureLocation() : null);
            datas.add(albumVo);
        });
        Long totalElements =  searchPageResults.getTotalElements();
        ResultInfo result = ResultUtil.success(datas);
        result.setTotal(totalElements);
        return  result;
    }
}
