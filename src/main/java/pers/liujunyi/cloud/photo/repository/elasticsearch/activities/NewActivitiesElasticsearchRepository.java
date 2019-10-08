package pers.liujunyi.cloud.photo.repository.elasticsearch.activities;

import org.springframework.data.domain.Pageable;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.photo.entity.activities.NewActivities;

import java.util.List;

/***
 * 文件名称: NewActivitiesElasticsearchRepository.java
 * 文件描述: 最新活动 Elasticsearch Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年09月23日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface NewActivitiesElasticsearchRepository extends BaseElasticsearchRepository<NewActivities, Long> {

    /**
     * 根据是否到期 字段获取数据
     * @param maturity
     * @param date
     * @param pageable
     * @return
     */
    List<NewActivities> findByMaturityAndEndDateTimeBefore(Byte maturity, Long date, Pageable pageable);


}
