package pers.liujunyi.cloud.photo.datasource;

import org.springframework.context.annotation.Configuration;

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
   /* @Bean(name = "dataSource")
    @Qualifier(value = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }*/


}
