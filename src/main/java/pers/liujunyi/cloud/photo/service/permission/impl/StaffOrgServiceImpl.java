package pers.liujunyi.cloud.photo.service.permission.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.impl.BaseServiceImpl;
import pers.liujunyi.cloud.photo.domain.album.AlbumDto;
import pers.liujunyi.cloud.photo.entity.permission.StaffOrg;
import pers.liujunyi.cloud.photo.repository.elasticsearch.permission.StaffOrgElasticsearchRepository;
import pers.liujunyi.cloud.photo.repository.jpa.permission.StaffOrgRepository;
import pers.liujunyi.cloud.photo.service.permission.StaffOrgService;

import java.util.List;

/***
 * 文件名称: StaffOrgServiceImpl.java
 * 文件描述: 职工关联组织机构 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class StaffOrgServiceImpl extends BaseServiceImpl<StaffOrg, Long> implements StaffOrgService {

    @Autowired
    private StaffOrgRepository staffOrgRepository;
    @Autowired
    private StaffOrgElasticsearchRepository staffOrgElasticsearchRepository;


    public StaffOrgServiceImpl(BaseRepository<StaffOrg, Long> baseRepository) {
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
