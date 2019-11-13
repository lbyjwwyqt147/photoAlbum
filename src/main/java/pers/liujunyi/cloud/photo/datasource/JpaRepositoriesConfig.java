package pers.liujunyi.cloud.photo.datasource;

import com.mysql.cj.jdbc.MysqlXADataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import pers.liujunyi.cloud.common.configuration.DruidDataSourceProperties;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;


/***
 * 文件名称: JpaRepositoriesConfig.java
 * 文件描述: 默认数据源 主库持久化配置
 * 公 司:
 * 内容摘要:
 * 其他说明:  @EnableTransactionManagement  开启注解事物   @EnableJpaRepositories 开启JPA存储库扫描
 *            EnableJpaRepositories 和 EnableElasticsearchRepositories 不能在同一包下面
 *            @EntityScan 配置 实体类所在的路径  解决： Not a managed type:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Configuration
@DependsOn("transactionManager")
@EntityScan(basePackages = {"pers.liujunyi.cloud.photo.entity", "pers.liujunyi.cloud.security.entity"})
@EnableJpaRepositories(basePackages = {"pers.liujunyi.cloud.photo.repository.jpa", "pers.liujunyi.cloud.security.repository.jpa"},
        entityManagerFactoryRef = "masterEntityManager", transactionManagerRef = "transactionManager")
@EnableElasticsearchRepositories(basePackages = {"pers.liujunyi.cloud.photo.repository.elasticsearch", "pers.liujunyi.cloud.security.repository.elasticsearch"})
public class JpaRepositoriesConfig {

    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private HibernateProperties hibernateProperties;

    @Autowired
    private DruidDataSourceProperties druidDataSourceProperties;


    @Bean("masterDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.druid.master")
    public DataSourceProperties masterDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * 默认数据库
     * @return
     */
    @Primary
    @Bean(name = "dataSource", initMethod = "init", destroyMethod = "close")
    public DataSource masterDataSource() throws SQLException {
        DataSourceProperties dataSourceProperties = masterDataSourceProperties();
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(dataSourceProperties.getUrl());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXaDataSource.setUser(dataSourceProperties.getUsername());
        mysqlXaDataSource.setPassword(dataSourceProperties.getPassword());
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("masterDataSource");
        xaDataSource.setPoolSize(druidDataSourceProperties.getInitialSize());
        xaDataSource.setMinPoolSize(druidDataSourceProperties.getMinIdle());
        xaDataSource.setMaxPoolSize(druidDataSourceProperties.getMaxActive());
        xaDataSource.setMaxIdleTime(druidDataSourceProperties.getMinIdle());
        xaDataSource.setMaxLifetime(druidDataSourceProperties.getMinEvictableIdleTimeMillis().intValue());
        xaDataSource.setConcurrentConnectionValidation(druidDataSourceProperties.getTestWhileIdle());
        xaDataSource.setTestQuery(druidDataSourceProperties.getValidationQuery());
        xaDataSource.setReapTimeout(druidDataSourceProperties.getMinEvictableIdleTimeMillis().intValue());
        return xaDataSource;

    }

    /**
     * 默认数据源事物配置
     * @return
     * @throws Throwable
     */
    @Primary
    @Bean(name = "masterEntityManager")
    @DependsOn("transactionManager")
    public LocalContainerEntityManagerFactoryBean masterEntityManager() throws Throwable {
        Map<String, String> properties =  this.jpaProperties.getProperties();
        // 标注transaction是JTA和JTA平台是AtomikosJtaPlatform.class.getName()
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJtaDataSource(masterDataSource());
        entityManager.setJpaVendorAdapter(jpaVendorAdapter);
        //设置实体类所在位置
        entityManager.setPackagesToScan("pers.liujunyi.cloud.photo.entity", "pers.liujunyi.cloud.security.entity");
        entityManager.setPersistenceUnitName("masterPersistenceUnit");
        // 设置jpa属性  使用 hibernateProperties.determineHibernateProperties(properties, new HibernateSettings()) 解决不按照配置的JPA 命令策略生成表的问题
        entityManager.setJpaPropertyMap(hibernateProperties.determineHibernateProperties(properties, new
                HibernateSettings()));
        return entityManager;
    }

}
