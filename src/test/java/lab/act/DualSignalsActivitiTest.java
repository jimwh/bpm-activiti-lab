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

/**
 * house keeper work in UnitTestAccessor, here just focus on task and work flow
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ActivitiConfig.class})
public class DualSignalsActivitiTest extends UnitTestAccessor {

    private static final Logger log = LoggerFactory.getLogger(DualSignalsActivitiTest.class);

    @Test
    @Deployment(resources = {"dualSignals.bpmn20.xml"})
    public void test() {
        String procDefKey = "dualSignals";

        takeTrain(procDefKey, "takeTrain");
        printTaskHistory(procDefKey, "takeTrain");

        takeFlight(procDefKey, "takeFlight");
        printTaskHistory(procDefKey, "takeFlight");
    }

    void takeTrain(String procDefKey, String bizKey) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("RETURN", Boolean.TRUE);

        startProcessInstanceByKey(procDefKey, bizKey, map);

        Assert.assertEquals(1, taskCount(bizKey));

        completeTask(bizKey, "enter");
        Assert.assertEquals(2, taskCount(bizKey));

        completeTask(bizKey, "train", map);
        Assert.assertEquals(1, taskCount(bizKey));
        Assert.assertNotNull(getTask(bizKey, "bookTicket"));
    }

    void takeFlight(String procDefKey, String bizKey) {
        startProcessInstanceByKey(procDefKey, bizKey);

        Assert.assertEquals(1, taskCount(bizKey));

        completeTask(bizKey, "enter");
        Assert.assertEquals(2, taskCount(bizKey));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("RETURN", Boolean.FALSE);
        completeTask(bizKey, "flight", map);
        Assert.assertEquals(1, taskCount(bizKey));
        Assert.assertNotNull(getTask(bizKey, "bookTicket"));
    }

}
