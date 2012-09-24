package org.jbpm.evaluation.carinsurance;

import java.io.Serializable;

/**
 * This represents the driver who is applying for an insurance Policy.
 *
 */
public class Driver implements Serializable
{
    private static final long serialVersionUID = 1L; 
	private String driverName = "Bill Smith";
	private Integer age = new Integer(0);
	private String ssn = "0000";	
	private String dlNumber = "0000";
	private Integer  creditScore = new Integer(0);

	
	// Driver name
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	
	// Driver Age
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
	// Driver Social Security Number 
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	
	// Driver Licence Number
	public String getDlNumber() {
		return dlNumber;
	}
	public void setDlNumber(String dlNumber) {
		this.dlNumber = dlNumber;
	}
	
	// Driver Credit Score
	public int getCreditScore() {
		return creditScore;
	}
	public void setCreditScore(Integer creditScore) {
		this.creditScore = creditScore;
	}
	
	
	
}
