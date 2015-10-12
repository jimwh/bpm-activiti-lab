package lab.act.service;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpirationReminder implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(ExpirationReminder.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("calling expirationReminder");
        expirationReminder(delegateExecution);
    }

    private void expirationReminder(DelegateExecution execution) throws Exception {

        log.info("bizKey={}, currentActivityId={}",
                execution.getProcessBusinessKey(),
                execution.getCurrentActivityId()
        );
        throw new BpmnError("ReminderException");
    }
}
