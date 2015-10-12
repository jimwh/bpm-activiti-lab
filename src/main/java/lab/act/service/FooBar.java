package lab.act.service;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class FooBar {

    @Resource
    private Environment env;

    public String getDir() {
        return env.getProperty("downloadDirectory");
    }
}