package lab.act;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UndoService implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(UndoService.class);
    private Expression counterName;

    public void execute(DelegateExecution execution) throws Exception {
        String variableName = (String) counterName.getValue(execution);
        Object variable = execution.getVariable(variableName);
        if (variable == null) {
            execution.setVariable(variableName, 1);
        } else {
            execution.setVariable(variableName, ((Integer) variable) + 1);
        }
    }

}
