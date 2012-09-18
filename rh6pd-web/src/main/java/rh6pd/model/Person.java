package rh6pd.model;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Person {
	

	   private Long id;
	   
	   @NotNull(message = "Please enter your name")
	   @Size(min = 2, max = 25, message = "Firstname must be between 2 and 25 characters long!")
	   @Pattern(regexp = "[A-Za-z ]*", message = "must contain only letters and spaces")
	   private String firstName;
	   
	   @NotNull(message = "Surname must not be Empty")
	   @Size(min = 2, max = 25, message = "Surname must be between 2 and 25 characters long!")
	   @Pattern(regexp = "[A-Za-z ]*", message = "must contain only letters and spaces")
	   private String surName;
	   
	   @NotNull(message = "Please enter your Date of Birth: dd-MM-yyyy")
	   private Date dob;
	   
	   @NotNull(message = "Please enter your 1st Address line")
	   @Size(min = 1, max = 60,message = "Address Line 1 must be between 1 and 60 characters long")
	   @Pattern(regexp = "[A-Za-z0-9 ]*", message = "must contain only Alphanumeric characters")
	   private String add1;
	   
	   @NotNull(message = "Please enter your 2nd Address line")
	   @Size(min = 1, max = 60, message = "Address Line 2 must be between 1 and 60 characters long")
	   @Pattern(regexp = "[A-Za-z0-9 ]*", message = "must contain only Alphanumeric characters")
	   private String add2;	   
	   
	   @Size(min = 2, max = 50, message = "Adress Line 3 must be between 2 and 50 characters long")
	   @Pattern(regexp = "[A-Za-z ]*", message = "must contain only Alphabetical characters")
	   private String add3;
	   
	   @NotNull(message = "Please enter your home County")
	   @Size(min = 3, max = 50, message = "County must be between 3 and 50 characters long")
	   @Pattern(regexp = "[A-Za-z ]*", message = "must contain only Alphabetical characters") 
	   private String county;
	   
	   @NotNull(message = "Please enter your Post Code")
	   @Size(min = 3, max = 8, message = "Postcode must be between 4 and 8 characters long")
	   @Pattern(regexp = "[A-Za-z0-9 ]*", message = "must contain only Alphanumeric characters")
	   private String postCode;


	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getAdd2() {
		return add2;
	}

	public void setAdd2(String add2) {
		this.add2 = add2;
	}

	public String getAdd3() {
		return add3;
	}

	public void setAdd3(String add3) {
		this.add3 = add3;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public String getAdd1() {
		return add1;
	}

	public void setAdd1(String add1) {
		this.add1 = add1;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}	   

}
