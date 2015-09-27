package lab.act;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import org.junit.Assert;
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
public class MyTest {

    private static final Logger log = LoggerFactory.getLogger(MyTest.class);

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Test
    public void test() {
        String bizKey = "testAutoDeactivate-bizKey";
        testAutoDeactivate(bizKey);
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
        }
        log.info("after timer fired open task count: {}", taskCount(bizKey));
        printHistory(bizKey);

    }

    public void testAutoDeactivate(String bizKey) {
        String ProcessDefKey = "icoi";
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(ProcessDefKey, bizKey);
        Assert.assertNotNull(instance);

        log.info("open task count: {}", taskCount(bizKey));
        completeTask(bizKey, "submit");
        log.info("open task count: {}", taskCount(bizKey));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("RETURN", Boolean.FALSE);
        completeTask(bizKey, "activate", map);
        log.info("open task count: {}", taskCount(bizKey));

    }

    public void testDeactivate() {
        String ProcessDefKey = "icoi";
        String bizKey = "testDeactivate-bizKey";
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(ProcessDefKey, bizKey);
        Assert.assertNotNull(instance);

        log.info("open task count: {}", taskCount(bizKey));
        completeTask(bizKey, "submit");
        log.info("open task count: {}", taskCount(bizKey));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("RETURN", Boolean.FALSE);
        completeTask(bizKey, "activate", map);
        log.info("open task count: {}", taskCount(bizKey));
        completeTask(bizKey, "deactivate");
        log.info("open task count: {}", taskCount(bizKey));
    }

    public void testReturn() {
        String ProcessDefKey = "icoi";
        String bizKey = "testReturn-bizKey";
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(ProcessDefKey, bizKey);
        Assert.assertNotNull(instance);

        log.info("open task count: {}", taskCount(bizKey));
        completeTask(bizKey, "submit");
        log.info("open task count: {}", taskCount(bizKey));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("RETURN", Boolean.TRUE);
        completeTask(bizKey, "return", map);
        log.info("open task count: {}", taskCount(bizKey));
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
        for(Task task: list) {
            log.info("key={}",task.getTaskDefinitionKey());
        }
    }

    void printHistory(String bizKey) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceBusinessKey(bizKey)
                .list();
        for(HistoricTaskInstance ht: list) {
            log.info("key={}, name={}, delReasone={}", ht.getTaskDefinitionKey(), ht.getName(), ht.getDeleteReason());
        }
    }
}
