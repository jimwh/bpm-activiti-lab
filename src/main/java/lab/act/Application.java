package lab.act;

import lab.act.config.ActivitiConfig;
import lab.act.config.DataSourceConfig;

import org.activiti.engine.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("lab.act")
@SpringApplicationConfiguration(classes = {DataSourceConfig.class, ActivitiConfig.class})
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        log.info("start...");

        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        deployment(ctx);

        SpringApplication.exit(ctx);

        log.info("done...");
    }

    static void deployment(ApplicationContext ctx) {
        RepositoryService repositoryService =
                (RepositoryService) ctx.getBean("repositoryService");
        String deploymentId = repositoryService
                .createDeployment()
                .addClasspathResource("dualSignals.bpmn20.xml")
                .deploy()
                .getId();
        log.info("deploymentId={}", deploymentId);
        repositoryService.deleteDeployment(deploymentId);
    }
}