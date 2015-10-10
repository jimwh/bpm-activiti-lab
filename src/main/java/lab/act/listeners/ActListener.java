package lab.act.listeners;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jh3389 on 9/27/15.
 */
public class ActListener implements TaskListener, ExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(ActListener.class);

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        String eventName = delegateExecution.getEventName();
        if (EVENTNAME_START.equals(eventName)) {
            onStart(delegateExecution);
        }
    }

    private void onStart(DelegateExecution execution) {
        ExecutionEntity thisEntity = (ExecutionEntity) execution;
        ExecutionEntity superExecEntity = thisEntity.getSuperExecution();
        String eventName = execution.getEventName();

        if (superExecEntity == null) {
            // get the business key of the main process
            log.info("main process: eventName={}, bizKey={}, procDefId={}", eventName, thisEntity.getBusinessKey(), thisEntity.getProcessDefinitionId());
            // thisEntity.setVariable(...);
        } else {
            // in a sub-process so get the BusinessKey variable set by the caller.
            String key = (String) superExecEntity.getVariable("BusinessKey");
            // ... superExecEntity.getVariable(...);
            log.info("sub-process: eventName={}, bizKey={}, procDefId={}",
                    eventName, key, thisEntity.getProcessDefinitionId());
            thisEntity.setVariable("BusinessKey", key);
            // for get task by business key
            thisEntity.setBusinessKey(key);
        }
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if (EVENTNAME_CREATE.equals(eventName)) {
            onCreate(EVENTNAME_CREATE, delegateTask);
        } else if (EVENTNAME_COMPLETE.equals(eventName)) {
            onComplete(EVENTNAME_COMPLETE, delegateTask);
        } else if (EVENTNAME_END.equals(eventName)) {
            logTaskEvent(EVENTNAME_END, delegateTask);
        }
    }

    private void onCreate(String eventName, DelegateTask task) {
        logTaskEvent(eventName, task);
    }

    private void onComplete(String eventName, DelegateTask task) {
        logTaskEvent(eventName, task);
    }

    private void logTaskEvent(String eventName, DelegateTask task) {
        DelegateExecution taskExecution = task.getExecution();
        String bizKey = taskExecution.getProcessBusinessKey();
        String processId = taskExecution.getProcessInstanceId();
        String taskId = task.getId();
        String taskDefKey = task.getTaskDefinitionKey();
        log.info("{}: bizKey={}, taskDefKey={}, taskId={}, processId={}",
                eventName, bizKey, taskDefKey, taskId, processId);
    }
}
