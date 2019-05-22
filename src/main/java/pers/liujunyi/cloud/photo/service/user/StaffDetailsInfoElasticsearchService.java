package pers.liujunyi.cloud.photo.service.user;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseElasticsearchService;
import pers.liujunyi.cloud.photo.domain.user.StaffDetailsInfoQueryDto;
import pers.liujunyi.cloud.photo.domain.user.StaffDetailsInfoVo;
import pers.liujunyi.cloud.photo.entity.user.StaffDetailsInfo;

/***
 * 文件名称: StaffDetailsInfoElasticsearchService.java
 * 文件描述: 职工档案信息 Elasticsearch Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface StaffDetailsInfoElasticsearchService extends BaseElasticsearchService<StaffDetailsInfo, Long> {

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
    StaffDetailsInfo findStaffDetails(Long staffAccountsId);

    /**
     * 分页列表
     * @param query
     * @return
     */
    ResultInfo findPageGird(StaffDetailsInfoQueryDto query);

    /**
     * 根据 帐号id 获取员工详细数据
     * @param staffAccountsId
     * @return
     */
    ResultInfo findByStaffAccountsId(Long staffAccountsId);

    /**
     * 根据 帐号id 获取员工详细数据
     * @param staffAccountsId
     * @return
     */
    StaffDetailsInfoVo getStaffDetailsByStaffAccountsId(Long staffAccountsId);

    /**
     * 根据 帐号id 获取员工详细数据
     * @param id
     * @return
     */
    ResultInfo findById(Long id);

    /**
     * 根据 id 获取员工详细数据
     * @param id
     * @return
     */
    StaffDetailsInfoVo getStaffDetailsById(Long id);
}
