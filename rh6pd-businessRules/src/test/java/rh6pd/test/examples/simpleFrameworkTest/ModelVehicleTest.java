package rh6pd.test.examples.simpleFrameworkTest;

import org.junit.Test;

import rh6pd.businessRules.Vehicle;

import junit.framework.Assert;


public class ModelVehicleTest {
	@Test 
	public void testGettersAndSetters() {
		Vehicle vehicle = new Vehicle("Ford", "Fiesta", 2.0);
		
		Assert.assertEquals("Ford", vehicle.getMake()); 
		Assert.assertEquals("Fiesta", vehicle.getModel());
		Assert.assertEquals(2.0, vehicle.getEngineSize());

		vehicle.setEngineSize(1.0);
		Assert.assertEquals(1.0, vehicle.getEngineSize());
		
		vehicle.setMake("Porsche");
		Assert.assertEquals("Porsche", vehicle.getMake());
		
		vehicle.setModel("911");
		Assert.assertEquals("911", vehicle.getModel());
	}
}
