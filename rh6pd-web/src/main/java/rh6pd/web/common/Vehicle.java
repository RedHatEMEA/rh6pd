package rh6pd.web.common;

public class Vehicle {

	private String make;
	private String model;
	private double engine;
	
	public Vehicle(String make, String model, double engine) {
		this.make = make;
		this.model = model;
		this.engine = engine;
	}
	
	public void setMake(String make) {
		this.make = make;
	} 
	 
	public String getMake() { 
		return make;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getModel() {
		return model;
	}

	public double getEngineSize() {
		return engine;
	}

	public void setEngineSize(double engine) {
		this.engine = engine;
	}
}
