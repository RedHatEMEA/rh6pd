package rh6pd.web;

import javax.faces.bean.ManagedBean;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;

@ManagedBean(name="main")
public class Main {
	private int random = 42; 
	
	public static final void main(String[] args) {
		
		System.out.println("Attempting to build KnowledgeBase");
		
		try {
			
			KnowledgeBase kbase = readKnowledgeBase();
			
			System.out.println("Successfully built KnowledgeBase");
			
		} catch (Exception e) {
			
			System.out.println("Unsuccessfully built KnowledgeBase");
			
			e.printStackTrace();
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
}   
    