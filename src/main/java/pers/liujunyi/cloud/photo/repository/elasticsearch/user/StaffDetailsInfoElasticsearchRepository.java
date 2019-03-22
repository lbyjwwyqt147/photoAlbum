package pers.liujunyi.cloud.photo.repository.elasticsearch.user;

import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.photo.entity.StaffDetailsInfo;

/***
 * 文件名称: StaffDetailsInfoElasticsearchRepository.java
 * 文件描述: 职工档案信息 Elasticsearch Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface StaffDetailsInfoElasticsearchRepository extends BaseElasticsearchRepository<StaffDetailsInfo, Long> {

    /**
     * 根据 帐号id 获取员工详细数据
     * @param staffAccountsId
     * @param  staffStatus   0：正常  1：冻结  2：离职
     * @return
     */
    StaffDetailsInfo findFirstByStaffAccountsIdAndStaffStatus(Long staffAccountsId, Byte staffStatus);

    /**
     * 根据 帐号id 获取员工详细数据
     * @param staffAccountsId
     * @return
     */
    StaffDetailsInfo findFirstByStaffAccountsId(Long staffAccountsId);
}
