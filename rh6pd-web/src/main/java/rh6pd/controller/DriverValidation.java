package rh6pd.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import rh6pd.model.Person;

@ManagedBean
@ViewScoped
public class DriverValidation implements Serializable {
	
	private static final long serialVersionUID = 1L;
	   
	private Person person;

    public DriverValidation() {
        person = new Person();
    }
    
    public String submitDriverDetails() {
    	
    	/* Insert some code here  to do any additional backend validation and persist  
    	 * the data to a datastore.
    	 * Also redirect code to the next step on the form needs to be added.
    	 */
    	
    	//Some noddy navigation rules, suggest using MyFaces Orchestra library to allow conversation scoped persisted context
    	
    	if (FacesContext.getCurrentInstance().isValidationFailed() == false ) { // To be replaced using a proper validation method!
    	
        FacesMessage message = new FacesMessage("Driver Details successful validated!");
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        return  "about-car.xhtml";
        
    	} else {
    		
    		//Validation failed for some reason!
    		FacesMessage message = new FacesMessage("Drivier Details unsuccessful validated! Please review!");
            FacesContext.getCurrentInstance().addMessage(null, message);
    		
    	return "about-car.xhtml";
    	
    	}
    }
	
    public Person getPerson() {
        return person;
    }  

}
