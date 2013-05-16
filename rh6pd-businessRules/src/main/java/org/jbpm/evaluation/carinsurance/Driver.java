package org.jbpm.evaluation.carinsurance;

import java.io.Serializable;

// This represents the driver who is applying for an insurance Policy.

public class Driver implements Serializable
{
    
	private String  driverName        = "Driver: Not Set Yet";
	private Integer driverAge         =  0;
	private String  driverSsn         = "Driver: Not Set Yet";	
	private String  driverLicence     = "Driver: Not Set Yet";
	private Integer driverCreditScore = 0;
	private String 	id = "";
	
	// Constructor
	public Driver () {}
	
	public Driver(String id) {
		this.id = id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	
	// Driver name
	public String getName() {
		return driverName;
	}
	public void setName(String driverName) {
		this.driverName = driverName;
	}
	
	// Driver Age
	public Integer getAge() {
		return driverAge;
	}
	public void setAge(Integer driverAge) {
		this.driverAge = driverAge;
	}
	
	// Driver Social Security Number 
	public String getSsn() {
		return driverSsn;
	}
	public void setSsn(String driverSsn) {
		this.driverSsn = driverSsn;
	}
	
	// Driver Licence Number
	public String getLicence() {
		return driverLicence;
	}
	public void setLicence(String driverLicence) {
		this.driverLicence = driverLicence;
	}
	
	// Driver Credit Score
	public int getCreditScore() {
		return driverCreditScore;
	}
	public void setCreditScore(Integer driverCreditScore) {
		this.driverCreditScore = driverCreditScore;
	}
	
	// Methods to support Hibernate persistence
	public boolean equals(final Object object) {
		if (object == this ) {
			return true;
		}
		
		if ( object == null || !(object instanceof Driver) ) {
			return false;
			
		}
		
		final Driver other = (Driver) object;
		
		return (   this.id.equals( other.getId() )  );
					
	}
	
	public int hashCode() {
		return this.id.hashCode();
	}
	
}
