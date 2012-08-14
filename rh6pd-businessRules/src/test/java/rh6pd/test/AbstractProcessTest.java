package rh6pd.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.process.Process;
import org.drools.io.ResourceFactory;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.process.WorkItem;
import org.jbpm.process.workitem.wsht.WSHumanTaskHandler;
import org.jbpm.test.JbpmJUnitTestCase;

import rh6pd.resources.FtlResourceType;
 
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
	
//	protected void insertWorkItemParameters(String name, String value) {
//		// TODO Auto-generated method stub
//		
//	}
	
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
 
	public void testProcess(String ruleFileName, String processId, String... requiredNodes) {	
		ProcessInstance processInstance = session.startProcess(processId, props);
		
		if(ruleFileName!=null && !ruleFileName.isEmpty()){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(new ClassPathResource(ruleFileName), ResourceType.DRL);
		session.getKnowledgeBase().addKnowledgePackages(kbuilder.getKnowledgePackages());
		}
		session.fireAllRules();
		
		// check whether the process instance has completed successfully 
		assertProcessInstanceCompleted(processInstance.getId(), session);
		assertNodeTriggered(processInstance.getId(), requiredNodes);
	}
	
	protected void testHumanTask(String formFileName, String processId, String workItemName,  Map humanTaskParameters, Map contentParameters, String... nodes) {
		TestWorkItemHandler testWorkItemHandler = new TestWorkItemHandler();
		session.getWorkItemManager().registerWorkItemHandler(workItemName, testWorkItemHandler);
		
		/**
		//add a freemarker resource type to be able to add the form to the classpath.
		if(formFileName!=null && ! formFileName.isEmpty()){
			KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			kbuilder.add(new ClassPathResource(formFileName), FtlResourceType.FTL);
			session.getKnowledgeBase().addKnowledgePackages(kbuilder.getKnowledgePackages());
		}
		**/
		
		ProcessInstance processInstance = session.startProcess(processId, props);
		
		
		// check if the process instance has started
		assertProcessInstanceActive(processInstance.getId(), session);
//		assertNodeTriggered(processInstance.getId(), nodes);
		
		// assert Human Task Parameters
		WorkItem workItem = testWorkItemHandler.getWorkItem();
		assertNotNull(workItem);
		assertEquals("Human Task", workItem.getName());
		assertEquals(workItem.getParameter("ActorId"), humanTaskParameters.get("ActorId"));
		assertEquals(workItem.getParameter("GroupId"), humanTaskParameters.get("GroupId"));
		assertEquals(workItem.getParameter("TaskName"), humanTaskParameters.get("TaskName"));
		
		HashMap<String, String> actualContent =  (HashMap<String, String>) humanTaskParameters.get("Content");
		HashMap<String, String> expectedContent =  (HashMap<String, String>) workItem.getParameter("Content");
		if(actualContent!= null){
			int contentSize = actualContent.size();
			for(int i =1; i<=contentSize; i++ ){
				String key = "content"+i;
				String contentValue = (String) contentParameters.get(key);
				assertEquals(expectedContent.get(contentValue), actualContent.get(contentValue));
			}
		}
		
		// notify the engine the humanTask has been executed
		session.getWorkItemManager().abortWorkItem(workItem.getId());
		
		//check if processInstance has been successfully executed
		assertProcessInstanceCompleted(processInstance.getId(), session);
		assertNodeTriggered(processInstance.getId(), nodes);
		
	}

	}

