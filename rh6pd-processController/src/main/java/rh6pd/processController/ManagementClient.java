package rh6pd.processController;

import java.net.URLDecoder; 
import java.util.List;
import java.util.Vector;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.NameValuePair;
import org.jboss.bpm.console.client.model.HistoryProcessInstanceRef;
import org.jboss.bpm.console.client.model.HistoryProcessInstanceRefWrapper;
import org.jboss.bpm.console.client.model.ProcessDefinitionRef;
import org.jboss.bpm.console.client.model.ProcessDefinitionRefWrapper;
import org.jboss.bpm.console.client.model.ProcessInstanceRef;
import org.jboss.bpm.console.client.model.ProcessInstanceRefWrapper;
import org.slf4j.Logger;
 
import com.google.gson.Gson;

public class ManagementClient {
	Logger log = org.slf4j.LoggerFactory.getLogger(ManagementClient.class);
	private static final String authentication_form_url = "/business-central-server/rs/process/definitions";
	private static final String authentication_submit_url = "/business-central-server/rs/identity/secure/j_security_check";

	private static final String deployment_url = "/business-central-server/rs/engine/deployments";
	private static final String history_search_url = "/business-central-server/rs/process/definition/";

	// private static final String execute_task_url =
	// "/business-central-server/rs/process/definition/org.jbpm.evaluation.carinsurance.quote/new_instance";
	private static final String execute_process_url = "/business-central-server/rs/engine/job/PROCESS/execute";
	private static final String render_human_task_url = "/business-central-server/rs/form/task/TASKID/render"; 

	private String username = "admin"; 
	private String password = "admin";
 
	private final String jobs_url = "/business-central-server/rs/engine/jobs";

	private boolean authenticated = false;

	public HttpMethodWrapper httpWrapper;
 
	public ManagementClient(String username, String password) {
		this.username = username;
		this.password = password;
		this.httpWrapper = new HttpMethodWrapper();
	}

	public void doLoginIfNecessary() { 
		if (!this.authenticated) {
			this.log.info("Need to login, fetching login form");

			if (this.httpWrapper.getState() == null) { // (we have no cookies,
														// FIXME)
				this.getLoginForm();
			} 

			this.submitLoginForm();
		}
	}

	public void executeProcess(String processId) throws Exception {
		this.httpWrapper.httpPost(ManagementClient.execute_process_url.replace("PROCESS", processId));
	}

	public List<ProcessDefinitionRef> getAllDefinitions() throws Exception {
		String result = this.httpWrapper.httpGet("/business-central-server/rs/process/definitions");

		this.log.debug("Completed request to show all definitions");

		Gson gson = new Gson();
		ProcessDefinitionRefWrapper wrapper = gson.fromJson(result, ProcessDefinitionRefWrapper.class);

		return wrapper.getDefinitions();
	}
 
	public void getHistoricProcessInstance() throws Exception {
		String processId = "{http://www.jboss.org/bpel/examples}HelloGoodbye-1";
		// String encodedId = ModelAdaptor.encodeId(processId);
		String processDefinition = "org.jbpm.evaluation.carinsurance";

		new java.util.Date(103, 1, 1).getTime();
		new java.util.Date().getTime();

		// /business-central-server/rs/process/definition/history/org.jbpm.evaluation.carinsurance.quote/nodeInfo
		String search_url = ManagementClient.history_search_url + processDefinition + "/nodeInfo";

		// + "/instances?status=" + status + "&starttime=" + starttime
		// + "&endtime=" + endtime;

		this.log.debug("Get historic process instances from process definition of : " + processId);

		String result = this.httpWrapper.httpGet(search_url);

		Gson gson = new Gson();
		HistoryProcessInstanceRefWrapper wrapper = gson.fromJson(result, HistoryProcessInstanceRefWrapper.class);

		for (HistoryProcessInstanceRef ref : wrapper.getDefinitions()) {
			this.log.debug("historic instance id is: " + ref.getProcessInstanceId() + " definition key is: " + URLDecoder.decode(ref.getProcessDefinitionId(), "UTF-8"));
		}

	}
	 
	public String getUrlHumanTaskForm(int taskNumber) throws Exception {
		return render_human_task_url.replace("TASKID", Integer.toString(taskNumber));
 	}
	
	public String renderHumanTaskForm(int taskNumber) throws Exception {
		String renderedForm = this.httpWrapper.httpGet(render_human_task_url.replace("TASKID", Integer.toString(taskNumber)));
		
		this.log.debug("Got rendered human task form");
		
		return renderedForm;
	}

	private void getLoginForm() {
		try {
			this.httpWrapper.httpGet(ManagementClient.authentication_form_url);

			// set scope.
			// httpclient.getParams().setAuthenticationPreemptive(true);
			// httpClient.getState().setCredentials(new AuthScope("localhost",
			// 8080, AuthScope.ANY_REALM), defaultcreds);

			Cookie c = this.httpWrapper.getState().getCookies()[0];
			c.setPathAttributeSpecified(false);
			c.setDomainAttributeSpecified(false);
			c.setVersion(-1);

			this.log.debug("My cookie is (should include JSESSIONID): " + c);
		} catch (Exception e) {
			System.out.println(e);
		}
	} 

	public List<ProcessInstanceRef> getRunningProcessInstances(String processName) throws Exception {
		String result = this.httpWrapper.httpGet("/business-central-server/rs/process/definition/{id}/instances".replace("{id}", processName));

		Gson gson = new Gson();
		ProcessInstanceRefWrapper wrapper = gson.fromJson(result, ProcessInstanceRefWrapper.class);

		return wrapper.getInstances();
	}

	public void showAllDeployments() throws Exception {
		String result = this.httpWrapper.httpGet(ManagementClient.deployment_url);

		System.out.println("Result of showAllDeployments: " + result);

		// System.out.println("Marshall the Json data into java class.");
		//
		// Gson gson = new Gson();
		//
		// DeploymentRefWrapper wrapper = gson.fromJson(result,
		// DeploymentRefWrapper.class);
		//
		// for (DeploymentRef ref : wrapper.getDeployments()) {
		// System.out.println("deployment name is: " + ref.getName());
		// }
	}

	public void showAllJobs() throws Exception {
		String result = this.httpWrapper.httpGet(this.jobs_url);

		this.log.debug("list of jobs: " + result);
	}
	
	private final String urlAvailableHumanTasks = "/business-central-server/rs/tasks/admin/participation";
	 
	public String getAvailableHumanTasks() throws Exception { 
		String response = this.httpWrapper.httpGet(urlAvailableHumanTasks);
		 
		return response;
	}

	private void submitLoginForm() {
		this.log.debug("submiting Login Form");

		this.log.debug("How many cookies do I have?: " + this.httpWrapper.getState().getCookies());

		NameValuePair[] data = { new NameValuePair("j_username", this.username), new NameValuePair("j_password", this.password) };

		this.httpWrapper.httpPost(ManagementClient.authentication_submit_url, data);

		this.log.debug("auth content result: " + this.httpWrapper.lastContent);

		if (httpWrapper.lastResponse == 302) {
			log.debug("Redirecting, login was probably okay");
			this.authenticated = true; 
		} else { 
			log.debug("Not redirecting after login, so the login probably failed");
		}
	}
}
