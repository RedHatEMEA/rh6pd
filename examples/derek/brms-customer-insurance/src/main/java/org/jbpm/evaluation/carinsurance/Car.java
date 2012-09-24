package org.jbpm.evaluation.carinsurance;

import java.io.Serializable;

/**
 * This object represents a car in the  insurance policy.
 *
 */
public class Car implements Serializable
{
    private static final long serialVersionUID = 1L;
	private String carMake ="FORD";
	private String model ="MONDEO";	
	private int vehicleYear = new Integer(0);
	private String carReg ="BOTTOM CAR" ;
	
	// Make of car
	public String getCarMake() {
		return carMake;
	}
	public void setCarMake(String carMake) {
		this.carMake = carMake;
	}
	
	
	// Model of car
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	
	// Year of car
	public int getVehicleYear() {
		return vehicleYear;
	}

	public void setVehicleYear(int vehicleYear) {
		this.vehicleYear = vehicleYear;
	}
	
	// Registration of the Car on the Policy
	public String getCarReg() {
			return carReg;
	}
	
	public void setCarReg(String driverName) {
		this.carReg = driverName;
	}



}
