package pers.liujunyi.cloud.photo.service.album.impl;

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
import pers.liujunyi.cloud.photo.domain.album.RollingPictureQueryDto;
import pers.liujunyi.cloud.photo.domain.album.RollingPictureVo;
import pers.liujunyi.cloud.photo.entity.album.RollingPicture;
import pers.liujunyi.cloud.photo.repository.elasticsearch.album.RollingPictureElasticsearchRepository;
import pers.liujunyi.cloud.photo.service.album.RollingPictureElasticsearchService;
import pers.liujunyi.cloud.photo.util.DictConstant;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
        List<RollingPictureVo> datas = new CopyOnWriteArrayList<>();
        // 排序方式 解决无数据时异常 No mapping found for [priority] in order to sort on
        SortBuilder sortBuilder = SortBuilders.fieldSort("priority").unmappedType("byte").order(SortOrder.ASC);
        // 查询数据
        SearchQuery searchQuery = query.toSpecSortPageable(sortBuilder);
        Page<RollingPicture> searchPageResults = this.rollingPictureElasticsearchRepository.search(searchQuery);
        List<RollingPicture> searchList = searchPageResults.getContent();
        if (!CollectionUtils.isEmpty(searchList)) {
            // 获取数据字典值
            List<String> dictCodeList = new LinkedList<>();
            dictCodeList.add(DictConstant.IMAGE_PAGE);
            dictCodeList.add(DictConstant.PAGE_POSITION);
            Map<String, Map<String, String>> dictMap = this.dictUtil.getDictNameToMapList(dictCodeList);
            searchList.stream().forEach(item ->{
                RollingPictureVo rollingPictureVo = DozerBeanMapperUtil.copyProperties(item, RollingPictureVo.class);
                Map<String, String> pageMap = dictMap.get(DictConstant.IMAGE_PAGE);
                rollingPictureVo.setPageText( pageMap.get(item.getBusinessCode()));
                Map<String, String> positionMap = dictMap.get(DictConstant.PAGE_POSITION);
                rollingPictureVo.setPositionText(positionMap.get(item.getPosition()));
                datas.add(rollingPictureVo);
            });
        }
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
