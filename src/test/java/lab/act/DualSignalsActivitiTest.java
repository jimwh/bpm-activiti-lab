package lab.act;

import lab.act.testconf.ActivitiConfig;
import lab.act.testconf.UnitTestAccessor;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * house keeper work in UnitTestAccessor, here just focus on task and work flow
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ActivitiConfig.class})
public class DualSignalsActivitiTest extends UnitTestAccessor {

    @Test
    @Deployment(resources = {"bpm/dualSignals.bpmn20.xml"})
    public void test() {
        String procDefKey = "dualSignals";

        takeTrain(procDefKey, "takeTrain");
        printTaskHistory(procDefKey, "takeTrain");

        takeFlight(procDefKey, "takeFlight");
        printTaskHistory(procDefKey, "takeFlight");
    }

    void takeTrain(String procDefKey, String bizKey) {
        startProcessInstanceByKey(procDefKey, bizKey);
        Assert.assertEquals(1, taskCount(bizKey));

        completeTask(bizKey, "enter");
        Assert.assertEquals(2, taskCount(bizKey));

        completeTask(bizKey, "train");
        Assert.assertEquals(1, taskCount(bizKey));
        Assert.assertNotNull(getTask(bizKey, "bookTicket"));
    }

    void takeFlight(String procDefKey, String bizKey) {
        startProcessInstanceByKey(procDefKey, bizKey);

        Assert.assertEquals(1, taskCount(bizKey));

        completeTask(bizKey, "enter");
        Assert.assertEquals(2, taskCount(bizKey));

        Task task = getTask(bizKey, "flight");
        taskService.setVariable(task.getId(), "from", "flight");
        completeTask(bizKey, "flight");

        Assert.assertEquals(1, taskCount(bizKey));
        Assert.assertNotNull(getTask(bizKey, "bookTicket"));
        task = getTask(bizKey, "bookTicket");
        Assert.assertNotNull(taskService.getVariable(task.getId(), "from"));
        Assert.assertEquals("previous task is {}", "flight",taskService.getVariable(task.getId(), "from"));
    }

}
