package lab.act;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import java.util.HashMap;
import java.util.Map;

public class SetVariablesDelegate implements JavaDelegate {

    public static Map<Object, Integer> variablesMap = new HashMap<Object, Integer>();

    // activiti creates a single instance of the delegate
    protected int lastInt = 0;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Object nrOfCompletedInstances = execution.getVariableLocal("nrOfCompletedInstances");
        variablesMap.put(nrOfCompletedInstances, lastInt);
        execution.setVariableLocal("variable", lastInt);
        lastInt++;
    }

}
