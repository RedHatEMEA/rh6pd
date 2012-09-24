package org.jbpm.evaluation.carinsurance;

/**
 * This represents the history of a driver who is applying for an insurance Policy.
 *
 */
public class History {

	
	private Integer numberOfAccidents = new Integer(0);
	private Integer numberOfTickets = new Integer(0);
	
	// Driver Accidents 
	public Integer getNumberOfAccidents() {
		return numberOfAccidents;
	}
	public void setNumberOfAccidents(Integer numberOfAccidents) {
		this.numberOfAccidents = numberOfAccidents;
	}
	
	// Driver Tickets
	public Integer getNumberOfTickets() {
		return numberOfTickets;
	}
	public void setNumberOfTickets(Integer numberOfTickets) {
		this.numberOfTickets = numberOfTickets;
	}
	
}
