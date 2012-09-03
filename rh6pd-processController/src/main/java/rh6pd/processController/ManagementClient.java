package rh6pd.processController;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jboss.bpm.console.client.model.*;
import org.jboss.deployers.plugins.deployers.DeployerWrapper;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Wrapper; 
import java.util.HashMap;  
import java.util.HashSet;   
import java.util.List;
 
import com.google.gson.*; 
     
public class ManagementClient {     
	Logger log = org.slf4j.LoggerFactory.getLogger(ManagementClient.class);

	// Used for authentication.
	//private static final String authentication_form_url = "/business-central-server/rs/";
	//private static final String authentication_form_url   = "/business-central-server/rs/process/definition/org.jbpm.evaluation.carinsurance.quote/instances";
	private static final String authentication_form_url   = "/business-central-server/rs/process/definitions";
	private static final String authentication_submit_url = "/business-central-server/rs/identity/secure/j_security_check";
 	
	// Show AllDeployments.
	private static final String deployment_url = "/business-central-server/rs/engine/deployments";
	
	// 
		 
	// Show Historic Process Instance.
	private static final String history_search_url = "/business-central-server/rs/process/definition/";

	// Show Tasks.
	private static final String tasks_url = "/business-central-server/rs/tasks/admin/participation";
	
	//private static final String execute_task_url = "/business-central-server/rs/process/definition/org.jbpm.evaluation.carinsurance.quote/new_instance";
	private static final String execute_process_url = "/business-central-server/rs/engine/job/PROCESS/execute";
	  
	// Set user name and password. 
	private String username = "admin";
	private String password = "admin";
	
	public ManagementClient(String username, String password) 
	{ 
		this.username = username; 
		this.password = password;
		this.httpWrapper = new HttpMethodWrapper();
	}

	public void executeProcess(String processId) throws Exception 
	{
		String result = httpWrapper.httpPost(execute_process_url.replace("PROCESS", processId));
		 
		log.debug("Result of executeTask(): "+result);  
	}
	
	private final String jobs_url = "/business-central-server/rs/engine/jobs";
	
	public void showAllJobs() throws Exception { 
		String result = this.httpWrapper.httpGet(jobs_url);
		 
		log.debug("list of jobs: " + result);
	}
	
	public void showAllDeployments() throws Exception 
	{
		String result = this.httpWrapper.httpGet(deployment_url);
		
		System.out.println("Result of showAllDeployments: "+ result);

		//System.out.println("Marshall the Json data into java class.");
// 
//		Gson gson = new Gson();
//
//		DeploymentRefWrapper wrapper = gson.fromJson(result,
//				DeploymentRefWrapper.class);
//
//		for (DeploymentRef ref : wrapper.getDeployments()) {
//			System.out.println("deployment name is: " + ref.getName());
//		}
	}    
 
	public List<ProcessDefinitionRef> getAllDefinitions() throws Exception 
	{  
		String result = this.httpWrapper.httpGet("/business-central-server/rs/process/definitions"); 
		   
		log.debug("Completed request to show all definitions"); 
		 
		Gson gson = new Gson();
		ProcessDefinitionRefWrapper wrapper = gson.fromJson(result,ProcessDefinitionRefWrapper.class);
 
		return wrapper.getDefinitions();   
	}
    
	public List<ProcessInstanceRef> getProcessInstances(String processName) throws Exception 
	{ 
		String result = this.httpWrapper.httpGet("/business-central-server/rs/process/definition/{id}/instances".replace("{id}", processName));
  
		Gson gson = new Gson();
		ProcessInstanceRefWrapper wrapper = gson.fromJson(result, ProcessInstanceRefWrapper.class);
  
		return wrapper.getInstances(); 
	}  

	public void getHistoricProcessInstance() throws Exception 
	{
		String processId = "{http://www.jboss.org/bpel/examples}HelloGoodbye-1";
		// String encodedId = ModelAdaptor.encodeId(processId);
		String processDefinition = "org.jbpm.evaluation.carinsurance";

		String status = "COMPLETED";

		Long starttime = new java.util.Date(103, 1, 1).getTime();
		Long endtime = new java.util.Date().getTime();   
 
		// /business-central-server/rs/process/definition/history/org.jbpm.evaluation.carinsurance.quote/nodeInfo
		String search_url = history_search_url + processDefinition + "/nodeInfo";
		
		//		+ "/instances?status=" + status + "&starttime=" + starttime 
		//		+ "&endtime=" + endtime; 

		log.debug("Get historic process instances from process definition of : " + processId);
  
		String result = this.httpWrapper.httpGet(search_url); 

		Gson gson = new Gson();
		HistoryProcessInstanceRefWrapper wrapper = gson.fromJson(result,
				HistoryProcessInstanceRefWrapper.class);    
 
		for (HistoryProcessInstanceRef ref : wrapper.getDefinitions()) {
			log.debug("historic instance id is: "
					+ ref.getProcessInstanceId() + " definition key is: "
					+ URLDecoder.decode(ref.getProcessDefinitionId(), "UTF-8"));
		}

	}

	private boolean authenticated = false;

	public void doLoginIfNecessary() 
	{
		if (!authenticated) {  
			log.info("Need to login, fetching login form");
			 
			if (httpWrapper.getState() == null) {   // (we have no cookies, FIXME)
				getLoginForm();   
			}   

			submitLoginForm(); 
		}
	}

	public HttpMethodWrapper httpWrapper;
 
	private void getLoginForm() 
	{
		try { 
			this.httpWrapper.httpGet(authentication_form_url);
			
			// set scope.
			//httpclient.getParams().setAuthenticationPreemptive(true);
			//httpClient.getState().setCredentials(new AuthScope("localhost", 8080, AuthScope.ANY_REALM), defaultcreds);
			
			Cookie c = httpWrapper.getState().getCookies()[0];
			c.setPathAttributeSpecified(false);  
			c.setDomainAttributeSpecified(false); 
			c.setVersion(-1); 
			   
			log.debug("My cookie is (should include JSESSIONID): " + c);
		} catch (Exception e) { 
			System.out.println(e);
		}
	}
 
	private void submitLoginForm() {       
		log.debug("submiting Login Form"); 
		 
		log.debug("How many cookies do I have?: " + this.httpWrapper.getState().getCookies());
	
		authenticated = true; // FIXME 
		// FIXME - This is not using HTTP Wrapper 
		
		//HashMap<String, String> params = new HashMap<String, String>(); 
		//params.put("j_username", this.username);
		//params.put("j_password", this.password);    

		//HttpClient httpclient = new HttpClient(); 
		//PostMethod authMethod = new PostMethod(httpWrapper.getBaseUrl() + authentication_submit_url);
		
		//Credentials defaultcreds = new UsernamePasswordCredentials("admin", "admin");  
		
		// set cookie. 
		//httpclient.getState().addCookie(cookie);  
		
		// set scope. 
		//httpclient.getParams().setAuthenticationPreemptive(true);
		//httpclient.getState().setCredentials(new AuthScope("localhost", 8080, AuthScope.ANY_REALM), defaultcreds);
		
		NameValuePair[] data = { 
				new NameValuePair("j_username", username),
				new NameValuePair("j_password", password) 
		};
   
		//authMethod.setRequestBody(data); 
		 
		httpWrapper.httpPost(authentication_submit_url, data);
 		 	  
		log.debug("auth content result: " + httpWrapper.lastContent); 
	}
}
