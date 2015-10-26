package lab.act.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class DevHireService implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(DevHireService.class);

    public void storeResume() {
        log.info("store resume ...");
    }

    public void sendRejectionEmail() {
        log.info("send rejection email ...");
    }

    public void sendWelcomeEmail() {
        log.info("send Welcome Email ...");
    }

}
