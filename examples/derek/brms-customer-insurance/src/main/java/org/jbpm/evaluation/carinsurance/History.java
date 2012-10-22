package org.jbpm.evaluation.carinsurance;

/**
 * This represents the history of a driver who is applying for an insurance Policy.
 *
 */
public class History {

	
	private Integer historyAccidents = 0;
	private Integer historyTickets   = 0;
	
	// Driver Accidents 
	public Integer getAccidents() {
		return historyAccidents;
	}
	public void setAccidents(Integer historyAccidents) {
		this.historyAccidents = historyAccidents;
	}
	
	// Driver Tickets
	public Integer getTickets() {
		return historyTickets;
	}
	public void setTickets(Integer historyTickets) {
		this.historyTickets = historyTickets;
	}
	
}
