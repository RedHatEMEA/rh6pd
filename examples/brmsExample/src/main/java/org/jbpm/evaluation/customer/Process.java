package org.jbpm.evaluation.customer;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.SystemEventListenerFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.task.AccessType;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.ContentData;
import org.jbpm.task.service.TaskClient;
import org.jbpm.task.service.mina.MinaTaskClientConnector;
import org.jbpm.task.service.mina.MinaTaskClientHandler;
import org.jbpm.task.service.responsehandlers.BlockingTaskOperationResponseHandler;
import org.jbpm.task.service.responsehandlers.BlockingTaskSummaryResponseHandler;

/**
 * This is a sample file to launch a process.
 */
public class Process {

	public static final void main(String[] args) throws Exception {
		// load up the knowledge base
		KnowledgeBase kbase = readKnowledgeBase();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		
		
		// start a new process instance 
		HashMap<String,Object> props = new HashMap<String,Object>();
		
		props.put("validRequest", true);
		Person person = new Person("1337", "James Read");
		person.setAge(50); 
		
		props.put("person", person);
		Request request =  new Request("request1");
		
		
		request.setPersonId(person.getId());
		request.setAmount(100);
		
		props.put("request",request);
		
		System.out.println("Debugging info");
		System.out.println("   person id: " + person.getId()); 
		System.out.println("   amount: " + request.getAmount());
		
		ksession.startProcess("org.jbpm.customer-evaluation", props);
		ksession.fireAllRules(); 
		
		
		
	} 

	private static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("financerules.drl"), ResourceType.DRL);
		kbuilder.add(ResourceFactory.newClassPathResource("customereval.bpmn2"), ResourceType.BPMN2);
		//kbuilder.add(ResourceFactory.newClassPathResource("Test.ftl"), ResourceType.); 
		return kbuilder.newKnowledgeBase();   
	}
	
}
