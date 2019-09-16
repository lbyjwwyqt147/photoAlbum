package pers.liujunyi.cloud.photo.service.album.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseElasticsearchServiceImpl;
import pers.liujunyi.cloud.common.util.DictUtil;
import pers.liujunyi.cloud.photo.domain.album.AlbumVo;
import pers.liujunyi.cloud.photo.domain.album.RollingPictureQueryDto;
import pers.liujunyi.cloud.photo.entity.album.RollingPicture;
import pers.liujunyi.cloud.photo.repository.elasticsearch.album.RollingPictureElasticsearchRepository;
import pers.liujunyi.cloud.photo.service.album.RollingPictureElasticsearchService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/***
 * 文件名称: RollingPictureElasticsearchServiceImpl.java
 * 文件描述: 轮播图 Elasticsearch Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月04日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class RollingPictureElasticsearchServiceImpl extends BaseElasticsearchServiceImpl<RollingPicture, Long> implements RollingPictureElasticsearchService {

    @Autowired
    private RollingPictureElasticsearchRepository rollingPictureElasticsearchRepository;

    @Autowired
    private DictUtil dictUtil;

    public RollingPictureElasticsearchServiceImpl(BaseElasticsearchRepository<RollingPicture, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }

    @Override
    public ResultInfo findPageGird(RollingPictureQueryDto query) {
        List<AlbumVo> datas = new CopyOnWriteArrayList<>();
        //分页参数
        Pageable pageable = query.toPageable();
        // 查询数据
        SearchQuery searchQuery = query.toSpecPageable(pageable);
        Page<RollingPicture> searchPageResults = this.rollingPictureElasticsearchRepository.search(searchQuery);
        List<RollingPicture> searchList = searchPageResults.getContent();
        searchList.stream().forEach(item ->{

        });
        Long totalElements =  searchPageResults.getTotalElements();
        ResultInfo result = ResultUtil.success(datas);
        result.setTotal(totalElements);
        return  result;
    }

    @Override
    public ResultInfo details(Long id) {

        return ResultUtil.success();
    }

    @Override
    public ResultInfo findByBusinessCodeAndPositionOrderByPriority(String businessCode, String position) {
        return ResultUtil.success(this.rollingPictureElasticsearchRepository.findByBusinessCodeAndPositionOrderByPriority(businessCode, position));
    }


}
