package pers.liujunyi.cloud.photo.service.user;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseService;
import pers.liujunyi.cloud.photo.domain.user.CustomerDetailInfoDto;
import pers.liujunyi.cloud.photo.entity.user.CustomerDetailInfo;

import java.util.List;

/***
 * 文件名称: CustomerDetailInfoService.java
 * 文件描述:  顾客档案信息 Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface CustomerDetailInfoService extends BaseService<CustomerDetailInfo, Long> {

    /**
     * 保存数据
     * @param record
     * @return
     */
    ResultInfo saveRecord(CustomerDetailInfoDto record);

    /**
     * 修改状态
     * @param status   0：正常  1：禁用
     * @param ids
     * @return
     */
    ResultInfo updateStatus(Byte status, List<Long> ids, List<Long> userIds, String putParams);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    ResultInfo deleteBatch(List<Long> ids, List<Long> userIds);

    /**
     * 单条删除
     * @param id
     * @param userId
     * @return
     */
    ResultInfo deleteSingle(Long id, Long userId);

    /**
     * 同步数据到es中
     * @return
     */
    ResultInfo syncDataToElasticsearch();

}
