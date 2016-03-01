package lab.act;

import lab.act.testconf.ActivitiConfig;
import lab.act.testconf.UnitTestAccessor;
import org.activiti.engine.test.Deployment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ActivitiConfig.class})
public class IcoiActivitiTest extends UnitTestAccessor {

    @Test
    @Deployment(resources = {"ICOIProcess.bpmn20.xml"})
    public void testICOI() {
        String procDefKey = "icoiProcess";
        testReturn(procDefKey, "takeTrain");
        printTaskHistory(procDefKey, "takeTrain");

        //
        testAutoDeactivate(procDefKey);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //Assert.assertEquals(0, taskCount("testAutoDeactivate"));
        printTaskHistory(procDefKey, "testAutoDeactivate");
/*
        testReActivate(procDefKey);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        Assert.assertEquals(0, taskCount("testReActivate"));
        printTaskHistory(procDefKey, "testReActivate");
*/
    }

    void testAutoDeactivate(String processDefKey) {
        String bizKey = "testAutoDeactivate";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("expirationDuration", "PT1S");
        map.put("reactivateExpirationDuration", "PT1S");

        startProcessInstanceByKey(processDefKey, bizKey, map);

        Assert.assertEquals(1, taskCount(bizKey));

        completeTask(bizKey, "submit");
        Assert.assertEquals(2, taskCount(bizKey));

        completeTask(bizKey, "deactivate", map);
        Assert.assertEquals(1, taskCount(bizKey));
    }

    void testReActivate(String procDefKey) {
        String bizKey = "testReActivate";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("expirationDuration", "PT3S");
        map.put("reactivateExpirationDuration", "PT3S");

        startProcessInstanceByKey(procDefKey, bizKey, map);

        Assert.assertEquals(1, taskCount(bizKey));

        completeTask(bizKey, "submit");
        Assert.assertEquals(2, taskCount(bizKey));

        completeTask(bizKey, "deactivate", map);
        Assert.assertEquals(1, taskCount(bizKey));

        completeTask(bizKey, "reactivate");
        Assert.assertEquals(1, taskCount(bizKey));
        Assert.assertNotNull(getTask(bizKey, "deactivate"));
    }

    void testReturn(String procDefKey, String bizKey) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("expirationDuration", "P30D");
        map.put("reactivateExpirationDuration", "P30D");

        startProcessInstanceByKey(procDefKey, bizKey, map);

        Assert.assertEquals(1, taskCount(bizKey));

        completeTask(bizKey, "submit");
        Assert.assertEquals(2, taskCount(bizKey));

        completeTask(bizKey, "return", map);
        Assert.assertEquals(0, taskCount(bizKey));
    }

}
