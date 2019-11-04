package pers.liujunyi.cloud.photo.datasource;

import com.alibaba.druid.pool.DruidDataSource;
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
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
    public LocalContainerEntityManagerFactoryBean masterEntityManager() throws IOException, SQLException  {
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

        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
        processEngineConfiguration.setConfigurators(processEngineConfigurators);
        configureProcessDefinitionResources(processDefinitionResourceFinder, processEngineConfiguration);
        processEngineConfiguration.setDataSource(activitiDataSource());
        processEngineConfiguration.setTransactionManager(transactionManager);
        if (springAsyncExecutor != null) {
            processEngineConfiguration.setAsyncExecutor(springAsyncExecutor);
        }
        processEngineConfiguration.setDeploymentName(activitiProperties.getDeploymentName());
        processEngineConfiguration.setDatabaseSchema(activitiProperties.getDatabaseSchema());
        processEngineConfiguration.setDatabaseSchemaUpdate(activitiProperties.getDatabaseSchemaUpdate());
        processEngineConfiguration.setDbHistoryUsed(activitiProperties.isDbHistoryUsed());
        processEngineConfiguration.setAsyncExecutorActivate(activitiProperties.isAsyncExecutorActivate());
        if (!activitiProperties.isAsyncExecutorActivate()) {
            ValidatorSet springBootStarterValidatorSet = new ValidatorSet("activiti-spring-boot-starter");
            springBootStarterValidatorSet.addValidator(new AsyncPropertyValidator());
            if (processEngineConfiguration.getProcessValidator() == null) {
                ProcessValidatorImpl processValidator = new ProcessValidatorImpl();
                processValidator.addValidatorSet(springBootStarterValidatorSet);
                processEngineConfiguration.setProcessValidator(processValidator);
            } else {
                processEngineConfiguration.getProcessValidator().getValidatorSets().add(springBootStarterValidatorSet);
            }
        }
        processEngineConfiguration.setMailServerHost(activitiProperties.getMailServerHost());
        processEngineConfiguration.setMailServerPort(activitiProperties.getMailServerPort());
        processEngineConfiguration.setMailServerUsername(activitiProperties.getMailServerUserName());
        processEngineConfiguration.setMailServerPassword(activitiProperties.getMailServerPassword());
        processEngineConfiguration.setMailServerDefaultFrom(activitiProperties.getMailServerDefaultFrom());
        processEngineConfiguration.setMailServerUseSSL(activitiProperties.isMailServerUseSsl());
        processEngineConfiguration.setMailServerUseTLS(activitiProperties.isMailServerUseTls());

        if (userGroupManager != null) {
            processEngineConfiguration.setUserGroupManager(userGroupManager);
        }

        processEngineConfiguration.setHistoryLevel(activitiProperties.getHistoryLevel());
        processEngineConfiguration.setCopyVariablesToLocalForTasks(activitiProperties.isCopyVariablesToLocalForTasks());
        processEngineConfiguration.setSerializePOJOsInVariablesToJson(activitiProperties.isSerializePOJOsInVariablesToJson());
        processEngineConfiguration.setJavaClassFieldForJackson(activitiProperties.getJavaClassFieldForJackson());

        if (activitiProperties.getCustomMybatisMappers() != null) {
            processEngineConfiguration.setCustomMybatisMappers(
                    getCustomMybatisMapperClasses(activitiProperties.getCustomMybatisMappers()));
        }

        if (activitiProperties.getCustomMybatisXMLMappers() != null) {
            processEngineConfiguration.setCustomMybatisXMLMappers(
                    new HashSet<>(activitiProperties.getCustomMybatisXMLMappers()));
        }

        if (activitiProperties.getCustomMybatisXMLMappers() != null) {
            processEngineConfiguration.setCustomMybatisXMLMappers(
                    new HashSet<>(activitiProperties.getCustomMybatisXMLMappers()));
        }

        if (activitiProperties.isUseStrongUuids()) {
            processEngineConfiguration.setIdGenerator(new StrongUuidGenerator());
        }

        if (activitiProperties.getDeploymentMode() != null) {
            processEngineConfiguration.setDeploymentMode(activitiProperties.getDeploymentMode());
        }

        processEngineConfiguration.setActivityBehaviorFactory(new DefaultActivityBehaviorFactory());

        if (processEngineConfigurationConfigurer != null) {
            processEngineConfigurationConfigurer.configure(processEngineConfiguration);
        }

        return processEngineConfiguration;
    }

    private void configureProcessDefinitionResources(
            ProcessDefinitionResourceFinder processDefinitionResourceFinder,
            SpringProcessEngineConfiguration conf) throws IOException {
        List<Resource> procDefResources = processDefinitionResourceFinder
                .discoverProcessDefinitionResources();
        if (!procDefResources.isEmpty()) {
            conf.setDeploymentResources(procDefResources.toArray(new Resource[0]));
        }
    }

}
