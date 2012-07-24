package rh6pd.test;

import java.util.HashMap;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.test.JbpmJUnitTestCase;
 
public abstract class AbstractProcessTest extends JbpmJUnitTestCase {
	private StatefulKnowledgeSession session;
	private final HashMap<String, Object> props = new HashMap<String, Object>();
	private boolean isStarted = false; 
	  
	protected void createNewSession(String bpmnFile) {
		isStarted = false; 
		this.session = createKnowledgeSession("simpleFrameworkTest/sample.bpmn");
	} 
	
	protected void insertVar(String name, Object o) {
		if (session == null) {
			throw new RuntimeException("Cannot insert variable, no session has been created.");
		} 
		 
		if (isStarted) {
			throw new RuntimeException("Cannot insert variables after the process has started.");			
		} else {
			session.insert(o); 
			props.put(name, o);
		} 
	}
 
	public void testProcess(String processId, String... requiredNodes) {	
		ProcessInstance processInstance = session.startProcess(processId, props);
		session.fireAllRules();
		
		// check whether the process instance has completed successfully 
		assertProcessInstanceCompleted(processInstance.getId(), session);
		assertNodeTriggered(processInstance.getId(), requiredNodes);
	}

}