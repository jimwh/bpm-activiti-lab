package lab.act;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetVariablesDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(GetVariablesDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        Object nrOfCompletedInstances = execution.getVariable("nrOfCompletedInstances");

        Integer variable = SetVariablesDelegate.variablesMap.get(nrOfCompletedInstances);
        Object variableLocal = execution.getVariable("variable");
        log.info("...............variable={}", variableLocal);
        if (!variableLocal.equals(variable)) {
            throw new ActivitiIllegalArgumentException("wrong variable passed in to compensation handler");
        }
    }

}
