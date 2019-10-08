package pers.liujunyi.cloud.photo.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.util.DateTimeUtils;
import pers.liujunyi.cloud.photo.entity.activities.NewActivities;
import pers.liujunyi.cloud.photo.repository.elasticsearch.activities.NewActivitiesElasticsearchRepository;
import pers.liujunyi.cloud.photo.service.activities.NewActivitiesService;
import pers.liujunyi.cloud.photo.util.Constant;

import java.util.List;

/***
 * 文件名称: ActivitiesJobHandler.java
 * 文件描述:  最新活动任务调度
 * 公 司:
 * 内容摘要:
 * 其他说明: 任务Handler示例（Bean模式）
 *  开发步骤：
 *   1、继承"IJobHandler"：“com.xxl.job.core.handler.IJobHandler”；
 *   2、注册到Spring容器：添加“@Component”注解，被Spring容器扫描为Bean实例；
 *   3、注册到执行器工厂：添加“@JobHandler(value="自定义jobhandler名称")”注解，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 *   4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * 完成日期:2019年10月08日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@JobHandler(value="newActivitiesJobHandler")
@Component
public class ActivitiesJobHandler extends IJobHandler {

    @Autowired
    private NewActivitiesElasticsearchRepository newActivitiesElasticsearchRepository;
    @Autowired
    private NewActivitiesService newActivitiesService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("XXL-JOB, 执行检查最新活动是否过期.");
        String msg = this.checkExpire();
        XxlJobLogger.log(" >>>>>> " + msg);
        return SUCCESS;
    }


    /**
     * 检查最新活动是否过期
     * @return
     */
    private String checkExpire() {
        Pageable allPageable = PageRequest.of(0, 9999999);
        List<NewActivities> newActivitiesList = this.newActivitiesElasticsearchRepository.findByMaturityAndEndDateTimeBefore(Constant.ENABLE_STATUS, DateTimeUtils.getCurrentDate().getTime(), allPageable);
        if (!CollectionUtils.isEmpty(newActivitiesList)) {
            newActivitiesList.stream().forEach(item -> {
                newActivitiesService.updateMaturityStatus(Constant.DISABLE_STATUS, item.getId(), item.getDataVersion());
            });
            return "成功更新最新活动为过期状态，数量：" + newActivitiesList.size() + "条数据.";
        } else {
            return "最新活动已全部到期,没有可执行的最新活动.";
        }
    }
}
