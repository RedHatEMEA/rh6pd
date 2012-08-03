package rh6pd.test;

import java.util.HashMap;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.test.JbpmJUnitTestCase;
 
public abstract class AbstractProcessTest extends JbpmJUnitTestCase {
	private StatefulKnowledgeSession session;
	private final HashMap<String, Object> props = new HashMap<String, Object>();
	private boolean isStarted = false; 
	  
	protected void createNewSession(String... bpmnFiles) {
		isStarted = false;   
		this.session = createKnowledgeSession(bpmnFiles);
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
	
	protected void printBpmnErrors(String... listBpmnFiles) {
		KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
		
		for (String bpmnFile : listBpmnFiles) {
			kb.add(ResourceFactory.newClassPathResource(bpmnFile), ResourceType.BPMN2);
		}
		
		KnowledgeBuilderErrors errs = kb.getErrors();
		
		for (KnowledgeBuilderError err : errs) {
			System.out.println(err.toString()); 
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