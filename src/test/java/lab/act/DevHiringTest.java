package lab.act;

import lab.act.service.DevHireService;
import lab.act.testconf.ActivitiConfig;
import lab.act.testconf.UnitTestAccessor;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;


/**
 * house keeper work in UnitTestAccessor, here just focus on task and work flow
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ActivitiConfig.class})
public class DevHiringTest extends UnitTestAccessor {

    @Autowired
    public DevHireService devHireService;

    @Test
    @Deployment(resources = {"DeveloperHiring.bpmn20.xml"})
    public void testPhoneInterviewOk() {
        String procDefKey = "hireProcess";
        String bizKey = "bob";
        Map<String, Object>map = new HashMap<String, Object>();
        map.put("devHireService", devHireService);
        startProcessInstanceByKey(procDefKey, bizKey, map);
        Assert.assertEquals(1, taskCount(bizKey));
        Task task = getTask(bizKey, "phoneInterview");
        taskService.setVariable(task.getId(), "telephoneInterviewOutcome", true);
        completeTask(bizKey, "phoneInterview");
        //
        Assert.assertEquals(2, taskCount(bizKey));
        //
        Task task1 = getTask(bizKey, "techInterview");
        Assert.assertNotNull(task1);
        Task task2 = getTask(bizKey, "financialNegotiation");
        Assert.assertNotNull(task2);
        //
        taskService.setVariable(task1.getId(), "techOK", true);
        completeTask(bizKey, "techInterview");
        taskService.setVariable(task2.getId(), "financialOk", true);
        completeTask(bizKey, "financialNegotiation");
        Assert.assertEquals(0, taskCount(bizKey));
        //
        printTaskHistory(procDefKey, bizKey);
        printActivityHistory(procDefKey, bizKey);
    }

    public void testPhoneInterview() {
        String procDefKey = "hireProcess";
        String bizKey = "bob";
        Map<String, Object>map = new HashMap<String, Object>();
        map.put("devHireService", devHireService);
        startProcessInstanceByKey(procDefKey, bizKey, map);
        Assert.assertEquals(1, taskCount(bizKey));
        Task task = getTask(bizKey, "phoneInterview");
        taskService.setVariable(task.getId(), "telephoneInterviewOutcome", false);
        completeTask(bizKey, "phoneInterview");
        Assert.assertEquals(0, taskCount(bizKey));
    }

}
