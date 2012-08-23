package rh6pd.test.examples.simpleFrameworkTest;

import java.io.IOException;

import org.junit.Test;

import rh6pd.businessRules.Person;
import rh6pd.businessRules.Vehicle;
import rh6pd.test.AbstractProcessTest;

public class TestSampleDecisionTable extends AbstractProcessTest{

	String processFileName = "SampleDecisionTable.bpmn";
	String processId = "com.redhat.sample.bpmn.decisionTableAndHumanTask";
	
	@Test
	public void testUnderageDriverCar() {
		this.createNewSession(processFileName);

		Vehicle vehicle = new Vehicle("Ford", "Fiesta", 1.0);
		Person person = new Person("Alice", 10, vehicle, 800);

		this.insertVar("person", person);
		this.insertVar("vehicle", vehicle);

		this.testProcess(null, null, processId,
				"StartProcess", "Underage");
	}

	@Test
	public void testLargeEngineSize() {
		printBpmnErrors(processFileName);
		this.createNewSession(processFileName);

		Vehicle vehicle = new Vehicle("ford", "Fiesta", 2.0);
		Person person = new Person("Alice", 20, vehicle, 800);

		this.insertVar("person", person);
		this.insertVar("vehicle", vehicle);

		this.testProcess(null, null, processId,
				"StartProcess", "Engine Size Fail");
	}

	@Test
	public void testWrongCarMake() {
		this.createNewSession(processFileName);
		Vehicle vehicle = new Vehicle("For", "Fiesta", 1.0);
		Person person = new Person("Alice", 20, vehicle, 800);
		this.insertVar("person", person);
		this.insertVar("vehicle", vehicle);
		this.testProcess(null, null, processId,
				"StartProcess", "Model Fail");
	}
	
	@Test
	public void testWithPremium() throws IOException{
		this.createNewSession(processFileName);
		Vehicle vehicle = new Vehicle("ford", "Fiesta", 1.0);
		Person person = new Person("Alice", 20, vehicle, 100);
		this.insertVar("person", person);
		this.insertVar("vehicle", vehicle);
		this.testProcess(null, "CalculatePremium.xls", processId,
				"StartProcess", "CalculatePremium", "GoodbyeMessage");
//	String ruleFile = this.convertDecisionTableToRuleFile("CalculatePremium.xls");
//	System.out.println("Content of RuleFile generated from Decision Table:  "+ruleFile);
	}

	
	@Test
	public void testWithoutPremium(){
		this.createNewSession(processFileName);
		Vehicle vehicle = new Vehicle("ford", "Fiesta", 1.0);
		Person person = new Person("Alice", 40, vehicle, 100);
		this.insertVar("person", person);
		this.insertVar("vehicle", vehicle);
		this.testProcess(null, "CalculatePremium.xls", processId,
				"StartProcess", "CalculatePremium", "GoodbyeMessage");
	}
	
}
