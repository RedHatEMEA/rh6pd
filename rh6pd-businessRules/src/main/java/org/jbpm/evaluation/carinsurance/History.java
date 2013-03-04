package org.jbpm.evaluation.carinsurance;

import java.io.Serializable;

/**
 * This represents the history of a driver who is applying for an insurance Policy.
 *
 */
public class History implements Serializable
{

	private Integer historyAccidents = 0;
	private Integer historyTickets   = 0;
	private String 	id;
	
	// Constructor
	public History () {}
	
	public History(String id) {
		this.id = id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	
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
	
	// Methods to support Hibernate persistence
	public boolean equals(final Object object) {
		if (object == this ) {
			return true;
		}
		
		if ( object == null || !(object instanceof History) ) {
			return false;
			
		}
		
		final History other = (History) object;
		
		return (   this.id.equals( other.getId() )  );
					
	}
	
	public int hashCode() {
		return this.id.hashCode();
	}
}
