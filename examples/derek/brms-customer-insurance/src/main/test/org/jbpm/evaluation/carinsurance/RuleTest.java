package org.jbpm.evaluation.carinsurance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.rule.FactHandle;
import org.jbpm.task.TaskService;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.test.JbpmJUnitTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

public class RuleTest extends JbpmJUnitTestCase {
    public KnowledgeBase getKbase()   
    {    
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();                
        kbuilder.add(ResourceFactory.newClassPathResource("testCalcProcess.jbpm.rf"), ResourceType.BPMN2);
        kbuilder.add(ResourceFactory.newClassPathResource("calcDriverRisk.drl"),ResourceType.DRL); 
          
        return kbuilder.newKnowledgeBase();  
    }  
     
    @Test 
    public void testYoungDriver() {
    	StatefulKnowledgeSession ksession = createKnowledgeSession(getKbase());
    	Policy policy = new Policy();  
    	Driver driver = new Driver();  
    	driver.setAge(12);   
    	
    	HashMap<String, Object> props = new HashMap<String, Object>();
    	props.put("driver", driver);
    	props.put("policy", policy);
    	   
        ksession.insert(driver); 
        ksession.insert(policy);
    	  
    	ProcessInstance pi = ksession.startProcess("testCalcProcess", props);
        assertProcessInstanceActive(pi.getId(), ksession);
               
        ksession.fireAllRules();  

        assertNodeTriggered(pi.getId(), "Start", "End");
        assertProcessInstanceCompleted(pi.getId(), ksession);  
           
        assertEquals(999, driver.getCreditScore());
        assertEquals(999, policy.getDriverRisk());   
    } 
    
    @Test 
    public void testOldDriver() { 
    	StatefulKnowledgeSession ksession = createKnowledgeSession(getKbase());
    	Policy policy = new Policy();  
    	Driver driver = new Driver();    
    	driver.setAge(99);   
    	
    	HashMap<String, Object> props = new HashMap<String, Object>();
    	props.put("driver", driver);
    	props.put("policy", policy);
    	   
        //FactHandle fh1 = ksession.insert(alice);
    	ksession.insert(driver);  
        ksession.insert(policy); 
        //ksession.retract(fh1);
    	 
        ProcessInstance pi = ksession.startProcess("testCalcProcess", props);
        assertProcessInstanceActive(pi.getId(), ksession);
              
        ksession.fireAllRules();  

        assertNodeTriggered(pi.getId(), "Start", "End");
        assertProcessInstanceCompleted(pi.getId(), ksession);  
             
        assertEquals(0, driver.getCreditScore());
        assertEquals(0, policy.getDriverRisk());   
    } 
}