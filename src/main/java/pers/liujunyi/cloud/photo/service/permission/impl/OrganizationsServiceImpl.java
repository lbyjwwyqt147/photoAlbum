package pers.liujunyi.cloud.photo.service.permission.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.photo.domain.permission.OrganizationsDto;
import pers.liujunyi.cloud.photo.entity.permission.Organizations;
import pers.liujunyi.cloud.photo.repository.elasticsearch.permission.OrganizationsElasticsearchRepository;
import pers.liujunyi.cloud.photo.repository.jpa.permission.OrganizationsRepository;
import pers.liujunyi.cloud.photo.service.permission.OrganizationsService;
import pers.liujunyi.common.repository.jpa.BaseRepository;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.restful.ResultUtil;
import pers.liujunyi.common.service.impl.BaseServiceImpl;
import pers.liujunyi.common.util.DozerBeanMapperUtil;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/***
 * 文件名称: OrganizationsServiceImpl.java
 * 文件描述: 组织机构 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class OrganizationsServiceImpl extends BaseServiceImpl<Organizations, Long> implements OrganizationsService {

    @Autowired
    private OrganizationsRepository organizationsRepository;
    @Autowired
    private OrganizationsElasticsearchRepository organizationsElasticsearchRepository;


    public OrganizationsServiceImpl(BaseRepository<Organizations, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(OrganizationsDto record) {
        Organizations organizations = DozerBeanMapperUtil.copyProperties(record, Organizations.class);
        if (record.getSeq() == null) {
            organizations.setSeq(10);
        }
        Organizations queryOrg = getOrganizations(record.getParentId());
        if (queryOrg != null) {
            StringBuffer fullName = new StringBuffer();
            fullName.append(queryOrg.getOrgName());
            fullName.append("-").append(record.getOrgName()).append("-");
            organizations.setFullName(fullName.toString());
            organizations.setOrgLevel((byte)(queryOrg.getOrgLevel() + 1));
        } else {
            organizations.setOrgLevel((byte) 1);
        }
        Organizations saveObject = this.organizationsRepository.save(organizations);
        if (saveObject == null || saveObject.getId() == null) {
            return ResultUtil.fail();
        }
        this.organizationsElasticsearchRepository.save(saveObject);
        return ResultUtil.success();
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids) {
        int count = this.organizationsRepository.setOrgStatusByIds(status, new Date(), ids);
        if (count > 0) {
            this.organizationsElasticsearchRepository.setOrgStatusByIds(status, new Date(), ids);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo batchDeletes(List<Long> ids) {
        long count = this.organizationsRepository.deleteByIdIn(ids);
        if (count > 0) {
            this.organizationsElasticsearchRepository.deleteByIdIn(ids);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo syncDataToElasticsearch() {
        Sort sort =  new Sort(Sort.Direction.ASC, "id");
        List<Organizations> list = this.organizationsRepository.findAll(sort);
        if (!CollectionUtils.isEmpty(list)) {
            this.organizationsElasticsearchRepository.deleteAll();
            // 限制条数
            int pointsDataLimit = 1000;
            int size = list.size();
            //判断是否有必要分批
            if(pointsDataLimit < size){
                //分批数
                int part = size/pointsDataLimit;
                for (int i = 0; i < part; i++) {
                    //1000条
                    List<Organizations> partList = new LinkedList<>(list.subList(0, pointsDataLimit));
                    //剔除
                    list.subList(0, pointsDataLimit).clear();
                    this.organizationsElasticsearchRepository.saveAll(partList);
                }
                //表示最后剩下的数据
                if (!CollectionUtils.isEmpty(list)) {
                    this.organizationsElasticsearchRepository.saveAll(list);
                }
            } else {
                this.organizationsElasticsearchRepository.saveAll(list);
            }
        }
        return ResultUtil.success();
    }

    /**
     * 根据id 获取数据
     * @param id
     * @return
     */
    private Organizations getOrganizations(Long id) {
        Optional<Organizations> organizations = this.organizationsElasticsearchRepository.findById(id);
        if (organizations.isPresent()) {
            return organizations.get();
        }
        return null;
    }
}
