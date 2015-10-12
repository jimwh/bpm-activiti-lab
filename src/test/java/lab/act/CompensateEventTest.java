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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ActivitiConfig.class})
public class CompensateEventTest extends UnitTestAccessor {

    @Test
    @Deployment(resources = {"TestCompensateScope.bpmn20.xml"})
    public void testCompensateScope() {

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("compensateProcess");

        assertEquals(5, runtimeService.getVariable(processInstance.getId(), "undoBookHotel"));
        assertEquals(5, runtimeService.getVariable(processInstance.getId(), "undoBookFlight"));

        runtimeService.signal(processInstance.getId());

        //assertProcessEnded(processInstance.getId());

    }


}
