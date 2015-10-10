package lab.act.listeners;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jh3389 on 10/1/15.
 */
public class ActEvtListener implements ActivitiEventListener {

    private static final Logger log = LoggerFactory.getLogger(ActEvtListener.class);

    @Override
    public void onEvent(ActivitiEvent activitiEvent) {
        log.info("executionId={}, processDefId={}, prcInstId={}, name={}",
                activitiEvent.getExecutionId(),
                activitiEvent.getProcessDefinitionId(),
                activitiEvent.getProcessInstanceId(),
                activitiEvent.getType().name());
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
