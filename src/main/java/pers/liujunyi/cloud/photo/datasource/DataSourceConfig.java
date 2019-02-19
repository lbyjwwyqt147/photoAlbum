package pers.liujunyi.cloud.photo.datasource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/***
 * 文件名称: DataSourceConfig.java
 * 文件描述: 配置数据源
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Configuration
public class DataSourceConfig {

    /**
     * 　声明数据源 Bean实例
     *
     * @return
     */
    @Bean(name = "dataSource")
    @Qualifier(value = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource masterDataSource() {
        return DruidDataSourceBuilder.create().build();
    }


}
