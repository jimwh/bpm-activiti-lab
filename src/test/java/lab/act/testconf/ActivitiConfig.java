package lab.act.testconf;

import org.activiti.engine.*;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;

@Configuration
@ContextConfiguration
public class ActivitiConfig {
    private static final Logger log = LoggerFactory.getLogger(ActivitiConfig.class);

    @Bean
    @Primary
    public DataSource dataSource() {
        log.info("datasource first...");
        return DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000")
                .username("sa")
                .password("")
                .build();
    }

    @Bean
    public TransactionAwareDataSourceProxy transactionAwareDataSource() {
        return new TransactionAwareDataSourceProxy(dataSource());
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public ProcessEngine processEngine() {
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        config.setDataSource(transactionAwareDataSource());
        config.setTransactionManager(transactionManager());
        config.setProcessEngineName("ACTIVITI-LAB-BPM-ENGINE");
        config.setDatabaseType("h2");
        config.setDatabaseSchemaUpdate("create-drop");
        config.setJobExecutorActivate(true);
        config.setAsyncExecutorActivate(true);
        config.setHistory("full");
        Set<Class<?>> set = new HashSet<Class<?>>();
        set.add(lab.act.service.LabMybatisMapper.class);
        config.setCustomMybatisMappers(set);
        return config.buildProcessEngine();
    }

    @Bean
    public RepositoryService repositoryService() {
        return processEngine().getRepositoryService();
    }

    @Bean
    public ManagementService managementService() {
        return processEngine().getManagementService();
    }

    @Bean
    public RuntimeService runtimeService() {
        return processEngine().getRuntimeService();
    }

    @Bean
    public TaskService getTaskService() {
        return processEngine().getTaskService();
    }

    @Bean
    public HistoryService getHistoryService() {
        return processEngine().getHistoryService();
    }

    @Bean
    public ManagementService getManagementService() {
        return processEngine().getManagementService();
    }

    @Bean
    public IdentityService getIdentityService() {
        return processEngine().getIdentityService();
    }

    @Bean
    public FormService getFormService() {
        return processEngine().getFormService();
    }

    @Bean
    public ActivitiRule activitiRule() {
        return new ActivitiRule(processEngine());
    }

}
