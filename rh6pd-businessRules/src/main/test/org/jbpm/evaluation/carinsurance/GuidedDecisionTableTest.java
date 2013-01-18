package org.jbpm.evaluation.carinsurance;

import java.util.HashMap;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.test.JbpmJUnitTestCase;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class GuidedDecisionTableTest extends JbpmJUnitTestCase {
    public KnowledgeBase getKbase()   
    {    
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();                
        kbuilder.add(ResourceFactory.newClassPathResource("testDecisionTable.rf"), ResourceType.BPMN2);
        kbuilder.add(ResourceFactory.newClassPathResource("CalcDriverRisk3.gdst"),ResourceType.XDRL);  
           
        return kbuilder.newKnowledgeBase();   
    }  

    @Test
    public void testMagic() {
    	StatefulKnowledgeSession ksession = createKnowledgeSession(getKbase());

    	Policy policy = new Policy();  
    	Driver alice = new Driver();  
    	alice.setAge(10);   
    	 
    	HashMap<String, Object> props = new HashMap<String, Object>();
    	props.put("driver", alice);
    	props.put("policy", policy);
    	   
        ksession.insert(alice); 
        ksession.insert(policy);
    	 
    	ProcessInstance pi = ksession.startProcess("testDecisionTable", props);
        assertProcessInstanceActive(pi.getId(), ksession);   
              
        ksession.fireAllRules();   

        assertNodeTriggered(pi.getId(), "Start", "End");
        //assertEquals(10, policy.getDriverRisk());
        assertProcessInstanceCompleted(pi.getId(), ksession);
        
    }
}
