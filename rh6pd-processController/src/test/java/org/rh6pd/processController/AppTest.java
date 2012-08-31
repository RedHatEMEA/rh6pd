package org.rh6pd.processController;

import java.util.List;

import org.apache.log4j.spi.LoggerFactory;
import org.jboss.bpm.console.client.model.ProcessDefinitionRef;
import org.slf4j.Logger;

import rh6pd.processController.ManagementClient;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite; 
 
/**
 * Unit test for simple App.
 */ 
public class AppTest  
    extends TestCase  
{ 
	Logger log = org.slf4j.LoggerFactory.getLogger(AppTest.class);
	
	public void testShowAllDeployments() throws Exception {
		log.error("test"); 
		
		ManagementClient brmsClient = new ManagementClient("admin", "admin");
		brmsClient.doLoginIfNecessary();
		
		List<ProcessDefinitionRef> definitions = brmsClient.getAllDefinitions();
		 
		log.debug("Number of defs: " + definitions.size());
		   
		Assert.assertTrue(true); 
	}

}
