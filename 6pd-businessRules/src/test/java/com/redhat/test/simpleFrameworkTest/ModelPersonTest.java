package com.redhat.test.simpleFrameworkTest;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.Person;
import com.redhat.Vehicle;

public class ModelPersonTest {
	@Test
	public void testGettersAndSetters() { 
		Vehicle vehicle = new Vehicle("Ford", "Fiesta", 9.0); 
		Person person = new Person("Alice", 1337, vehicle, 1);
		
		Assert.assertEquals("Alice", person.getName());
	}
}
