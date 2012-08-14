package rh6pd.test;

import org.junit.Test;

import rh6pd.businessRules.Person;
import rh6pd.businessRules.Vehicle;
 
public class TestInsuranceQuote extends AbstractProcessTest {
	@Test
	public void testInsuranceQuote() {  
		this.createNewSession("rh6pd/businessRules/insuranceQuote/InsuranceQuote.rf", "rh6pd/businessRules/insuranceQuote/InsuranceQuoteValidatePerson.rf", "rh6pd/businessRules/insuranceQuote/InsuranceQuoteValidateVehicle.rf");
		 
		this.testProcess(null, null, "rh6pd.businessRules.insuranceQuote", "GatewayStart", "GatewayFinish");
	}
} 
