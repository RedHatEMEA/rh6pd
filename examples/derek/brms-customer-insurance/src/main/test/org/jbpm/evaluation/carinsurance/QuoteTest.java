package org.jbpm.evaluation.carinsurance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.task.TaskService;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.test.JbpmJUnitTestCase;
import org.junit.Test;

public class QuoteTest extends JbpmJUnitTestCase {
	public QuoteTest() {
		super(true); 
	}
   
    @Test
    public void testProcess() {  
    	setPersistence(true); 
        StatefulKnowledgeSession ksession = createKnowledgeSession("Quote.bpmn");
        TaskService taskService = getTaskService(ksession); 
       
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("validQuote", true);
        ProcessInstance processInstance =
            ksession.startProcess("org.jbpm.evaluation.carinsurance.Quote", params);
       
        assertNodeTriggered(processInstance.getId(), "Start");
        
        assertNodeTriggered(processInstance.getId(), "Init", "GetDriver");
       
        List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner("admin", "en-UK");
        assertEquals(1, tasks.size());
        long taskId = tasks.get(0).getId();
        taskService.start(taskId, "admin");
        taskService.complete(taskId, "admin", null);
       
        assertNodeTriggered(processInstance.getId(), "CalcDriverRisk");

        ksession.fireAllRules();

        

        tasks = taskService.getTasksAssignedAsPotentialOwner("admin", "en-UK");
        assertEquals(1, tasks.size());
        taskId = tasks.get(0).getId();
        taskService.start(taskId, "admin");
        taskService.complete(taskId, "admin", null);
        System.out.println("Found task " + tasks.get(0).getName());
       
        assertNodeTriggered(processInstance.getId(), "End");
    }

}