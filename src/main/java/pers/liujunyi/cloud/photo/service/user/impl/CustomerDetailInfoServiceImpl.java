package pers.liujunyi.cloud.photo.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.impl.BaseServiceImpl;
import pers.liujunyi.cloud.photo.domain.album.AlbumDto;
import pers.liujunyi.cloud.photo.entity.user.CustomerDetailInfo;
import pers.liujunyi.cloud.photo.repository.elasticsearch.user.CustomerDetailInfoElasticsearchRepository;
import pers.liujunyi.cloud.photo.repository.jpa.user.CustomerDetailInfoRepository;
import pers.liujunyi.cloud.photo.service.user.CustomerDetailInfoService;

import java.util.List;

/***
 * 文件名称: CustomerDetailInfoServiceImpl.java
 * 文件描述: 顾客档案信息 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class CustomerDetailInfoServiceImpl extends BaseServiceImpl<CustomerDetailInfo, Long> implements CustomerDetailInfoService {

    @Autowired
    private CustomerDetailInfoRepository customerDetailInfoRepository;
    @Autowired
    private CustomerDetailInfoElasticsearchRepository customerDetailInfoElasticsearchRepository;


    public CustomerDetailInfoServiceImpl(BaseRepository<CustomerDetailInfo, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(AlbumDto record) {
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
