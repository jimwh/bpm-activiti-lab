package lab.act;

import lab.act.testconf.ActivitiConfig;
import lab.act.testconf.UnitTestAccessor;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ActivitiConfig.class})
public class CompensateEventTest extends UnitTestAccessor {

    @Test
    @Deployment(resources = {"TestCompensateScope.bpmn20.xml"})
    public void testCompensateScope() {
        String procKey = "compensateProcess";
        String bizKey = "testCompensateScope";
        startProcessInstanceByKey(procKey, bizKey);
        ProcessInstance instance = getProcessInstance(procKey,bizKey);
        assertEquals(5, runtimeService.getVariable(instance.getId(), "undoBookHotel"));
        assertEquals(5, runtimeService.getVariable(instance.getId(), "undoBookFlight"));

        runtimeService.signal(instance.getId());

        assertEquals(false, hasProcess(instance.getId()));
        assertNull( getProcessInstance(procKey,bizKey) );

        printActivityHistory(procKey, bizKey);
        printTaskHistory(procKey,bizKey);

    }
}
