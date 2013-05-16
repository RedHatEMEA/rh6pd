package org.jbpm.evaluation.carinsurance;

import java.io.Serializable;

/**
 * This object represents a car in the insurance policy.
 *
 */
public class Car implements Serializable 
{
    
	private String carMake    = "Car: Not Set Yet";
	private String carModel   = "Car: Not Set Yet";	
	private int    carYear    = 0;
	private String carRegMark = "Car: Not Set Yet" ;
	private String id = "";
	
	// Constructor
	public Car () {}
	
	public Car(String id) {
		this.id = id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	
	// Make of car
	public String getMake() {
		return carMake;
	}
	public void setMake(String carMake) {
		this.carMake = carMake;
	}
	
	// Model of car
	public String getModel() {
		return carModel;
	}
	public void setModel(String carModel) {
		this.carModel = carModel;
	}
	
	// Year of car
	public int getYear() {
		return carYear;
	}
	public void setYear(int carYear) {
		this.carYear = carYear;
	}
	
	// Registration mark of the car.
	public String getReg() {
			return carRegMark;
	}
	public void setReg(String carRegMark) {
		this.carRegMark = carRegMark;
	}
	
	// Methods to support Hibernate persistence
	public boolean equals(final Object object) {
		if (object == this ) {
			return true;
		}
		
		if ( object == null || !(object instanceof Car) ) {
			return false;
			
		}
		
		final Car other = (Car) object;
		
		return (   this.id.equals( other.getId() )  );
					
	}
	
	public int hashCode() {
		return this.id.hashCode();
	}

}
