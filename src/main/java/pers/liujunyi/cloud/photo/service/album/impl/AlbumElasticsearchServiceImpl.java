package pers.liujunyi.cloud.photo.service.album.impl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseElasticsearchServiceImpl;
import pers.liujunyi.cloud.common.util.DictUtil;
import pers.liujunyi.cloud.common.util.DozerBeanMapperUtil;
import pers.liujunyi.cloud.common.util.SystemUtils;
import pers.liujunyi.cloud.photo.domain.album.AlbumQueryDto;
import pers.liujunyi.cloud.photo.domain.album.AlbumVo;
import pers.liujunyi.cloud.photo.entity.album.Album;
import pers.liujunyi.cloud.photo.entity.album.AlbumPicture;
import pers.liujunyi.cloud.photo.repository.elasticsearch.album.AlbumElasticsearchRepository;
import pers.liujunyi.cloud.photo.repository.elasticsearch.album.AlbumPictureElasticsearchRepository;
import pers.liujunyi.cloud.photo.service.album.AlbumElasticsearchService;
import pers.liujunyi.cloud.photo.service.user.StaffDetailsInfoElasticsearchService;
import pers.liujunyi.cloud.photo.util.Constant;
import pers.liujunyi.cloud.photo.util.DictConstant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
    private StaffDetailsInfoElasticsearchService staffDetailsInfoElasticsearchService;
    @Autowired
    private DictUtil dictUtil;

    public AlbumElasticsearchServiceImpl(BaseElasticsearchRepository<Album, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }

    @Override
    public ResultInfo findPageGird(AlbumQueryDto query) {
        List<AlbumVo> datas = new CopyOnWriteArrayList<>();
        // 排序方式 解决无数据时异常 No mapping found for [albumPriority] in order to sort on
        SortBuilder sortBuilder = SortBuilders.fieldSort("albumPriority").unmappedType("byte").order(SortOrder.ASC);
        // 如果使用这种排序方式 如果表中数据为空时,会报异常 No mapping found for [createTime] in order to sort on
        //Sort sort = Sort.by(Sort.Direction.ASC, "albumPriority");
        // 查询数据
        SearchQuery searchQuery = query.toSpecSortPageable(sortBuilder);
        Page<Album> searchPageResults = this.albumElasticsearchRepository.search(searchQuery);
        List<Album> searchList = searchPageResults.getContent();
        searchList.stream().forEach(item ->{
            AlbumVo albumVo = DozerBeanMapperUtil.copyProperties(item, AlbumVo.class);
            //获取相册图片信息
            List<AlbumPicture> albumPictures = this.albumPictureElasticsearchRepository.findByAlbumId(item.getId(), this.allPageable);
            albumVo.setTotal(albumPictures.size());
            albumVo.setAlbumPictureData(albumPictures);
            albumVo.setTitle(item.getAlbumTitle());
            albumVo.setStatus(item.getAlbumStatus());
            if (StringUtils.isBlank(item.getSurfacePlot())) {
                List<AlbumPicture> filterList = albumPictures.stream().filter(u -> u.getCover().byteValue() == 0).limit(1).collect(Collectors.toList());
                albumVo.setCover(!filterList.isEmpty() ? filterList.get(0).getPictureLocation() : null);
            } else {
                albumVo.setCover(item.getSurfacePlot());
            }
            datas.add(albumVo);
        });
        Long totalElements =  searchPageResults.getTotalElements();
        ResultInfo result = ResultUtil.success(datas);
        result.setTotal(totalElements);
        return  result;
    }

    @Override
    public ResultInfo details(Long id) {
        AlbumVo albumVo = this.detailsById(id);
        if (albumVo != null) {
            //获取相册图片信息
            List<AlbumPicture> albumPictures = this.albumPictureElasticsearchRepository.findByAlbumId(albumVo.getId(), this.allPageable);
            albumVo.setTotal(albumPictures.size());
            albumVo.setPictures(JSON.toJSONString(albumPictures));
        }
        return ResultUtil.success(albumVo);
    }

    @Override
    public AlbumVo detailsById(Long id) {
        AlbumVo albumVo = null;
        Album album = this.findById(id);
        if (album != null) {
            albumVo = DozerBeanMapperUtil.copyProperties(album, AlbumVo.class);
            albumVo.setSpotForPhotographyText(this.dictUtil.getDictName(DictConstant.IMAGE_SITE, album.getSpotForPhotography()));
            albumVo.setAlbumStyleText(this.dictUtil.getDictName(DictConstant.IMAGE_STYLE, album.getAlbumStyle()));
            albumVo.setAlbumClassificationText(this.dictUtil.getDictName(DictConstant.ALBUM_CLASSIFICATION, album.getAlbumClassification()));
            Set<Long> staffIdSet = new HashSet<>();
            List<Long> albumPhotographyAuthorList = new ArrayList<>();
            if (StringUtils.isNotBlank(album.getAlbumPhotographyAuthor())) {
                albumPhotographyAuthorList.addAll(SystemUtils.idToLong(album.getAlbumPhotographyAuthor()));
                staffIdSet.addAll(albumPhotographyAuthorList);
            }
            List<Long> albumAnaphasisAuthorList = new ArrayList<>();
            if (StringUtils.isNotBlank(album.getAlbumAnaphasisAuthor())) {
                albumAnaphasisAuthorList.addAll(SystemUtils.idToLong(album.getAlbumAnaphasisAuthor()));
                staffIdSet.addAll(albumAnaphasisAuthorList);
            }
            List<Long> albumDresserList = new ArrayList<>();
            if (StringUtils.isNotBlank(album.getAlbumDresser())) {
                albumDresserList.addAll(SystemUtils.idToLong(album.getAlbumDresser()));
                staffIdSet.addAll(albumDresserList);
            }
            if (staffIdSet.size() > 0) {
                List<Long> staffIds = new LinkedList<>(staffIdSet);
                Map<Long, String> staffNameMap = this.staffDetailsInfoElasticsearchService.getStaffNameMap(staffIds);
                if (!CollectionUtils.isEmpty(staffNameMap)) {
                    StringBuffer nameBuffer = new StringBuffer();
                    if (albumPhotographyAuthorList.size() > 0) {
                        albumPhotographyAuthorList.stream().forEach(item -> {
                            nameBuffer.append(staffNameMap.get(item)).append(" ");
                        });
                        albumVo.setAlbumPhotographyAuthorText(nameBuffer.toString());
                        nameBuffer.setLength(0);
                    }
                    if (albumAnaphasisAuthorList.size() > 0) {
                        albumAnaphasisAuthorList.stream().forEach(item -> {
                            nameBuffer.append(staffNameMap.get(item)).append(" ");
                        });
                        albumVo.setAlbumAnaphasisAuthorText(nameBuffer.toString());
                        nameBuffer.setLength(0);
                    }
                    if (albumDresserList.size() > 0) {
                        albumDresserList.stream().forEach(item -> {
                            nameBuffer.append(staffNameMap.get(item)).append(" ");
                        });
                        albumVo.setAlbumDresserText(nameBuffer.toString());
                        nameBuffer.setLength(0);
                    }
                }
            }
        }
        return albumVo;
    }

    @Override
    public Album findById(Long id) {
        Optional<Album> optional  = this.albumElasticsearchRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public ResultInfo findAlbumPictureByAlbumId(Long albumId) {
        List<AlbumPicture> albumPictures = this.albumPictureElasticsearchRepository.findByAlbumId(albumId, this.allPageable);
        return ResultUtil.success(albumPictures);
    }

    @Override
    public ResultInfo albumComboBox(String albumClassification, String albumClassify) {
        AlbumQueryDto query = new AlbumQueryDto();
        query.setPageNumber(1);
        query.setPageSize(20);
        query.setAlbumClassification(albumClassification);
        query.setAlbumStatus(Constant.ENABLE_STATUS);
        query.setAlbumClassify(albumClassify);
        // 排序方式 解决无数据时异常 No mapping found for [albumPriority] in order to sort on
        SortBuilder sortBuilder = SortBuilders.fieldSort("albumPriority").unmappedType("byte").order(SortOrder.ASC);
        // 查询数据
        SearchQuery searchQuery = query.toSpecSortPageable(sortBuilder);
        Page<Album> searchPageResults = this.albumElasticsearchRepository.search(searchQuery);
        List<Album> searchList = searchPageResults.getContent();
        List<Map<String, String>> resultData = new CopyOnWriteArrayList<>();
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("id", "");
        map.put("text", "--请选择--");
        resultData.add(map);
        if (!CollectionUtils.isEmpty(searchList)) {
            searchList.stream().forEach(item -> {
                Map<String, String> albumMap = new ConcurrentHashMap<>();
                albumMap.put("id", item.getId().toString());
                albumMap.put("text", item.getAlbumTitle());
                resultData.add(albumMap);
            });
        }
        return ResultUtil.success(resultData);
    }
}
