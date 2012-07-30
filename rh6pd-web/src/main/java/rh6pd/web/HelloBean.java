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

import rh6pd.web.common.Person;
import rh6pd.web.common.Vehicle;

import java.io.Serializable;
import java.util.HashMap;
	 
@ManagedBean(name="helloBean")
@SessionScoped
public class HelloBean implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	 
	private String name;
	private int age;
	private String make;
	private String model;
	private double engine;
	
	private StatefulKnowledgeSession ksession;
		
	@PostConstruct
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
			
			// declare a hashmap for our properties and pass them to the process
			HashMap<String, Object> props = new HashMap<String, Object>(); 
			props.put("person", person); 
			props.put("car", car);
			
			ksession.insert(person);
			
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
