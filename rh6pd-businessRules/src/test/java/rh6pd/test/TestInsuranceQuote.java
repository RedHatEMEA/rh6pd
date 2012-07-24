package rh6pd.test;

import org.junit.Test;

import rh6pd.businessRules.Person;
import rh6pd.businessRules.Vehicle;
 
public class TestInsuranceQuote extends AbstractProcessTest {
	@Test
	public void testInsuranceQuote() {  
		this.createNewSession("InsuranceQuote.rf", "InsuranceQuoteValidatePerson.rf", "InsuranceQuoteValidateVehicle.rf");
		 
		this.testProcess("6pd.businessRules.insuranceQuote", "GatewayStart", "GatewayFinish");
	}
} 
