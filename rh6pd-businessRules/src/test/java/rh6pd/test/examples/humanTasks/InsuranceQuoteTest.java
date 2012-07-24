package rh6pd.test.examples.humanTasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.task.TaskService; 
import org.jbpm.task.query.TaskSummary;
import org.jbpm.test.JbpmJUnitTestCase;


public class InsuranceQuoteTest extends JbpmJUnitTestCase {
	    
    public InsuranceQuoteTest() {
        super(true);
        setPersistence(true);
    }
   
    @org.junit.Test 
    public void testProcess() {
        StatefulKnowledgeSession ksession = createKnowledgeSession("examples/humanTasks/InsuranceQuote.bpmn");
        TaskService taskService = getTaskService(ksession);
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("validQuote", true);
        ProcessInstance processInstance = ksession.startProcess("org.jbpm.evaluation.carinsurance.quote", params);
       
        assertNodeTriggered(processInstance.getId(), "init", "Get Driver");
       
        List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner("admin", "en-UK");
        assertEquals(1, tasks.size());
        long taskId = tasks.get(0).getId();
        taskService.start(taskId, "admin");
        taskService.complete(taskId, "admin", null);
       
        assertNodeTriggered(processInstance.getId(), "CalcDriverRisk");

        ksession.fireAllRules();

        assertNodeTriggered(processInstance.getId(), "Gateway", "Get Car");

        tasks = taskService.getTasksAssignedAsPotentialOwner("admin", "en-UK");
        assertEquals(1, tasks.size());
        taskId = tasks.get(0).getId();
        taskService.start(taskId, "admin");
        taskService.complete(taskId, "admin", null);
        System.out.println("Found task " + tasks.get(0).getName());
       
    }

}
