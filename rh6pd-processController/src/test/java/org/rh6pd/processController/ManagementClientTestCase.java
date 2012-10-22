package org.rh6pd.processController;

import java.util.List;

import org.apache.log4j.spi.LoggerFactory;
import org.jboss.bpm.console.client.model.ProcessDefinitionRef;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;

import rh6pd.processController.ManagementClient;
 
public class ManagementClientTestCase { 
	Logger log = org.slf4j.LoggerFactory.getLogger(ManagementClientTestCase.class);
	  
	@Test
	public void testGetAllDefinitions() throws Exception {
		ManagementClient brmsClient = new ManagementClient("admin", "admin");
		brmsClient.doLoginIfNecessary();
		
		List<ProcessDefinitionRef> definitions = brmsClient.getAllDefinitions();
		 
		log.debug("Number of defs: " + definitions.size());
		
		for (ProcessDefinitionRef r : definitions) { 
			log.debug("ref: " + r.getId());
		}  
		      
		Assert.assertFalse(definitions.isEmpty());   
	}
	 
	public void logHttpResult(ManagementClient brmsClient) { 
		log.debug("HTTP Response code: " + brmsClient.httpWrapper.lastResponse + " Content: " + brmsClient.httpWrapper.lastContent); 
	}
	  
	@Test
	@Ignore
	public void testStartInstance() throws Exception {
		ManagementClient brmsClient = new ManagementClient("admin", "admin");
		brmsClient.doLoginIfNecessary();
		 
		brmsClient.executeProcess("testCalcProcess");
 
		logHttpResult(brmsClient);
	} 
}  
