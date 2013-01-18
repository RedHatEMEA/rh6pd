package rh6pd.processController.service;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rh6pd.processController.manager.HttpRequestType;
import rh6pd.processController.manager.JBPMRestManagementClient;
import rh6pd.processController.model.DefinitionsRS;
import rh6pd.processController.model.InstancesRS;
import rh6pd.processController.model.ProcessDefinitionInstancesRS;
import rh6pd.processController.model.TaskRS;
import rh6pd.processController.model.UserTaskVO;

import com.google.gson.Gson;

public class Process extends JBPMRestManagementClient {
	public enum ProcessState {
		NOTSTARTED, REQ_FORM_START_PROCESS, SUB_FORM_START_PROCESS, REQ_HT_FORM, SUB_HT_FORM;

		private String[] args;

		ProcessState(String... args) {
			this.args = args;
		}

		public String getArg(int i) {
			return this.args[i];
		}
	}

	private ProcessState processState = ProcessState.NOTSTARTED;
	private final DefinitionsRS definitionsRS;

	private static final transient Logger LOG = LoggerFactory.getLogger(JBPMRestManagementClient.class);

	static Process process;

	public Process(DefinitionsRS def) {
		super("admin", "admin");

		this.definitionsRS = def;
	}

	public String getDataSet(TaskRS taskRS) throws Exception {
		String data = "";
		String url = super.urlForm("process.management.dataset", taskRS.getId());
		data = super.getDataFromRestService(url, HttpRequestType.GET);
		return data;
	}

	public String getDataSetByInstanceId(InstancesRS instance) throws Exception {
		String data = "";
		String url = super.urlForm("process.management.dataset", instance.getId());
		data = super.getDataFromRestService(url, HttpRequestType.GET);
		return data;
	}

	public InputStream getProcessImage() throws Exception {
		String url = this.getProcessImageUrl();
		InputStream img = super.getBytesFromRestGetService(url);
		return img;
	}

	public String getMutilatedProcessRenderHtml(String html, String bundleString, Object... params) throws Exception {
		LOG.debug(html);

		Pattern p = Pattern.compile("form action=\"(.+?)\"");
		Matcher m = p.matcher(html);

		if (m.find()) {
			String url = bundle.getString(bundleString);
			url = "http://" + bundle.getString("process.host") + bundle.getString("process.urlContext") + MessageFormat.format(url, params);

			html = html.replace(m.group(1), url);
		}
		return html;
	}

	public ProcessDefinitionInstancesRS getProcessInstances(DefinitionsRS definitionsRS) throws Exception {
		ProcessDefinitionInstancesRS pdi = null;
		String url = super.urlForm("process.management.instances", definitionsRS.getId());
		String json = super.getDataFromRestService(url, HttpRequestType.GET);
		Gson gson = new Gson();
		pdi = gson.fromJson(json, ProcessDefinitionInstancesRS.class);
		return pdi;
	}

	public InputStream getProcessRender() throws Exception {
		InputStream render = null;
		String url = super.urlForm("form.process.render", this.definitionsRS.getId());
		render = super.getBytesFromRestGetService(url);
		return render;
	}

	public String getSecureSid() throws Exception {
		String url = super.urlForm("user.management.secure.sid");
		return super.getDataFromRestService(url, HttpRequestType.GET);
	}

	public String getSid() throws Exception {
		String url = super.urlForm("user.management.sid");
		return super.getDataFromRestService(url, HttpRequestType.GET);
	}

	public String getRenderTaskFromUserTaskVO(UserTaskVO userTaskVO) throws Exception {
		return getTaskRenderHTML(userTaskVO.getTaskRS().getId(), userTaskVO.getUsername(), userTaskVO.getPassword());
	}

	public String getProcessRenderHTML() throws Exception {
		String url = super.urlForm("form.process.render", this.definitionsRS.getId());

		String html = super.getDataFromRestService(url, HttpRequestType.GET);
		String formTag = StringUtils.substringBetween(html, "<form", ">");
		formTag = "<form" + formTag + ">";
		String formTagFinal = formTag + "<input type='hidden' name='_e_pID' value='" + this.definitionsRS.getId() + "' />";
		html = html.replaceAll(formTag, formTagFinal);
		return html;
	}

	public InputStream getTaskRender(UserTaskVO userTaskVO) throws Exception {
		InputStream render = null;
		String url = super.urlForm("form.task.render", userTaskVO.getTaskRS().getId());

		// need to login again as the user that owns the task
		super.relogin(userTaskVO.getUsername(), userTaskVO.getPassword());

		return super.getBytesFromRestGetService(url);
	}

	public String getTaskRenderHTMLTemp(UserTaskVO userTaskVO) throws Exception {
		String taskId = userTaskVO.getTaskRS().getId();
		String url = super.urlForm("form.task.render", taskId);
		super.relogin(userTaskVO.getUsername(), userTaskVO.getPassword());
		String html = super.getDataFromRestService(url, HttpRequestType.GET);
		return html;
	}

	public String getRenderTaskFromTaskRSTemp(TaskRS taskRS) throws Exception {
		String taskId = taskRS.getId();
		String url = super.urlForm("form.task.render", taskId);
		super.relogin("admin", "admin");
		String html = super.getDataFromRestService(url, HttpRequestType.GET);
		return html;
	}

	public String getRenderTaskFromTaskRS(TaskRS taskRS, String username, String password) throws Exception {
		if (username.isEmpty() || password.isEmpty() || taskRS == null) {
			return null;
		} else {
			String html = getTaskRenderHTML(taskRS.getId(), username, password);
			return html;
		}
	}

	public String getTaskRenderHTML(String taskID, String username, String password) throws Exception {
		String url = super.urlForm("form.task.render", taskID);
		super.relogin(username, password);
		String html = super.getDataFromRestService(url, HttpRequestType.GET);
		return html;
	}

	public String getProcessImageUrl() throws Exception {
		String url = super.urlForm("process.management.image", definitionsRS.getId());
		return url;
	}

	public String processComplete() throws Exception {
		String resp;
		String url = super.urlForm("form.process.complete", definitionsRS.getId());
		resp = super.getDataFromRestService(url, HttpRequestType.MULTIPART);
		LOG.info(resp);
		return resp;
	}

	public String processComplete(Map<String, Object> params) throws Exception {
		String resp;
		String url = super.urlForm("form.process.complete", definitionsRS.getId());

		LOG.debug("DEBUG URL: " + url);
		resp = super.getDataFromRestService(url, HttpRequestType.MULTIPART, params);
		return resp;
	}

	private void stateTransition(ProcessState expected, ProcessState transitionTo) throws Exception {
		if (processState != expected) {
			throw new Exception("Process cannot enter state: " + transitionTo + ", because it needs to be in state " + expected + " first. ");
		}

		this.processState = transitionTo;
	}

	@Deprecated
	public InstancesRS startInstance() throws Exception {
		this.stateTransition(ProcessState.NOTSTARTED, ProcessState.REQ_FORM_START_PROCESS);

		LOG.warn("Created new instance of process with definition id: " + definitionsRS.getId());

		InstancesRS inst;
		LOG.info("Definition RS: " + definitionsRS.getId());
		String url = super.urlForm("process.management.new_instance", definitionsRS.getId());
		String json = super.getDataFromRestService(url, HttpRequestType.POST);
		Gson gson = new Gson();
		inst = gson.fromJson(json, InstancesRS.class);
		return inst;
	}

	public String taskComplete(TaskRS taskRS, Map<String, Object> params) throws Exception {
		String resp;
		String url = super.urlForm("form.task.complete", taskRS.getId());
		resp = super.getDataFromRestService(url, HttpRequestType.MULTIPART, params);
		LOG.info(resp);
		return resp;
	}

	public String usertaskComplete(UserTaskVO userTaskVO, Map<String, Object> params) throws Exception {
		String resp;
		String url = super.urlForm("form.task.complete", userTaskVO.getTaskRS().getId());
		super.relogin(userTaskVO.getUsername(), userTaskVO.getPassword());
		resp = super.getDataFromRestService(url, HttpRequestType.MULTIPART, params);
		LOG.info(resp);
		return resp;
	}

	public void userInvalidate() throws Exception {
		String url = super.urlForm("user.management.invalidate");
		super.getDataFromRestService(url, HttpRequestType.POST);
	}

	public void claimGroupTask(TaskRS taskRS, String username) throws Exception {
		String url = super.urlForm("task.management.assign", taskRS.getId(), username);

		this.getDataFromRestService(url, HttpRequestType.POST);
	}
}