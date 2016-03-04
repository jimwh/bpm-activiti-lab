package lab.act;

import lab.act.testconf.ActivitiConfig;
import lab.act.testconf.UnitTestAccessor;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ActivitiConfig.class})
public class CompensateEventTest extends UnitTestAccessor {

    private static final Logger log = LoggerFactory.getLogger(CompensateEventTest.class);
    @Test
    @Deployment(resources = {"TestCompensateScope.bpmn20.xml"})
    public void foobarTest() {
        List<Map.Entry<String, String>>list=getEntry();
        // you can't add this now
        // list.add(new SimpleImmutableEntry<String, String>("foo1", "bar1"));
        for(Map.Entry<String, String>e: list) {
            log.info(e.toString());
        }
    }

    private List<Map.Entry<String, String>>getEntry(){
        List<Map.Entry<String, String>>list=new ArrayList();
        List<Map.Entry<String, String>>unmodifiableList= Collections.unmodifiableList(list);
        list.add( new SimpleImmutableEntry("foo", "bar"));
        list.add( new SimpleImmutableEntry("a", "b"));
        list.add( new SimpleImmutableEntry("c", "d"));
        return unmodifiableList;
    }

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
