package lab.act;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class UndoService implements JavaDelegate {

    @Autowired
    private Expression counterName;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        // alternative way to set
        /*
        Set<String>set=execution.getVariableNames();
        if( set.isEmpty() ) {
            execution.setVariable("undoBookHotel", 1);
        }else if ( !set.contains("undoBookFlight") ) {
            execution.setVariable("undoBookFlight", 1);
        }
        for(String varName: set) {
            Object o = execution.getVariable(varName);
            if(o == null) {
                execution.setVariable(varName, 1);
            }else {
                if(o instanceof Integer) {
                    Integer i = (Integer) o;
                    if( i.intValue() < 5) {
                        execution.setVariable(varName, i + 1);
                    }
                }
            }
        }
        */


        String variableName = counterName.getValue(execution).toString();
        Object variable = execution.getVariable(variableName);
        if (variable == null) {
            execution.setVariable(variableName, 1);
        } else {
            if(variable instanceof Integer)
                execution.setVariable(variableName, ((Integer)variable) + 1);
        }


    }

}
