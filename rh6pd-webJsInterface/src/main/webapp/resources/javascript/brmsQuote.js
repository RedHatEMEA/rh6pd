function onFetchedDefinitionsSuccess(data, textStatus, request) {
	var output="<ul>";
	
	if (request.getResponseHeader("Content-Type") == "text/html") { 
		onFetchedDefinitionsError(request, "Response is HTML, not JSON", null);
		return;
	}
	
	for (var i in data.definitions) {
	    output+="<li><strong>Name:</strong> " + data.definitions[i].name + "<br><strong>Package:</strong> " + data.definitions[i].packageName+"</li>";
		action = "http://localhost:8080/business-central-server/rs/process/definition/" + data.definitions[i].id + "/new_instance";
		//document.getElementById("action").innerHTML=action;
		document.getElementById("placeholder").innerHTML=output;
		$('#button').removeAttr('disabled'); 
	}
	
	output+="</ul>";
}

function onFetchedDefinitionsError(request, status, error) {
	console.log(error);
    $("#content").html('<p>Caught error parsing intial JSON response from server. Have you logged into the jBPM Console? </p><p>The error was: ' + status + '</p>');
} 

function onPageLoad() {
	var url = "http://localhost:8080/business-central-server/rs/process/definitions";
	
	$.ajax({
		datatype: "json",
		url: url,
		data: null,
		success: onFetchedDefinitionsSuccess,
		error: onFetchedDefinitionsError
	});
}

function createFormAndSubmit(action) { 
	$.ajax({
		url: action,
		data: null,
		success: onProcessStartedSuccess,
		error: onProcessStartedError,
		type: "POST"
	}); 
} 
 
function onProcessStartedSuccess() {
	window.location = "tasks.html";	
}

function onProcessStartedError() {
    $("#content").html('<p>Caught error starting process.</p><p>The error was: ' + status + '</p>');
}
