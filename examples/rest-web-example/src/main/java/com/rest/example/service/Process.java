package com.rest.example.service;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.rest.example.manager.EnumJBPMRestType;
import com.rest.example.manager.JBPMRestManagementClient;
import com.rest.example.model.ActiveNodeInfoRS;
import com.rest.example.model.DefinitionsRS;
import com.rest.example.model.InstancesRS;
import com.rest.example.model.ProcessDefinitionInstancesRS;
import com.rest.example.model.ProcessDefinitionsRS;
import com.rest.example.model.TaskRS;
import com.rest.example.model.TaskUserRS;
import com.rest.example.model.UserTaskVO;

public class Process extends JBPMRestManagementClient {

	static Process process;

	public static Process instance() {
		if (process == null) {
			process = new Process();
		}
		return process;
	}

	public Process() {
		super();
	}

	public Process(String username, String password) {
		super(username, password);
	}

	public ProcessDefinitionsRS getDefinitions() throws Exception {
		ProcessDefinitionsRS proc = null;
		String url = super.urlForm("process.management.definitions");
		String json = "";
		json = super.getDataFromRestService(url, EnumJBPMRestType.GET);

		Gson gson = new Gson();
		proc = gson.fromJson(json, ProcessDefinitionsRS.class);
		return proc;
	}

	public ProcessDefinitionInstancesRS getProcessInstances(
			DefinitionsRS definitionsRS) throws Exception {
		ProcessDefinitionInstancesRS pi = null;
		String url = super.urlForm("process.management.instances",
				definitionsRS.getId());
		String json = super.getDataFromRestService(url, EnumJBPMRestType.GET);
		Gson gson = new Gson();
		pi = gson.fromJson(json, ProcessDefinitionInstancesRS.class);
		return pi;
	}

	/**
	 * Requires definition id & instance id
	 * 
	 * @param instancesRS
	 * @return
	 * @throws Exception
	 */
	public InstancesRS getProcessInstance(InstancesRS instancesRS)
			throws Exception {
		DefinitionsRS definitionsRS = new DefinitionsRS();
		definitionsRS.setId(instancesRS.getDefinitionId());
		ProcessDefinitionInstancesRS pi = this
				.getProcessInstances(definitionsRS);
		for (InstancesRS instance : pi.getInstances()) {
			if (instance.getId().equals(instancesRS.getId())) {
				instancesRS = instance;
			}
		}
		return instancesRS;
	}

	public void deleteInstance(InstancesRS instance) throws Exception {
		String url = super.urlForm("process.management.delete",
				instance.getId());
		String json = super.getDataFromRestService(url, EnumJBPMRestType.POST);
		System.out.println(json);
	}

	public void testStartInstance(DefinitionsRS definitionsRS) throws Exception {
		String url = super.urlForm("process.management.new_instance",
				definitionsRS.getId());
		String json = super.getDataFromRestService(url, EnumJBPMRestType.POST);

		if (!json.isEmpty()) {
			System.out.println(json);
		} else {
			System.out.println("DEBUG: json string empty!");
		}
	}

	public InstancesRS startInstance(DefinitionsRS definitionsRS)
			throws Exception {
		InstancesRS inst;
		String url = super.urlForm("process.management.new_instance",
				definitionsRS.getId());
		String json = super.getDataFromRestService(url, EnumJBPMRestType.POST);
		Gson gson = new Gson();
		inst = gson.fromJson(json, InstancesRS.class);
		return inst;
	}

	public Collection<ActiveNodeInfoRS> getActiveNodeInfo(InstancesRS instance)
			throws Exception {
		Collection<ActiveNodeInfoRS> list = null;
		String url = super.urlForm("process.management.activeNodeInfo",
				instance.getId());
		String json = super.getDataFromRestService(url, EnumJBPMRestType.GET);
		Gson gson = new Gson();
		Type collectionType = new TypeToken<Collection<ActiveNodeInfoRS>>() {
		}.getType();
		list = gson.fromJson(json, collectionType);
		return list;
	}

	public DefinitionsRS getDefinition(DefinitionsRS definition)
			throws Exception {
		ProcessDefinitionsRS proc = this.getDefinitions();
		Collection<DefinitionsRS> list = proc.getDefinitions();
		for (DefinitionsRS definitionsRS : list) {
			if (definition.getId().equals(definitionsRS.getId())) {
				definition = definitionsRS;
			}
		}

		return definition;
	}

	public String getUrlImage(DefinitionsRS definitionsRS) throws Exception {
		String url = super.urlForm("process.management.image",
				definitionsRS.getId());
		return url;
	}

	public InputStream getImage(DefinitionsRS definitionsRS) throws Exception {
		String url = this.getUrlImage(definitionsRS);
		InputStream img = super.getBytesFromRestService(url,
				EnumJBPMRestType.GET);
		return img;
	}

	public TaskUserRS getListUserTask(UserTaskVO userTaskVO) throws Exception {
		TaskUserRS tu = null;
		String url = super.urlForm("task.list.tasks", userTaskVO.getUsername());
		super.relogin(userTaskVO.getUsername(), userTaskVO.getPassword());
		String json = super.getDataFromRestService(url, EnumJBPMRestType.GET);
		Gson gson = new Gson();
		tu = gson.fromJson(json, TaskUserRS.class);
		return tu;
	}

	/**
	 * Requires username, password and the idTaskRS
	 * 
	 * @param userTaskVO
	 * @return
	 * @throws Exception
	 */
	public TaskRS getUserTask(UserTaskVO userTaskVO) throws Exception {
		TaskUserRS tu = null;
		TaskRS task = userTaskVO.getTaskRS();
		tu = this.getListUserTask(userTaskVO);
		for (TaskRS iterable_element : tu.getTasks()) {
			if (iterable_element.getId().equals(task.getId())) {
				task = iterable_element;
			}
		}
		return task;
	}

	public String getProcessRenderHTML(DefinitionsRS pd) throws Exception {
		String url = super.urlForm("form.process.render", pd.getId());

		String html = super.getDataFromRestService(url, EnumJBPMRestType.GET);
		String formTag = StringUtils.substringBetween(html, "<form", ">");
		formTag = "<form" + formTag + ">";
		String formTagFinal = formTag
				+ "<input type='hidden' name='_e_pID' value='" + pd.getId()
				+ "' />";
		html = html.replaceAll(formTag, formTagFinal);
		// html = html.replaceAll(formExp, formExp +
		// "<input type='hidden' name='_e_pID' value='"+pd.getId()+"' />");
		return html;
	}

	public String getTaskRenderHTML(UserTaskVO userTaskVO) throws Exception {
		String taskId = userTaskVO.getTaskRS().getId();
		String url = super.urlForm("form.task.render", taskId);
		super.relogin(userTaskVO.getUsername(), userTaskVO.getPassword());
		String html = super.getDataFromRestService(url, EnumJBPMRestType.GET);
		String formTag = StringUtils.substringBetween(html, "<form", ">");
		formTag = "<form" + formTag + ">";
		String formTagFinal = formTag
				+ "<input type='hidden' name='_e_tID' value='" + taskId
				+ "' />";
		html = html.replaceAll(formTag, formTagFinal);
		return html;
	}

	public InputStream getProcessRender(DefinitionsRS pd) throws Exception {
		InputStream render = null;
		String url = super.urlForm("form.process.render", pd.getId());
		render = super.getBytesFromRestService(url, EnumJBPMRestType.GET);
		return render;
	}

	public InputStream getTaskRender(UserTaskVO userTaskVO) throws Exception {
		InputStream render = null;
		String url = super.urlForm("form.task.render", userTaskVO.getTaskRS()
				.getId());
		super.relogin(userTaskVO.getUsername(), userTaskVO.getPassword());
		render = super.getBytesFromRestService(url, EnumJBPMRestType.GET);
		return render;
	}

	public String processComplete(DefinitionsRS definitionsRS) throws Exception {
		String resp;
		String url = super.urlForm("form.process.complete",
				definitionsRS.getId());
		resp = super.getDataFromRestService(url, EnumJBPMRestType.MULTIPART);
		System.out.println(resp);
		return resp;
	}

	public String processComplete(DefinitionsRS definitionsRS,
			Map<String, Object> params) throws Exception {
		String resp;
		String url = super.urlForm("form.process.complete",
				definitionsRS.getId());
		
		System.out.println("DEBUG URL: " + url);
		resp = super.getDataFromRestService(url, EnumJBPMRestType.MULTIPART,
				params);
		System.out.println(resp);
		return resp;
	}

	public String taskComplete(TaskRS taskRS, Map<String, Object> params)
			throws Exception {
		String resp;
		String url = super.urlForm("form.task.complete", taskRS.getId());
		resp = super.getDataFromRestService(url, EnumJBPMRestType.MULTIPART,
				params);
		System.out.println(resp);
		return resp;
	}

	public String taskComplete(UserTaskVO userTaskVO, Map<String, Object> params)
			throws Exception {
		String resp;
		String url = super.urlForm("form.task.complete", userTaskVO.getTaskRS()
				.getId());
		super.relogin(userTaskVO.getUsername(), userTaskVO.getPassword());
		resp = super.getDataFromRestService(url, EnumJBPMRestType.MULTIPART,
				params);
		// this.getSecureSid();
		System.out.println(resp);
		return resp;
	}

	public void userInvalidate() throws Exception {
		String url = super.urlForm("user.management.invalidate");
		super.getDataFromRestService(url, EnumJBPMRestType.POST);
	}

	public String getSid() throws Exception {
		String url = super.urlForm("user.management.sid");
		return super.getDataFromRestService(url, EnumJBPMRestType.GET);
	}

	public String getSecureSid() throws Exception {
		String url = super.urlForm("user.management.secure.sid");
		return super.getDataFromRestService(url, EnumJBPMRestType.GET);
	}

	public String getDataSet(TaskRS taskRS) throws Exception {
		String data = "";
		String url = super
				.urlForm("process.management.dataset", taskRS.getId());
		data = super.getDataFromRestService(url, EnumJBPMRestType.GET);
		return data;
	}

	public static void main(String[] args) throws Exception {

		TaskRS taskRS = new TaskRS();
		taskRS.setId("2");
		String proc = Process.instance().getDataSet(taskRS);
		System.out.println(taskRS);
		// ProcessDefinitionsRS proc = Process.instance().getDefinitions();
		// DefinitionsRS definition = new DefinitionsRS();
		// definition.setId("Hello");
		// Process.instance().processComplete(definition);
		// DefinitionsRS defini = Process.instance().getDefinition(definition);
		// System.out.println(defini.getDiagramUrl());
		// ProcessDefinitionInstancesRS pi =
		// Process.instance().getProcessInstances(definition);
		// // InstancesRS instance = new InstancesRS();
		// // instance.setId("2");
		// // Process.instance().getActiveNodeInfo(instance);
		// TaskUserRS taskUsers = Process.instance().getUserTask(new
		// UserTaskVO("krisv", "krisv"));
		// Process.instance().getImage(definition);
	}

}