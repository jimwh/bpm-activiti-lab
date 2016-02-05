package lab.act;

import lab.act.service.DevHireService;
import lab.act.testconf.ActivitiConfig;
import lab.act.testconf.UnitTestAccessor;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * house keeper work in UnitTestAccessor, here just focus on task and work flow
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ActivitiConfig.class})
public class DevHiringTest extends UnitTestAccessor {

    private static final Logger log= LoggerFactory.getLogger(DevHiringTest.class);
    @Autowired
    public DevHireService devHireService;

    @Test
    @Deployment(resources = {"DeveloperHiring.bpmn20.xml"})
    public void testPhoneInterviewOk() {
        LocalDate now = LocalDate.now();
        // 02-JAN-16
        // DateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour)
        DateTime dt = new DateTime(2016, 1, 2, 0, 0);
        int remainingDays = getDaysToExpiration(now, dt.toDate());
        log.info("now={}, remainingDays={}", now, remainingDays);

        String procDefKey = "hireProcess";
        String bizKey = "bob";
        Map<String, Object>map = new HashMap<String, Object>();
        map.put("devHireService", devHireService);
        startProcessInstanceByKey(procDefKey, bizKey, map);
        Assert.assertEquals(1, taskCount(bizKey));
        Task task = getTask(bizKey, "phoneInterview");
        taskService.setVariable(task.getId(), "telephoneInterviewOutcome", true);
        completeTask(bizKey, "phoneInterview");
        //
        Assert.assertEquals(2, taskCount(bizKey));
        //
        Task task1 = getTask(bizKey, "techInterview");
        Assert.assertNotNull(task1);
        Task task2 = getTask(bizKey, "financialNegotiation");
        Assert.assertNotNull(task2);
        //
        taskService.setVariable(task1.getId(), "techOK", true);
        completeTask(bizKey, "techInterview");
        taskService.setVariable(task2.getId(), "financialOK", true);
        completeTask(bizKey, "financialNegotiation");
        Assert.assertEquals(0, taskCount(bizKey));
        //
        printTaskHistory(procDefKey, bizKey);
        printActivityHistory(procDefKey, bizKey);
    }

    public void testPhoneInterview() {
        String procDefKey = "hireProcess";
        String bizKey = "bob";
        Map<String, Object>map = new HashMap<String, Object>();
        map.put("devHireService", devHireService);
        startProcessInstanceByKey(procDefKey, bizKey, map);
        Assert.assertEquals(1, taskCount(bizKey));
        Task task = getTask(bizKey, "phoneInterview");
        taskService.setVariable(task.getId(), "telephoneInterviewOutcome", false);
        completeTask(bizKey, "phoneInterview");
        Assert.assertEquals(0, taskCount(bizKey));
    }

    private int getDaysToExpiration(LocalDate runDate, Date date) {
        Days d = Days.daysBetween(runDate.toDateTimeAtCurrentTime(), new DateTime(date));
        // return LocalDate.fromDateFields(date).getDayOfYear() - runDate.getDayOfYear();
        return d.getDays();
    }

}
