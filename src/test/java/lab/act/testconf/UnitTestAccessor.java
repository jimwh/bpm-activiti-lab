package lab.act.testconf;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * handy class for testing
 */
public class UnitTestAccessor {

    private static final Logger log = LoggerFactory.getLogger(UnitTestAccessor.class);

    @Rule
    @Autowired
    public ActivitiRule activitiRule;

    @Autowired
    public ProcessEngine processEngine;

    @Autowired
    public RuntimeService runtimeService;

    @Autowired
    public TaskService taskService;

    @Autowired
    public HistoryService historyService;

    public void startProcessInstanceByKey(final String procDefKey, final String bizKey) {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(procDefKey, bizKey);
        Assert.assertNotNull(instance);
    }

    public void startProcessInstanceByKey(final String procDefKey, final String bizKey, final Map<String, Object> map) {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(procDefKey, bizKey, map);
        Assert.assertNotNull(instance);
    }

    public void completeTask(final String bizKey, final String taskDefKey) {
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(bizKey)
                .taskDefinitionKey(taskDefKey)
                .singleResult();
        Assert.assertNotNull(task);
        taskService.complete(task.getId());
    }

    public void completeTask(final String bizKey,
                             final String taskDefKey,
                             final Map<String, Object> taskVariable) {
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(bizKey)
                .taskDefinitionKey(taskDefKey)
                .singleResult();
        Assert.assertNotNull(task);
        taskService.complete(task.getId(), taskVariable);
    }

    public long taskCount(final String bizKey) {
        return taskService
                .createTaskQuery()
                .processInstanceBusinessKey(bizKey).count();
    }

    public void printTask(String bizKey) {
        List<Task> list = taskService
                .createTaskQuery()
                .processInstanceBusinessKey(bizKey)
                .active()
                .list();
        for (Task task : list) {
            log.info("key={}", task.getTaskDefinitionKey());
        }
    }

    public void printTaskHistory(final String procDefKey, final String bizKey) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey(procDefKey)
                .processInstanceBusinessKey(bizKey)
                .orderByHistoricTaskInstanceEndTime()
                .desc()
                .list();
        for (HistoricTaskInstance ht : list) {
            log.info("key={}, name={}, delReason={}", ht.getTaskDefinitionKey(), ht.getName(), ht.getDeleteReason());
        }
    }

    public Task getTask(final String bizKey, final String taskDefKey) {
        return taskService.createTaskQuery()
                .processInstanceBusinessKey(bizKey)
                .taskDefinitionKey(taskDefKey)
                .singleResult();
    }

    public void printActivityHistory(final String procDefKey, final String bizKey) {
        List<HistoricProcessInstance> hpiList = historyService.createHistoricProcessInstanceQuery()
                .notDeleted()
                .processDefinitionKey(procDefKey)
                .processInstanceBusinessKey(bizKey)
                .orderByProcessInstanceEndTime()
                .desc()
                .list();

        HistoricProcessInstance hp = hpiList.get(0);

        List<HistoricActivityInstance> list =
                historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(hp.getId())
                        .orderByHistoricActivityInstanceEndTime()
                        .desc()
                        .list();
        for (HistoricActivityInstance ht : list) {
            DateTime dt = new DateTime(ht.getEndTime());
            log.info("key={}, name={}, endTime={}",
                    ht.getProcessDefinitionId(),
                    ht.getActivityName(),
                    dt.toString("MM/dd/yyyy HH:mm:ss"));
        }
    }

    public ProcessInstance getProcessInstance(final String procKey, final String bizKey) {
        final List<ProcessInstance> list = runtimeService.createProcessInstanceQuery()
                .active()
                .includeProcessVariables()
                .processDefinitionKey(procKey)
                .processInstanceBusinessKey(bizKey)
                .orderByProcessDefinitionId()
                .desc()
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    public boolean hasProcess(final String processId) {
        return runtimeService.createProcessInstanceQuery()
                .processInstanceId(processId)
                .active()
                .singleResult()!=null;
    }


}
