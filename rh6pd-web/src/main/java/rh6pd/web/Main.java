package rh6pd.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import rh6pd.web.VehicleData;
import rh6pd.web.common.Car;
import rh6pd.web.common.Person;
import rh6pd.web.common.Vehicle;

@ManagedBean(name="main")
@SessionScoped
public class Main {
	
	private int random = 42;
	
	@ManagedProperty(value="#{vehicleData}")
	private VehicleData vehicleData;
	
	public static final void main(String[] args) { 
		
	System.out.println("Attempting to build KnowledgeBase");
		
		//Instantiate Class whilst running outside of Application Server
		//VehicleData.buildVehicleList();
		
		Vehicle car = new Vehicle("ford", "mustang", 4.0);
		Person person = new Person("Sam", 18, car, 200);
		
		// declare a hashmap for our properties and pass them to the process
		HashMap<String, Object> props = new HashMap<String, Object>(); 
		props.put("person", person); 
		props.put("car", car);
		
		try {
			
			KnowledgeBase kbase = readKnowledgeBase();
			
			//Build Statefull Knowledge Session
			StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
			
			ksession.insert(person);
			
			// start a new process instance
			ksession.startProcess("rh6pd.web.common.bpmn.insuranceQuote", props);
			ksession.fireAllRules(); 
			
			System.out.println("Successfully built KnowledgeBase");
			
		} catch (Exception e) {
			
			System.out.println("Unsuccessfully built KnowledgeBase");
			
			e.printStackTrace();
		}		
		
		
		for ( Car carMake : VehicleData.getVehicleInfo()) {
			
			System.out.println("Car Make: "  + carMake.getMake());
		}
		
		
	}
	  
	public void calculateRandomNumber() {
		this.random = 100; 
	}
	public int getRandom() {
		return random; 
	}
	
	protected static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("CalculatePremium.drl"), ResourceType.DRL); 
		kbuilder.add(ResourceFactory.newClassPathResource("sample.bpmn"), ResourceType.BPMN2);
		return kbuilder.newKnowledgeBase();

	}
	/**
	 * @return the vehicleData
	 */
	public VehicleData getVehicleData() {
		return vehicleData;
	}

	/**
	 * @param vehicleData the vehicleData to set
	 */
	public void setVehicleData(VehicleData vehicleData) {
		this.vehicleData = vehicleData;
	}
}   
    