package com.redhat.test.simpleFrameworkTest;

import org.junit.Test;

import com.redhat.Person;
import com.redhat.Vehicle;
import com.redhat.test.AbstractProcessTest;

public class TestSampleBpmn extends AbstractProcessTest {
	@Test  
	public void testUnderageDriverCar() {
		this.createNewSession("simpleFrameworkTest/sample.bpmn");
		 
		Vehicle vehicle = new Vehicle("Ford", "Fiesta", 1.0);   
		Person person = new Person("Alice", 10, vehicle, 800);
		  
		this.insertVar("person", person); 
		this.insertVar("vehicle", vehicle); 
		
		this.testProcess("com.redhat.bpmn.insuranceQuote", "StartProcess", "Underage");
	}
	
	@Test 
	public void testLargeEngineSize() {
		this.createNewSession("simpleFrameworkTest/sample.bpmn");
		 
		Vehicle vehicle = new Vehicle("ford", "Fiesta", 2.0);   
		Person person = new Person("Alice", 20, vehicle, 800);
		  
		this.insertVar("person", person); 
		this.insertVar("vehicle", vehicle); 
		
		this.testProcess("com.redhat.bpmn.insuranceQuote", "StartProcess", "Engine Size Fail");
	}
}  
 