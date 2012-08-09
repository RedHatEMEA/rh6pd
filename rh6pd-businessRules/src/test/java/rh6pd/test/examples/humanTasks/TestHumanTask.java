package rh6pd.test.examples.humanTasks;

import java.util.HashMap;

import org.drools.io.impl.ClassPathResource;
import org.drools.io.impl.DescrResource;
import org.jbpm.process.workitem.wsht.WSHumanTaskHandler;
import org.junit.Test;

import rh6pd.businessRules.Person;
import rh6pd.businessRules.Vehicle;
import rh6pd.test.AbstractProcessTest;

public class TestHumanTask extends AbstractProcessTest{
	@Test
	public void testSampleHumanTask() {
		printBpmnErrors("humanTask.bpmn");
		this.createNewSession("humanTask.bpmn");
		
		String form = "ProcessCustomer.ftl";
		
		//Test human task with processId and the WorkItem to be tested
		
		Vehicle vehicle = new Vehicle("ford", "Fiesta", 1.0);
		Person person = new Person("Alice", 20, vehicle, 800);
		this.insertVar("person", person);
		
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ActorId", "mary,john");
		parameters.put("GroupId", "admin");
		parameters.put("TaskName", "ProcessCustomer");
		
		// Test Content using variables from process
		HashMap<String, String> content = new HashMap<String, String>();
		content.put("name", person.getName());
		
		HashMap<String, String> contentMapping = new HashMap<String, String>();
		contentMapping.put("content1", "name");
		
		parameters.put("Content", content);
		
		
		this.testHumanTask(form, "Sample", "Human Task", parameters, contentMapping, "StartProcess", "Process Started", "Fill Customer Form", "Process Ended");

	}

	

	
}
