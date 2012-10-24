package rh6pd.web;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jboss.bpm.console.client.model.ProcessDefinitionRef;

import rh6pd.processController.ManagementClient;
import rh6pd.web.common.Car;
import rh6pd.web.common.Person;
import rh6pd.web.common.Vehicle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
	 
@ManagedBean(name="helloBean")
@SessionScoped
public class HelloBean implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	 
	private String name;
	private int age;
	private String make;
	private String model;
	private double engine;
	private List<String> brmsDefinitions;

	private StatefulKnowledgeSession ksession;
		
	//@PostConstruct
	public void init() {
		System.out.println("DEBUG TEST: Post Construct Fired");
		
		KnowledgeBase kbase;
		try {
			kbase = readKnowledgeBase();
			
			//Build Statefull Knowledge Session
			ksession = kbase.newStatefulKnowledgeSession();
		
			System.out.println("DEBUG TEST: Knowledgebase Succesfully Built");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("DEBUG TEST: Knowledgebase Un-Succesfully Built");
			e.printStackTrace();
		}		
	}
	
	
	@PostConstruct
	public void testManagementClient(){
		
		System.out.println("Running BRMS Login");
		ManagementClient MC = new ManagementClient("admin", "admin");
		
		try {

			MC.doLoginIfNecessary();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		System.out.println("Great Success, Management Client Loaded!!!");
	}
	
	public void testHelloWorldInstance() throws Exception {
		
		ManagementClient brmsClient = new ManagementClient("admin", "admin");
		brmsClient.doLoginIfNecessary();
		 
		brmsClient.executeProcess("helloworld");

	} 
	
	public List<String> testBRMS(){
		
		ManagementClient MC = new ManagementClient("admin", "admin");
		MC.doLoginIfNecessary();
		
		brmsDefinitions = new ArrayList<String>();
		try {
			 for (ProcessDefinitionRef ref : MC.getAllDefinitions()) {
					 		 
			 brmsDefinitions.add(new String(ref.toString()));
			 
			 }
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return brmsDefinitions;
	}
	 
	public String getName() {
	   return name;
	}
		
	public void setName(String name) {
		this.name = name;
	}
		
	public String getSayWelcome(){
		//check if null?
		if("".equals(name) || name ==null){
			return "";
		}else{
			
			Vehicle car = new Vehicle(make, model, engine);
			Person person = new Person(name, age, car, 200);
			
			ArrayList<Car> motors = new ArrayList<Car>();
			motors.add(new Car("ford", "fiesta", 2.0));  
			motors.add(new Car("audi", "a5", 2.0));  
			motors.add(new Car("bmw", "3 Series", 2.0));  
			motors.add(new Car("volvo", "d90", 3.0));  
			motors.add(new Car("nissan", "350z", 2.5));				
			
			// declare a hashmap for our properties and pass them to the process
			HashMap<String, Object> props = new HashMap<String, Object>(); 
			props.put("person", person); 
			props.put("car", car);	
			props.put("motors", motors);	
			
			if (motors.contains(person.getCar())){
				System.out.println("Motors Contains: " + person.getCar().getMake().toString());
			} else {
				System.out.println("Not Found! : " + person.getCar().getMake());
			}
			
			ksession.insert(person);
			ksession.insert(motors);
			
			// start a new process instance
			ksession.startProcess("rh6pd.web.common.bpmn.insuranceQuote", props);
			ksession.fireAllRules(); 
			
			return "Details successfully submited to brms: " + name + " : " + age + " : " + make + " : " + model +
					"Check console for details";
		}
		   
	}
	
	protected static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("CalculatePremium.drl"), ResourceType.DRL); 
		kbuilder.add(ResourceFactory.newClassPathResource("sample.bpmn"), ResourceType.BPMN2);
		return kbuilder.newKnowledgeBase();

	}
	
	
	public List<String> getBrmsDefinitions() {
		return brmsDefinitions;
	}


	public void setBrmsDefinitions(List<String> brmsDefinitions) {
		this.brmsDefinitions = brmsDefinitions;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * @param make the make to set
	 */
	public void setMake(String make) {
		this.make = make;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the engine
	 */
	public double getEngine() {
		return engine;
	}

	/**
	 * @param engine the engine to set
	 */
	public void setEngine(double engine) {
		this.engine = engine;
	}
}
