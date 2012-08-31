package rh6pd.processController;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jboss.bpm.console.client.model.*;
import org.jboss.deployers.plugins.deployers.DeployerWrapper;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Wrapper; 
import java.util.HashMap;  
import java.util.HashSet;   
 
import com.google.gson.*;
   
public class ManagementClient {  
	private static final Logger log = Logger.getLogger(ManagementClient.class);

	// Used for authentication.
	//private static final String authentication_form_url = "/business-central-server/rs/";
	//private static final String authentication_form_url   = "/business-central-server/rs/process/definition/org.jbpm.evaluation.carinsurance.quote/instances";
	private static final String authentication_form_url   = "/business-central-server/rs/process/definition/org.jbpm.evaluation.carinsurance.quote/new_instance";
	private static final String authentication_submit_url = "/business-central-server/rs/identity/secure/j_security_check";
	
	// Show AllDeployments.
	private static final String deployment_url = "/business-central-server/rs/engine/deployments";
	
	// Show AllDefinitions.  - Working
	private static final String definitions_url = "/business-central-server/rs/process/definitions";
	
	// Show Active Process Instance.
	private static final String instance_url = "/business-central-server/rs/process/definition/org.jbpm.evaluation.carinsurance.quote/instances";
	 
	// Show Historic Process Instance.
	private static final String history_search_url = "/business-central-server/rs/process/definition/";

	// Show Tasks.
	private static final String tasks_url = "/business-central-server/rs/tasks/admin/participation";
	
	//private static final String execute_task_url = "/business-central-server/rs/process/definition/org.jbpm.evaluation.carinsurance.quote/new_instance";
	private static final String execute_task_url = "//business-central-server/rs/form/process/org.jbpm.evaluation.carinsurance.quote/complete";
	
	
	// Set user name and password. 
	private String username = "admin";
	private String password = "admin";
	
	public ManagementClient(String u, String p) 
	{ 
		System.out.println("U=" + u + " P=" + p);
		username = u;
		password = p;
		this.httpWrapper = new HttpMethodWrapper();
		
	}

	public void executeTask() throws Exception 
	{
		System.out.println("**** EXECUTE TASK ****");
		
		String result = httpWrapper.httpGet(execute_task_url); 
		System.out.println("Result of executeTask(): "+result);

		//System.out.println("Marshall the Json data into java class.");

		//Gson gson = new Gson();

		//DeploymentRefWrapper wrapper = gson.fromJson(result,
		//		DeploymentRefWrapper.class);
		//
		//for (DeploymentRef ref : wrapper.getDeployments()) {
		//	System.out.println("deployment name is: " + ref.getName());
		//}
		System.out.println("******************************");
	}
	
	
	public void showAllDeployments() throws Exception 
	{
		log.debug("SHOW ALL DEPLOYMENTS ****"); 
		
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
		System.out.println("******************************");
	}

	public void showAllDefinitions() throws Exception 
	{
		System.out.println("**** SHOW ALL DEFINITIONS ****");
		
		String result = this.httpWrapper.httpGet(definitions_url);

		//System.out.println("Marshall the Json data into java class.");

//		Gson gson = new Gson();
//		ProcessDefinitionRefWrapper wrapper = gson.fromJson(result,ProcessDefinitionRefWrapper.class);
//
//		for (ProcessDefinitionRef ref : wrapper.getDefinitions()) {
//			System.out.println("process name is: " + ref.getName());
//		}
//		System.out.println("******************************");
		System.out.println("Process Definitions currently on server: "+result);
	}

	public void getActiveProcessInstance() throws Exception 
	{
		System.out.println("**** SHOW ALL ACTIVE PROCESS INSTANCES ****");
		
		
		String result = this.httpWrapper.httpGet(instance_url);

		System.out.println("-----------------------------");
		System.out.println("Marshall the Json data into java class.");
		
		Gson gson = new Gson();
		//ProcessInstanceRefWrapper wrapper = gson.DateTypeAdapterfromJson(result, ProcessInstanceRefWrapper.class);
//
		//for (ProcessInstanceRef ref : wrapper.getInstances()) 
		//{
		//	System.out.println("instance id is: " + ref.getId() + " definition key is: " + (ref.getDefinitionId()));
		//}
		System.out.println("******************************");
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

		System.out.println("-----------------------------");
		System.out.println("Get historic process instances from process definition of : " + processId);

		String result = this.httpWrapper.httpGet(search_url);
		System.out.println("-----------------------------");
		System.out.println("Marshall the Json data into java class.");

		Gson gson = new Gson();
		HistoryProcessInstanceRefWrapper wrapper = gson.fromJson(result,
				HistoryProcessInstanceRefWrapper.class);

		for (HistoryProcessInstanceRef ref : wrapper.getDefinitions()) {
			System.out.println("historic instance id is: "
					+ ref.getProcessInstanceId() + " definition key is: "
					+ URLDecoder.decode(ref.getProcessDefinitionId(), "UTF-8"));
		}

	}

	private boolean authenticated = false;

	private void doLoginIfNecessary() 
	{
		if (!authenticated) {
			System.out.println("Need to login, fetching login form");
			
			if (cookie == null) {
				getLoginForm();
			}

			submitLoginForm();
		}
	}

	private Cookie cookie;
	public HttpMethodWrapper httpWrapper;

	private void getLoginForm() 
	{
		try { 
			this.httpWrapper.httpGet(authentication_form_url);
			Cookie cookie = httpWrapper.getState().getCookies()[0]; 
			
			httpWrapper.setCookie(cookie);  
			// set scope.
			//httpclient.getParams().setAuthenticationPreemptive(true);
			//httpClient.getState().setCredentials(new AuthScope("localhost", 8080, AuthScope.ANY_REALM), defaultcreds);
			
			System.out.println("My cookie is (should include JSESSIONID): " + cookie);

			this.cookie = cookie;
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void submitLoginForm() 
	{
		authenticated = true; // FIXME
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("j_username", "admin");
		params.put("j_password", "admin");
		
		httpWrapper.httpPost(authentication_form_url, params); 

		
		HttpClient httpclient = new HttpClient();
		PostMethod authMethod = new PostMethod(authentication_submit_url);
		

		Credentials defaultcreds = new UsernamePasswordCredentials("admin", "admin");
		
		// set cookie.
		httpclient.getState().addCookie(cookie);
		
		// set scope.
		//httpclient.getParams().setAuthenticationPreemptive(true);
		httpclient.getState().setCredentials(new AuthScope("localhost", 8080, AuthScope.ANY_REALM), defaultcreds);
		
		NameValuePair[] data = { 
				new NameValuePair("j_username", username),
				new NameValuePair("j_password", password) 
		};

		authMethod.setRequestBody(data);
		 
		try {
			int authResult = httpclient.executeMethod(authMethod);
			System.out.println("HTTP result when attemped to authenticate (should be 200): " + authResult);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			authMethod.releaseConnection();
		}
	}

	public static void main(String[] args) throws Exception 
	{
		ManagementClient client = new ManagementClient("admin", "admin");
		client.doLoginIfNecessary();
		client.executeTask();
		client.showAllDeployments();
		client.getActiveProcessInstance(); 
		client.showAllDefinitions(); //Working
	//client.getHistoricProcessInstance();//Cannot work for now until we get to be able to see deployed processes in /business-central and subsequently /business-central-server consoles
	}
}
