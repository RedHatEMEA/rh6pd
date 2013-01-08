package org.train.tickets;

public class Customer {
	public String railcardType;  
	public double ticketPrice;
	
	public String getRailcard() {
		return this.railcardType;
	}
	
	public void setRailcard(String railcardType) {
		this.railcardType = railcardType;
	}
	
	public double getTicketPrice() {
		return this.ticketPrice;
	}
	  
	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
}
