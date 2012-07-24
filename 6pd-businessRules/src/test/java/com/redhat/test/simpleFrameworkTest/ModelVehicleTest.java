package com.redhat.test.simpleFrameworkTest;

import org.junit.Test;

import junit.framework.Assert;

import com.redhat.Vehicle;

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
