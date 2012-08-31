package org.rh6pd.processController;

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
	
	public void testShowAllDeployments() throws Exception {
		ManagementClient c = new ManagementClient("admin", "admin");
		c.showAllDeployments();
		
		Assert.assertTrue(true); 
	}

}
