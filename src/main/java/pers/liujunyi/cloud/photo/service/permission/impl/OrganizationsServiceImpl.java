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
import pers.liujunyi.cloud.photo.util.Constant;
import pers.liujunyi.common.repository.jpa.BaseRepository;
import pers.liujunyi.common.restful.ResultInfo;
import pers.liujunyi.common.restful.ResultUtil;
import pers.liujunyi.common.service.impl.BaseServiceImpl;
import pers.liujunyi.common.util.DozerBeanMapperUtil;
import pers.liujunyi.common.util.UserUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    @Autowired
    private UserUtils userUtils;


    public OrganizationsServiceImpl(BaseRepository<Organizations, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(OrganizationsDto record) {
        if (this.checkOrgNumberRepetition(record.getOrgNumber(), record.getId())) {
            return ResultUtil.params("机构代码重复,请重新输入！");
        }
        Organizations organizations = DozerBeanMapperUtil.copyProperties(record, Organizations.class);
        if (record.getSeq() == null) {
            organizations.setSeq(10);
        }
        if (record.getOrgStatus() == null) {
            organizations.setOrgStatus(Constant.ENABLE_STATUS);
        }
        if (record.getId() != null) {
            organizations.setUpdateTime(new Date());
            organizations.setUpdateUserId(this.userUtils.getPresentLoginUserId());
        }
        if (record.getParentId().longValue() > 0) {
            Organizations parent = this.getOrganizations(record.getParentId());
            organizations.setFullParent(parent.getFullParent() + ":"  + parent.getId());
            organizations.setOrgLevel((byte)(parent.getOrgLevel() + 1));
        } else {
            organizations.setFullParent("0");
            organizations.setOrgLevel((byte) 1);
        }
        organizations.setFullName(record.getOrgName());
        Organizations saveObject = this.organizationsRepository.save(organizations);
        if (saveObject == null || saveObject.getId() == null) {
            return ResultUtil.fail();
        }
        this.organizationsElasticsearchRepository.save(saveObject);
        return ResultUtil.success(saveObject.getId());
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids) {
        int a = 0/0;
        int count = this.organizationsRepository.setOrgStatusByIds(status, new Date(), ids);
        if (count > 0) {
            Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            Map<String, Object> docDataMap = new HashMap<>();
            docDataMap.put("orgStatus", status);
            docDataMap.put("updateTime", System.currentTimeMillis());
            ids.stream().forEach(item -> {
                sourceMap.put(String.valueOf(item), docDataMap);
            });
            // 更新 Elasticsearch 中的数据
            super.updateBatchElasticsearchData(sourceMap);
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

    /**
     * 检测机构代码是否重复
     * @param orgNumber
     * @return 重复返回 true   不重复返回  false
     */
    private Boolean checkOrgNumberRepetition(String orgNumber, Long id) {
        if (id == null){
            return this.checkOrgNumberData(orgNumber);
        } else {
            Organizations organizations = this.getOrganizations(id);
            if (organizations != null && !organizations.getOrgNumber().equals(orgNumber)) {
                return this.checkOrgNumberData(orgNumber);
            }
        }
        return false;
    }

    /**
     * 检查中是否存在dictCode 数据
     * @param orgNumber
     * @return
     */
    private Boolean checkOrgNumberData (String orgNumber) {
        Organizations organizations = this.organizationsElasticsearchRepository.findFirstByOrgNumber(orgNumber);
        if (organizations != null) {
            return true;
        }
        return false;
    }



}
