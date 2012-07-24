package rh6pd.web;

import javax.faces.bean.ManagedBean;

@ManagedBean(name="main")
public class Main {
	private int random = 42; 
	  
	public void calculateRandomNumber() {
		this.random = 100; 
	}
	public int getRandom() {
		return random; 
	}
}   
    