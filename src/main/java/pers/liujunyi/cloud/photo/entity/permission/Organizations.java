package pers.liujunyi.cloud.photo.entity.permission;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import pers.liujunyi.common.entity.BaseEntity;

import javax.persistence.Entity;

/***
 * 文件名称: Organizations.java
 * 文件描述: 组织机构
 * 公 司:
 * 内容摘要: elasticsearch6.0.0移除了一个索引允许映射多个类型，虽然还支持同索引多类型查询，但是Elasticsearch 7.0.0的版本将完全放弃type 。https://www.cnblogs.com/liugx/p/8470369.html
 *
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Data
@Entity
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(indexName = "photo_manage_organizations", type = "organizations", shards = 1, replicas = 0)
@DynamicInsert
@DynamicUpdate
public class Organizations extends BaseEntity {
    private static final long serialVersionUID = -2904937024962048531L;
    /** 机构编号 */
    private String orgNumber;

    /** 机构名称 */
    private String orgName;

    /** 机构级别 */
    private Byte orgLevel;

    /** 父级主键id */
    private Long parentId;

    /** 排序号 */
    private Integer seq;

    /** 完整的机构名称 */
    @Field(index = false)
    private String fullName;

    /** 描述说明 */
    @Field(index = false)
    private String description;

    /** 状态：0：正常  1：禁用 */
    private Byte orgStatus;

}