package pers.liujunyi.cloud.photo.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;

/***
 * 文件名称: ActivitiConfig.java
 * 文件描述:  Activiti 据源配置
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年10月28日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Configuration
@DependsOn("transactionManager")
@EntityScan(basePackages = {"pers.liujunyi.cloud.activiti.entity", "org.activiti"})
@EnableJpaRepositories(basePackages = {"pers.liujunyi.cloud.activiti.repository"},
        entityManagerFactoryRef = "slaveEntityManager", transactionManagerRef = "transactionManager")
public class ActivitiDataSourceConfig extends AbstractProcessEngineAutoConfiguration {

    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;



    @Bean("activitiDruidDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.activiti")
    public DruidDataSource activitiDruidDataSource() {
        return new DruidDataSource();
    }

    /**
     * Activiti 数据源
     * @return
     */
    @Bean(name = "activitiDataSource", initMethod = "init", destroyMethod = "close")
    public DataSource activitiDataSource() throws SQLException {
        DruidDataSource dataSourceProperties = activitiDruidDataSource();
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(dataSourceProperties.getUrl());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXaDataSource.setUser(dataSourceProperties.getUsername());
        mysqlXaDataSource.setPassword(dataSourceProperties.getPassword());
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("activitiDataSource");
        xaDataSource.setBorrowConnectionTimeout(60);
        xaDataSource.setMaxIdleTime(60);
        xaDataSource.setMaxPoolSize(dataSourceProperties.getMaxActive());
        xaDataSource.setMinPoolSize(dataSourceProperties.getMinIdle());
        return xaDataSource;

    }

    /**
     *
     * @return
     * @throws Throwable
     */
    @Bean(name = "slaveEntityManager")
    @DependsOn("transactionManager")
    public LocalContainerEntityManagerFactoryBean masterEntityManager() throws Throwable {
        HashMap<String, Object> properties = new HashMap<>();
        // 标注transaction是JTA和JTA平台是AtomikosJtaPlatform.class.getName()
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJtaDataSource(activitiDataSource());
        entityManager.setJpaVendorAdapter(jpaVendorAdapter);
        entityManager.setPackagesToScan("pers.liujunyi.cloud.activiti.entity");
        entityManager.setPersistenceUnitName("slavePersistenceUnit");
        entityManager.setJpaPropertyMap(properties);
        return entityManager;
    }


}
