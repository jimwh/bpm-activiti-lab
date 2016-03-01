package lab.act;

import lab.act.testconf.ActivitiConfig;
import lab.act.testconf.UnitTestAccessor;
import org.activiti.engine.test.Deployment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ActivitiConfig.class})
public class ExpirationReminderTest extends UnitTestAccessor {

    private static final Logger log = LoggerFactory.getLogger(ExpirationReminderTest.class);
    @Test
    @Deployment(resources = {"bpm/expirationReminder.bpmn20.xml"})
    public void testExpirationReminder() {

        String procDefKey = "ExpirationReminder";
        String bizKey = "testExpirationReminder";
        Map<String, Object>map = new HashMap<String, Object>();
        map.put("remindDate", "PT2S");
        startProcessInstanceByKey(procDefKey, bizKey, map);
        Assert.assertEquals(0, taskCount(bizKey));
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
        }

        if( taskCount(bizKey)==1 ) {
            Assert.assertNotNull(getTask(bizKey, "userResolver"));
            log.info("userResolve it .............................");
            completeTask(bizKey, "userResolver");
        }

        printTaskHistory(procDefKey, bizKey);
        printActivityHistory(procDefKey, bizKey);

        Assert.assertEquals(0, taskCount(bizKey));
    }
}
