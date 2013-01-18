
package org.jbpm.evaluation.carinsurance;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.process.instance.impl.demo.SystemOutWorkItemHandler;
import org.jbpm.process.workitem.wsht.SyncWSHumanTaskHandler;
import org.jbpm.task.AccessType;
import org.jbpm.task.TaskService; 
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.ContentData; 
import org.jbpm.test.JbpmJUnitTestCase;
import org.junit.Ignore;
import org.junit.Test; 

/**
 * This is a sample file to launch a process.
 */
@Ignore  
public class QuoteApprovalTest extends JbpmJUnitTestCase {

	private static StatefulKnowledgeSession ksession;
	private static TaskService taskService;
	private static Map<String, Object> params;
	private static ProcessInstance processInstance;
	
	public QuoteApprovalTest() {
		super(true);
		//setPersistence(true);
	}

	private void setupTestCase() {		
		
		System.out.println("starting quoteStage1.bpmn2");
		ksession = createKnowledgeSession("quoteStage1.bpmn2");
	    taskService = getTaskService(ksession);
		
		// register human task work item.
		System.out.println("register human task handler");
		SyncWSHumanTaskHandler humanTaskHandler = new SyncWSHumanTaskHandler(taskService, ksession);
		humanTaskHandler.setLocal(true);
		humanTaskHandler.connect();
		ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanTaskHandler);
	
		// register other work items
		System.out.println("register work items");
		ksession.getWorkItemManager().registerWorkItemHandler("name", new SystemOutWorkItemHandler());
		ksession.getWorkItemManager().registerWorkItemHandler("age", new SystemOutWorkItemHandler());
	
		System.out.println("Create hashmap and put params");
		params = new HashMap<String, Object>();
		// initialize process parameters.
		params.put("name", "derek");
		params.put("age", "51");
		
	}

	@Test
	public void quoteApprovalTest() {
		System.out.println("starting quoteApprovalTest");
		setupTestCase();
	
		// start process.
		processInstance = ksession.startProcess("org.jbpm.evaluation.carinsurance.quote1", params);

		// execute task by Mary from HR.
		System.out.println("execute task by Mary from HR");
		List<TaskSummary> list = taskService.getTasksAssignedAsPotentialOwner("mary", new ArrayList<String>(), "en-UK");
		TaskSummary task = list.get(0);
		taskService.claim(task.getId(), "mary", new ArrayList<String>());
		taskService.start(task.getId(), "mary");
		
		System.out.println("create map and insert paramsHR");
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put("name", "derek");
		taskParams.put("age", "21");
		taskParams.put("outcome", "outcome");
		
		// Serialized and inserted.
		System.out.println("serialise and insert content");
		ContentData content = new ContentData();
		content.setAccessType(AccessType.Inline);
		content.setContent(getByteArrayFromObject(taskParams));
		
		// add results of task.
		taskService.complete(task.getId(), "mary", content);
		
		System.out.println("assert process complete");
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
		
		System.out.println("assert task1 complete");
		assertNodeTriggered(processInstance.getId(), "GetDriver");
		
		// Did I get to the end
		System.out.println("reached end node");
		assertNodeTriggered(processInstance.getId(), "Ends");
		ksession.dispose();
	}
	
	@Test
	public void quoteRejectedTest() {
		System.out.println("starting quoteRejectedTest");
		setupTestCase();
		
		// start process.
		processInstance = ksession.startProcess("org.jbpm.evaluation.carinsurance.quote1", params);
		
		// execute task by John from HR.
		List<TaskSummary> list = taskService.getTasksAssignedAsPotentialOwner("john", new ArrayList<String>(), "en-UK");
		TaskSummary task = list.get(0);
		taskService.claim(task.getId(), "john", new ArrayList<String>());
		taskService.start(task.getId(), "john");
		
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put("Explanation", "Too complicated for me");
		taskParams.put("Outcome", "Rejected");
		
		// Serialized and inserted.
		ContentData content = new ContentData();
		content.setAccessType(AccessType.Inline);
		content.setContent(getByteArrayFromObject(taskParams));
		
		// add results of task.
		taskService.complete(task.getId(), "john", content);
		
		// test for completion and in correct end node.
		assertProcessInstanceCompleted(processInstance.getId(), ksession);
		assertNodeTriggered(processInstance.getId(), "GetDriver");
		assertNodeTriggered(processInstance.getId(), "Ends");
		ksession.dispose();
	}
	
	/**
	 * Converts an object to a serialized byte array.
	 * 
	 * @param obj Object to be converted.
	 * @return byte[] Serialized array representing the object.
	 */
	public static byte[] getByteArrayFromObject(Object obj) {
	    byte[] result = null;
	       
	    try {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream(baos);
	        oos.writeObject(obj);
	        oos.flush();
	        oos.close();
	        baos.close();
	        result = baos.toByteArray();
	    } catch (IOException ioEx) {
	        //Logger.getLogger("UtilityMethods").error("Error converting object to byteArray", ioEx);
	    }
	        
	    return result;
	}

}