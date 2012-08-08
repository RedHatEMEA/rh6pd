package rh6pd.test.examples.humanTasks;

import java.util.HashMap;

import org.jbpm.process.workitem.wsht.WSHumanTaskHandler;
import org.junit.Test;

import rh6pd.test.AbstractProcessTest;

public class TestHumanTask extends AbstractProcessTest{
	@Test
	public void testSampleHumanTask() {
		this.createNewSession("humanTask.bpmn");
		
		//Test human task with processId and the WorkItem to be tested
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("ActorId", "mary,john");
		parameters.put("GroupId", "admin");
		this.testHumanTask("Sample", "Human Task", parameters, "StartProcess", "User Task");
		
	}

	

	
}
