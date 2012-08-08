package rh6pd.web.common;

public class Person {

	private String name;
	private int age;
	private Vehicle car;
	private double premium;

	public Person(String name, int age, Vehicle car, double premium) {
		this.name = name;
		this.age = age;
		this.car = car;
		this.premium = premium;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public int getAge() {
		return age;
	}
	
	public Vehicle getCar() {
		return car;
	}
	
	public void setCar(Vehicle car) {
		this.car = car;
	}

	public double getPremium() {
		return premium;
	}

	public void setPremium(double premium) {
		this.premium = premium;
	}		
}
