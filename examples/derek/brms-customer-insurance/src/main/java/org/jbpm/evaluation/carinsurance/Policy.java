
package org.jbpm.evaluation.carinsurance;

import java.io.Serializable;
import java.util.Date;

/**
 * This represents a policy that a driver is applying for. 
 * 
 */
public class Policy implements Serializable
{
    private static final long serialVersionUID = 1L;
  	private Date requestDate   = new Date();
    private String policyType  = "AUTO"; // COMPREHENSIVE, FIRE_THEFT, THIRD_PARTY
    
	private int totalRisk      = 0;
	private int driverRisk     = 0;
	private int carRisk        = 0;
	private int historyRisk    = 0;
	
	private int price          = 0;
	private int priceDiscount  = 0;
	private String carReg      = "BOTTOM CAR";
	
	private Driver driver      = new Driver();
	
	// Date
	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	// Policy Type
	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}
	
	// This is the section which contains the various risks :-
	// driver risk, car risk and history risk, these will be used when we calculate the total risk
	// and determine the price.
	
	// Policy Total Risk
	public int getTotalRisk() 
	{
		return totalRisk;
	}
	public void setTotalRisk(int totalRisk) 
	{
		this.totalRisk = totalRisk;
	}	
		
	// Policy Driver Risk
	public int getDriverRisk() 
	{
		return driverRisk;
	}
	public void setDriverRisk(int driverRisk) 
	{
		this.driverRisk = driverRisk;
	}	
	
	// Policy Car Risk
	public int getCarRisk() 
	{
		return carRisk;
	}
	public void setCarRisk(int carRisk) 
	{
		this.carRisk = carRisk;
	}	
	
	// Policy History Risk
	public int getHistoryRisk() 
	{
		return historyRisk;
	}
	public void setHistoryRisk(int historyRisk) 
	{
		this.historyRisk = historyRisk;
	}	
	
	
	
	// Policy Price
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}	

	
	// Policy Discount
	public int getPriceDiscount() {
		return priceDiscount;
	}

	public void setPriceDiscount(int priceDiscount) {
		this.priceDiscount = priceDiscount;
	}

	// Policy Named Driver
	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	// Registration of the Car on the Policy
	public String getCarReg() {
		return carReg;
	}
	public void setCarReg(String driverName) {
		this.carReg = driverName;
	}

	
}
