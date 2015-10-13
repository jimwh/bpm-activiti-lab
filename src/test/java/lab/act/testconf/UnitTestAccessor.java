package lab.act.testconf;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
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

    public void startProcessInstanceByKey(String procDefKey, String bizKey) {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(procDefKey, bizKey);
        Assert.assertNotNull(instance);
    }

    public void startProcessInstanceByKey(String procDefKey, String bizKey, Map<String, Object> map) {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(procDefKey, bizKey, map);
        Assert.assertNotNull(instance);
    }

    public void completeTask(String bizKey, String taskDefKey) {
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(bizKey)
                .taskDefinitionKey(taskDefKey)
                .singleResult();
        Assert.assertNotNull(task);
        taskService.complete(task.getId());
    }

    public void completeTask(String bizKey, String taskDefKey, Map<String, Object> taskVariable) {
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(bizKey)
                .taskDefinitionKey(taskDefKey)
                .singleResult();
        Assert.assertNotNull(task);
        taskService.complete(task.getId(), taskVariable);
    }

    public long taskCount(String bizKey) {
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

    public void printTaskHistory(String procDefKey, String bizKey) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey(procDefKey)
                .processInstanceBusinessKey(bizKey)
                .finished()
                .orderByHistoricTaskInstanceEndTime()
                .desc()
                .list();
        for (HistoricTaskInstance ht : list) {
            log.info("key={}, name={}, delReason={}", ht.getTaskDefinitionKey(), ht.getName(), ht.getDeleteReason());
        }
    }

    public Task getTask(String bizKey, String taskDefKey) {
        return taskService.createTaskQuery()
                .processInstanceBusinessKey(bizKey)
                .taskDefinitionKey(taskDefKey)
                .singleResult();
    }

    public void printActivityHistory(String procDefKey, String bizKey) {
        List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery()
                .notDeleted()
                .processDefinitionKey(procDefKey)
                .processInstanceBusinessKey(bizKey)
                .orderByProcessInstanceEndTime()
                .desc()
                .list();

        for (HistoricProcessInstance hp : historicProcessInstanceList) {
            log.info("{}, {}, {}", hp.getProcessDefinitionId(), hp.getId(), hp.getEndTime());
        }
        HistoricProcessInstance hp = historicProcessInstanceList.get(0);

        List<HistoricActivityInstance> list =
                historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(hp.getId())
                        .orderByHistoricActivityInstanceEndTime()
                        .desc()
                        .list();
        for (HistoricActivityInstance ht : list) {
            log.info("key={}, name={}, endTime={}",
                    ht.getProcessDefinitionId(),
                    ht.getActivityName(),
                    ht.getEndTime());
        }
    }

    public ProcessInstance getProcessInstance(String procKey, String bizKey) {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery()
                .active()
                .includeProcessVariables()
                .processDefinitionKey(procKey)
                .processInstanceBusinessKey(bizKey)
                .orderByProcessDefinitionId()
                .desc()
                .list();
        return list.get(0);
    }

    public boolean hasProcess(String processId) {
        return runtimeService.createProcessInstanceQuery()
                .processInstanceId(processId)
                .active()
                .singleResult()==null;
    }


}
