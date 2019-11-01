package pers.liujunyi.cloud.photo.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
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

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;


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

    @Bean("masterDruidDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.master")
    public DruidDataSource masterDruidDataSource() {
        return new DruidDataSource();
    }

    /**
     * 默认数据库
     * @return
     */
    @Primary
    @Bean(name = "dataSource", initMethod = "init", destroyMethod = "close")
    public DataSource masterDataSource() throws SQLException {
        DruidDataSource dataSourceProperties = masterDruidDataSource();
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(dataSourceProperties.getUrl());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXaDataSource.setUser(dataSourceProperties.getUsername());
        mysqlXaDataSource.setPassword(dataSourceProperties.getPassword());
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("masterDataSource");
        xaDataSource.setBorrowConnectionTimeout(60);
        xaDataSource.setMaxIdleTime(60);
        xaDataSource.setMaxPoolSize(dataSourceProperties.getMaxActive());
        xaDataSource.setMinPoolSize(dataSourceProperties.getMinIdle());
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
        HashMap<String, Object> properties = new HashMap<String, Object>();
        // 标注transaction是JTA和JTA平台是AtomikosJtaPlatform.class.getName()
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJtaDataSource(masterDataSource());
        entityManager.setJpaVendorAdapter(jpaVendorAdapter);
        entityManager.setPackagesToScan("pers.liujunyi.cloud.photo.entity", "pers.liujunyi.cloud.security.entity");
        entityManager.setPersistenceUnitName("masterPersistenceUnit");
        entityManager.setJpaPropertyMap(properties);
        return entityManager;
    }

}
