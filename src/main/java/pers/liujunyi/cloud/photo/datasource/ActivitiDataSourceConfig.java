package pers.liujunyi.cloud.photo.datasource;

import com.mysql.cj.jdbc.MysqlXADataSource;
import org.activiti.api.runtime.shared.identity.UserGroupManager;
import org.activiti.engine.cfg.ProcessEngineConfigurator;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.activiti.engine.impl.persistence.StrongUuidGenerator;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.activiti.spring.boot.ActivitiProperties;
import org.activiti.spring.boot.DefaultActivityBehaviorFactoryMappingConfigurer;
import org.activiti.spring.boot.ProcessDefinitionResourceFinder;
import org.activiti.spring.boot.process.validation.AsyncPropertyValidator;
import org.activiti.validation.ProcessValidatorImpl;
import org.activiti.validation.validator.ValidatorSet;
import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import pers.liujunyi.cloud.common.configuration.DruidDataSourceProperties;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/***
 * 文件名称: ActivitiConfig.java
 * 文件描述:  Activiti 据源配置
 * 公 司:
 * 内容摘要:
 * 其他说明:
 *  @EnableTransactionManagement  开启注解事物   @EnableJpaRepositories 开启JPA存储库扫描
 *  EnableJpaRepositories 和 EnableElasticsearchRepositories 不能在同一包下面
 *  @EntityScan 配置 实体类所在的路径  解决： Not a managed type:
 * 完成日期:2019年10月28日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Configuration
@DependsOn("transactionManager")
@EntityScan(basePackages = {"pers.liujunyi.cloud.activiti.entity"})
@EnableJpaRepositories(basePackages = {"pers.liujunyi.cloud.activiti.repository.jpa"},
        entityManagerFactoryRef = "activitiEntityManager", transactionManagerRef = "transactionManager")
@EnableElasticsearchRepositories(basePackages = {"pers.liujunyi.cloud.activiti.repository.elasticsearch"})
public class ActivitiDataSourceConfig extends AbstractProcessEngineAutoConfiguration {

    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private HibernateProperties hibernateProperties;

    @Value("${spring.jpa.database-platform}")
    private String databasePlatform;

    @Autowired
    private DruidDataSourceProperties druidDataSourceProperties;

    @Bean("activitiDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.druid.activiti")
    public DataSourceProperties activitiDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * Activiti 数据源
     * @return
     */
    @Bean(name = "activitiDataSource", initMethod = "init", destroyMethod = "close")
    public DataSource activitiDataSource() throws SQLException {
        DataSourceProperties dataSourceProperties = activitiDataSourceProperties();
        // 使用mysql的分布式驱动
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(dataSourceProperties.getUrl());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXaDataSource.setUser(dataSourceProperties.getUsername());
        mysqlXaDataSource.setPassword(dataSourceProperties.getPassword());

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("activitiDataSource");
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
     *
     * @return
     * @throws Throwable
     */
    @Bean(name = "activitiEntityManager")
    @DependsOn("transactionManager")
    public LocalContainerEntityManagerFactoryBean masterEntityManager() throws IOException, SQLException  {
        Map<String, String> properties = this.jpaProperties.getProperties();
        // 标注transaction是JTA和JTA平台是AtomikosJtaPlatform.class.getName()
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJtaDataSource(activitiDataSource());
        entityManager.setJpaVendorAdapter(this.jpaVendorAdapter);
        //设置实体类所在位置
        entityManager.setPackagesToScan("pers.liujunyi.cloud.activiti.entity");
        entityManager.setPersistenceUnitName("activitiPersistenceUnit");
        entityManager.setJpaPropertyMap(this.hibernateProperties.determineHibernateProperties(properties, new
                HibernateSettings()));
        return entityManager;
    }


    /**
     *  注入数据源和事务管理器
     *  这段代码很重要,如果没有这段代码无法初始化 activitiDataSource
     * @param transactionManager
     * @param springAsyncExecutor
     * @param activitiProperties
     * @param processDefinitionResourceFinder
     * @param processEngineConfigurationConfigurer
     * @param processEngineConfigurators
     * @param userGroupManager
     * @return
     * @throws IOException
     */
    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(
            PlatformTransactionManager transactionManager,
            SpringAsyncExecutor springAsyncExecutor,
            ActivitiProperties activitiProperties,
            ProcessDefinitionResourceFinder processDefinitionResourceFinder,
            @Autowired(required = false) DefaultActivityBehaviorFactoryMappingConfigurer processEngineConfigurationConfigurer,
            @Autowired(required = false) List<ProcessEngineConfigurator> processEngineConfigurators,
            UserGroupManager userGroupManager) throws IOException, SQLException {

        SpringProcessEngineConfiguration conf = new SpringProcessEngineConfiguration();
        conf.setConfigurators(processEngineConfigurators);
        configureProcessDefinitionResources(processDefinitionResourceFinder,
                conf);
        conf.setDataSource(activitiDataSource());
        conf.setTransactionManager(transactionManager);

        if (springAsyncExecutor != null) {
            conf.setAsyncExecutor(springAsyncExecutor);
        }
        conf.setDeploymentName(activitiProperties.getDeploymentName());
        conf.setDatabaseSchema(activitiProperties.getDatabaseSchema());
        conf.setDatabaseSchemaUpdate(activitiProperties.getDatabaseSchemaUpdate());
        conf.setDbHistoryUsed(activitiProperties.isDbHistoryUsed());
        conf.setAsyncExecutorActivate(activitiProperties.isAsyncExecutorActivate());
        if (!activitiProperties.isAsyncExecutorActivate()) {
            ValidatorSet springBootStarterValidatorSet = new ValidatorSet("activiti-spring-boot-starter");
            springBootStarterValidatorSet.addValidator(new AsyncPropertyValidator());
            if (conf.getProcessValidator() == null) {
                ProcessValidatorImpl processValidator = new ProcessValidatorImpl();
                processValidator.addValidatorSet(springBootStarterValidatorSet);
                conf.setProcessValidator(processValidator);
            } else {
                conf.getProcessValidator().getValidatorSets().add(springBootStarterValidatorSet);
            }
        }
        conf.setMailServerHost(activitiProperties.getMailServerHost());
        conf.setMailServerPort(activitiProperties.getMailServerPort());
        conf.setMailServerUsername(activitiProperties.getMailServerUserName());
        conf.setMailServerPassword(activitiProperties.getMailServerPassword());
        conf.setMailServerDefaultFrom(activitiProperties.getMailServerDefaultFrom());
        conf.setMailServerUseSSL(activitiProperties.isMailServerUseSsl());
        conf.setMailServerUseTLS(activitiProperties.isMailServerUseTls());

        if (userGroupManager != null) {
            conf.setUserGroupManager(userGroupManager);
        }

        conf.setHistoryLevel(activitiProperties.getHistoryLevel());
        conf.setCopyVariablesToLocalForTasks(activitiProperties.isCopyVariablesToLocalForTasks());
        conf.setSerializePOJOsInVariablesToJson(activitiProperties.isSerializePOJOsInVariablesToJson());
        conf.setJavaClassFieldForJackson(activitiProperties.getJavaClassFieldForJackson());

        if (activitiProperties.getCustomMybatisMappers() != null) {
            conf.setCustomMybatisMappers(getCustomMybatisMapperClasses(activitiProperties.getCustomMybatisMappers()));
        }

        if (activitiProperties.getCustomMybatisXMLMappers() != null) {
            conf.setCustomMybatisXMLMappers(new HashSet<>(activitiProperties.getCustomMybatisXMLMappers()));
        }

        if (activitiProperties.getCustomMybatisXMLMappers() != null) {
            conf.setCustomMybatisXMLMappers(new HashSet<>(activitiProperties.getCustomMybatisXMLMappers()));
        }

        if (activitiProperties.isUseStrongUuids()) {
            conf.setIdGenerator(new StrongUuidGenerator());
        }

        if (activitiProperties.getDeploymentMode() != null) {
            conf.setDeploymentMode(activitiProperties.getDeploymentMode());
        }

        conf.setActivityBehaviorFactory(new DefaultActivityBehaviorFactory());

        if (processEngineConfigurationConfigurer != null) {
            processEngineConfigurationConfigurer.configure(conf);
        }

        return conf;
    }

    private void configureProcessDefinitionResources(ProcessDefinitionResourceFinder processDefinitionResourceFinder,
                                                     SpringProcessEngineConfiguration conf) throws IOException {
        List<Resource> procDefResources = processDefinitionResourceFinder.discoverProcessDefinitionResources();
        if (!procDefResources.isEmpty()) {
            conf.setDeploymentResources(procDefResources.toArray(new Resource[0]));
        }
    }


}
