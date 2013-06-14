function notEmpty(elem){
	if (elem == null) {
		return false;
	}
	
	if(elem.value.length == 0){
		return false;
	}
	return true;
} 
  
function isNumeric(elem){
	var numericExpression = /^[0-9]+$/;
	if(elem.value.match(numericExpression)){
		return true;
	} else {
		return false;
	}
}

function isAlphabet(elem){
	var alphaExp = /^[a-zA-Z0-9\_ .-@]+$/;
	if(elem.value.match(alphaExp)){
		return true;
	} else { 
		return false;
	}
}

function isAlphanumeric(elem){
	var alphaExp = /^[a-zA-Z0-9\_ .-@]+$/;
	if(elem.value.match(alphaExp)){
		return true;
	} else {
		return false;
	}
}

function isFloat(elem){
	if(elem.value.indexOf(".") < 0){
 		return false;
	} else {
  		if(parseFloat(elem.value)) {
          return true;
      	} else {
          return false;
      	}
	}
}

function taskFormValidatorDriver() {
	var i=0;
	var myInputs = new Array();

	myInputs[i] = document.getElementById("Name");
	i++;

	myInputs[i] = document.getElementById("Street");
	i++;

	myInputs[i] = document.getElementById("City");
	i++;
	
	myInputs[i] = document.getElementById("Age");
	i++;
			
	
	var j=0;

	if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
		alert("Please enter valid Name");
		myInputs[j].focus();
		return false;
	}	
	j++;

	if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
		alert("Please enter valid Street");
		myInputs[j].focus();
		return false;
	}	 
	j++;

	if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
		alert("Please enter valid City");
		myInputs[j].focus();
		return false;
	}	 
	j++;
	   
	if(notEmpty(myInputs[j]) && !isAlphanumeric(myInputs[j])) {
		alert("Please enter valid Age");
		myInputs[j].focus();  
		return false;
	}	
	j++;
			
	return true;
}

function taskFormValidatorCar() {
	try {
		validateField('Make', false, true, true);
		validateField('Model', false, true, true);
		validateField('Reg', false, true, true);	
		
		return true;      
	} catch (e) {
		return false;
	}
} 

function highlightField(field, isStatusGood) {
	console.log(field, isStatusGood);  
	if (isStatusGood) {  
		field.css('border', '2px solid green');
	} else {
		field.css('border', '2px solid red');
	} 
}

this.validatedFields = [];

function tryEnableButton(fieldId) { 
	this.validatedFields[fieldId] = true;
	
	for (var i = 0; i < this.validatedFields.length; i++) {
		if (validatedFields[i] == false) {
			return;
		}
	}
	
	$('#button').removeAttr('disabled');
}

function disableButton(element) { 
	this.validatedFields[element] = false;
	$('#button').attr('disabled', 'disabled');
}

function validateField(fieldId, acceptsOnlyNumbers, acceptsLetters, acceptsSpaces) {
	var field = $('#' + fieldId);
	var validationError = null;
	 
	try {
		if (field.val() == '') { 
			highlightField(field, false);
			throw "Failed to validate field: " + fieldId; 
		}   
	} catch (err) {
		validationError = err;
	}
	
	if (validationError == null) {
		tryEnableButton(fieldId);
		return;
	} else {
		disableButton(fieldId);
		throw validationError;
	}
}
  
function softValidateField(fieldId, acceptsOnlyNumbers, acceptsLetters, acceptsSpaces) {
	try {
		validateField(fieldId, acceptsOnlyNumbers, acceptsLetters, acceptsSpaces);
	} catch (err) {
		console.log(err); 
		highlightField($('#' + fieldId), false);
		return;   
	}  
	   
	highlightField($('#' + fieldId), true);
}


function taskFormValidator() {} 