package lab.act.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class FooBar {

    @Autowired
    private Environment env;

    public String getDir() {
        return env.getProperty("downloadDirectory");
    }
}