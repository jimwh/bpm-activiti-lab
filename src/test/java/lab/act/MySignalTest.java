package lab.act;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/test-context.xml"})
public class MySignalTest {

    private static final Logger log = LoggerFactory.getLogger(MySignalTest.class);

    // include this rule, then you can use @Deployment resource for testing each one
    // avoid auto deployment
    @Autowired
    @Rule
    public ActivitiRule activitiRule;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Test
    @Deployment(resources = {"rr_with_signal.bpmn20.xml"} )
    public void test() {
        String procDefKey = "R_R_S";

        testReturn(procDefKey, "testReturn");

        testApprove(procDefKey, "testApprove");

        //testDeactivate(procDefKey, "testDeactivate");

        //testAutoDeactivate();
    }

    void testAutoDeactivate() {
        String processDefKey = "R_R_S";
        String bizKey = "testAutoDeactivate-bizKey";
        testAutoDeactivate(processDefKey, bizKey);
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
        }
        log.info("after timer fired open task count: {}", taskCount(bizKey));
        printHistory(bizKey);
    }

    private void testAutoDeactivate(String processDefKey, String bizKey) {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(processDefKey, bizKey);
        Assert.assertNotNull(instance);
        Assert.assertEquals(1, taskCount(bizKey));

        completeTask(bizKey, "submit");
        Assert.assertEquals(2, taskCount(bizKey));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("RETURN", Boolean.FALSE);
        completeTask(bizKey, "activate", map);
        Assert.assertEquals(1, taskCount(bizKey));
    }

    void testDeactivate(String procDefKey, String bizKey) {

        ProcessInstance instance = runtimeService.startProcessInstanceByKey(procDefKey, bizKey);
        Assert.assertNotNull(instance);
        Assert.assertEquals(1, taskCount(bizKey));

        completeTask(bizKey, "submit");
        Assert.assertEquals(2, taskCount(bizKey));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("RETURN", Boolean.FALSE);
        completeTask(bizKey, "activate", map);
        Assert.assertEquals(1, taskCount(bizKey));

        completeTask(bizKey, "deactivate");
        Assert.assertEquals(0, taskCount(bizKey));
    }

    void testReturn(String procDefKey, String bizKey) {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(procDefKey, bizKey);
        Assert.assertNotNull(instance);

        Assert.assertEquals(1, taskCount(bizKey));

        completeTask(bizKey, "submit");
        Assert.assertEquals(2, taskCount(bizKey));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("RETURN", Boolean.TRUE);
        completeTask(bizKey, "return", map);
        //Assert.assertEquals(0, taskCount(bizKey));
        log.info("open:{}", taskCount(bizKey));
        // printTask(bizKey);
        printHistory(bizKey);
    }

    void testApprove(String procDefKey, String bizKey) {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(procDefKey, bizKey);
        Assert.assertNotNull(instance);

        Assert.assertEquals(1, taskCount(bizKey));

        completeTask(bizKey, "submit");
        Assert.assertEquals(2, taskCount(bizKey));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("RETURN", Boolean.FALSE);
        completeTask(bizKey, "approve", map);
        //Assert.assertEquals(0, taskCount(bizKey));
        log.info("open:{}", taskCount(bizKey));
        // printTask(bizKey);
        printHistory(bizKey);
    }

    void completeTask(String bizKey, String taskDefKey) {
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(bizKey)
                .taskDefinitionKey(taskDefKey)
                .singleResult();
        Assert.assertNotNull(task);
        taskService.complete(task.getId());
    }

    void completeTask(String bizKey, String taskDefKey, Map<String, Object> taskVariable) {
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(bizKey)
                .taskDefinitionKey(taskDefKey)
                .singleResult();
        Assert.assertNotNull(task);
        taskService.complete(task.getId(), taskVariable);
    }

    long taskCount(String bizKey) {
        return taskService
                .createTaskQuery()
                .processInstanceBusinessKey(bizKey).count();
    }

    void printTask(String bizKey) {
        List<Task> list = taskService
                .createTaskQuery()
                .processInstanceBusinessKey(bizKey)
                .active()
                .list();
        for (Task task : list) {
            log.info("key={}", task.getTaskDefinitionKey());
        }
    }

    void printHistory(String bizKey) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceBusinessKey(bizKey)
                .list();
        for (HistoricTaskInstance ht : list) {
            log.info("key={}, name={}, delReason={}", ht.getTaskDefinitionKey(), ht.getName(), ht.getDeleteReason());
        }
    }
}
