package org.jbpm.evaluation.carinsurance;

import java.io.Serializable;

/**
 * This object represents a car in the  insurance policy.
 *
 */
public class Car implements Serializable 
{
    private static final long serialVersionUID = 1L;
	private String carMake    = "Car: Not Set Yet";
	private String carModel   = "Car: Not Set Yet";	
	private int    carYear    = 0;
	private String carRegMark = "Car: Not Set Yet" ;
	
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

}
