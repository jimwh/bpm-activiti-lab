package lab.act.config;

import org.activiti.engine.*;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import java.util.HashSet;
import java.util.Set;

@Import(DataSourceConfig.class)
@Configuration
public class ActivitiConfig {

    @Autowired
    @Qualifier("primaryTransactionAwareDataSource")
    public TransactionAwareDataSourceProxy transactionAwareDataSource;

    @Autowired
    @Qualifier("primaryTransactionManager")
    public DataSourceTransactionManager transactionManager;

    @Bean
    public ProcessEngine processEngine() {
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        config.setDataSource(transactionAwareDataSource);
        config.setTransactionManager(transactionManager);
        config.setProcessEngineName("RASCAL-ACTIVITI-BPM-ENGINE");
        config.setDatabaseType("oracle");
        config.setDatabaseSchemaUpdate("false");
        config.setJobExecutorActivate(true);
        config.setAsyncExecutorActivate(true);
        config.setHistory("full");
        Set<Class<?>> set = new HashSet<Class<?>>();
        set.add(lab.act.service.LabMybatisMapper.class);
        config.setCustomMybatisMappers(set);
        //Resource
        //config.setDeploymentResources();
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

}
