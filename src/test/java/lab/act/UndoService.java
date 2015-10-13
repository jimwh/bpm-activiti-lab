package lab.act;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class UndoService implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(UndoService.class);

    private Expression counterName;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        // alternative way to set
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

        /*
        String variableName = counterName.getValue(execution).toString();
        log.info("variableName={}", variableName);
        Object variable = execution.getVariable(variableName);
        if (variable == null) {
            log.info("var is null");
            execution.setVariable(variableName, 1);
        } else {
            log.info("var is not null");
            execution.setVariable(variableName, ((Integer) variable) + 1);
        }
        */

    }

}
