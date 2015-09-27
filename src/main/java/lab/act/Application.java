package lab.act;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@EnableAutoConfiguration
@ImportResource( { "application-context.xml" } )
@ComponentScan("lab.act")
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        log.info("start...");

        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        //MiddleMan mm = ctx.getBean(MiddleMan.class);
        //mm.suspendProtocol("4901");
        SpringApplication.exit(ctx);

        log.info("done...");

    }
}