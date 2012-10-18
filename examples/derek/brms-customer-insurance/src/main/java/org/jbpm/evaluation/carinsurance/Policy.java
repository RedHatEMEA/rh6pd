
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
  	private Date    policyDate   = new Date();
  	
  	// COMPREHENSIVE, FIRE_THEFT, THIRD_PARTY
  	private String  policyType           = "Policy: Not Set Yet"; 
    
	private int     policyTotalRisk      = 0;
	private int     policyDriverRisk     = 0;
	private int     policyCarRisk        = 0;
	private int     policyHistoryRisk    = 0;
	private int     policyAccidentRisk   = 0;

	private int     policyPrice          = 0;
	private int     policyDiscount       = 0;
	

	
	// Policy Date
	public Date getDate(){
		return policyDate;
	}
	public void setDate(Date policyDate){
		this.policyDate = policyDate;
	}

	// Policy Type
	public String getType(){
		return policyType;
	}
	public void setType(String policyType){
		this.policyType = policyType;
	}
	
	// This is the section which contains the various risks :-
	// driver risk, car risk and history risk, these will be used when we calculate the total risk
	// and determine the price.
	
	// Policy Total Risk
	public int getTotalRisk(){
		return policyTotalRisk;
	}
	public void setTotalRisk(int policyTotalRisk){
		this.policyTotalRisk = policyTotalRisk;
	}	
		
	// Policy Driver Risk
	public int getDriverRisk(){
		return policyDriverRisk;
	}
	public void setDriverRisk(int policyDriverRisk){
		this.policyDriverRisk = policyDriverRisk;
	}	
	
	// Policy Car Risk
	public int getCarRisk(){
		return policyCarRisk;
	}
	public void setCarRisk(int carRisk){
		this.policyCarRisk = carRisk;
	}	
	
	// Policy History Risk
	public int getHistoryRisk() 
	{
		return policyHistoryRisk;
	}
	public void setHistoryRisk(int policyHistoryRisk){
		this.policyHistoryRisk = policyHistoryRisk;
	}	
	
	// Policy Accident Risk
	public int getAccidentRisk(){
		return policyAccidentRisk;
	}
	public void setAccidentRisk(int policyAccidentRisk) {
		this.policyAccidentRisk = policyAccidentRisk;
	}	
	
	// Policy Price
	public int getPrice() 
	{
		return policyPrice;
	}
	public void setPrice(int policyPrice){
		this.policyPrice = policyPrice;
	}	

	// Policy Discount
	public int getDiscount(){
		return policyDiscount;
	}

	public void setDiscount(int policyDiscount){
		this.policyDiscount = policyDiscount;
	}



}
