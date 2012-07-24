package rh6pd.test.examples.simpleFrameworkTest;

import junit.framework.Assert;

import org.junit.Test;

import rh6pd.businessRules.Person;
import rh6pd.businessRules.Vehicle;


public class ModelPersonTest {
	@Test
	public void testGettersAndSetters() { 
		Vehicle vehicle = new Vehicle("Ford", "Fiesta", 9.0); 
		Person person = new Person("Alice", 1337, vehicle, 1);
		
		Assert.assertEquals("Alice", person.getName());
	}
}
