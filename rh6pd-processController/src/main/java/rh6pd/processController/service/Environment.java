package rh6pd.processController.service;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rh6pd.processController.manager.HttpRequestType;
import rh6pd.processController.manager.JBPMRestManagementClient;
import rh6pd.processController.model.ActiveNodeInfoRS;
import rh6pd.processController.model.DefinitionsRS;
import rh6pd.processController.model.InstancesRS;
import rh6pd.processController.model.ProcessDefinitionInstancesRS;
import rh6pd.processController.model.ProcessDefinitionsRS;
import rh6pd.processController.model.TaskRS;
import rh6pd.processController.model.TaskUserRS;
import rh6pd.processController.model.UserTaskVO;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Environment extends JBPMRestManagementClient {
	private static final transient Logger LOG = LoggerFactory.getLogger(Environment.class);

	public Environment() {
		super("admin", "admin");
	}

	public Collection<TaskRS> getAvailableHumanTasks(String username) throws Exception {
		UserTaskVO ut = new UserTaskVO("admin", "admin");
		TaskUserRS listHts = this.getListUserTask(ut);

		return listHts.getTasks();
	}

	public void deleteInstance(InstancesRS instance) throws Exception {
		String url = super.urlForm("process.management.delete", instance.getId());
		String json = super.getDataFromRestService(url, HttpRequestType.POST);
		LOG.warn(json);
	}

	public void terminateInstance(InstancesRS instanceRS) throws Exception {
		String url = super.urlForm("process.management.terminate", instanceRS.getId());
		super.getDataFromRestService(url, HttpRequestType.POST);
	}

	public void terminateAll() throws Exception {
		LOG.warn("terminating everything in jBPM");
		Environment e = new Environment();

		ProcessDefinitionsRS defs = e.getDefinitions();

		for (DefinitionsRS o : defs.getDefinitions()) {
			ProcessDefinitionInstancesRS listInstances = this.getProcessInstances(o);

			LOG.info("Instances to terminate: " + listInstances.getInstances().size());

			for (InstancesRS i : listInstances.getInstances()) {
				this.terminateInstance(i);

				LOG.info("Terminating process instance: " + i.getId() + " which is of type: " + i.getDefinitionId());
			}
		}
	}

	/**
	 * Requires definition id & instance id
	 * 
	 * @param instancesRS
	 * @return
	 * @throws Exception
	 */
	public InstancesRS getProcessInstance(InstancesRS instancesRS) throws Exception {
		DefinitionsRS definitionsRS = new DefinitionsRS();
		definitionsRS.setId(instancesRS.getDefinitionId());
		ProcessDefinitionInstancesRS pi = this.getProcessInstances(definitionsRS);
		for (InstancesRS instance : pi.getInstances()) {
			if (instance.getId().equals(instancesRS.getId())) {
				instancesRS = instance;
			}
		}

		return instancesRS;
	}

	public ProcessDefinitionsRS getDefinitions() throws Exception {
		ProcessDefinitionsRS proc = null;
		String url = urlForm("process.management.definitions");
		String json = "";
		json = this.getDataFromRestService(url, HttpRequestType.GET);

		LOG.info(json);

		Gson gson = new Gson();
		proc = gson.fromJson(json, ProcessDefinitionsRS.class);
		return proc;
	}

	public ProcessDefinitionInstancesRS getProcessInstances(DefinitionsRS definitionsRS) throws Exception {
		ProcessDefinitionInstancesRS pi = null;
		String url = urlForm("process.management.instances", definitionsRS.getId());
		String json = getDataFromRestService(url, HttpRequestType.GET);
		Gson gson = new Gson();
		pi = gson.fromJson(json, ProcessDefinitionInstancesRS.class);
		return pi;
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

	public TaskUserRS getListUserTask(UserTaskVO userTaskVO) throws Exception {
		String url = super.urlForm("task.list.tasks", userTaskVO.getUsername());

		super.relogin(userTaskVO.getUsername(), userTaskVO.getPassword());
		LOG.warn("relogging in to BRMS, to get user tasks as a specific user");

		String json = super.getDataFromRestService(url, HttpRequestType.GET);
		LOG.info("User tasks as json: " + json);

		return new Gson().fromJson(json, TaskUserRS.class);
	}

	public Collection<ActiveNodeInfoRS> getActiveNodeInfo(InstancesRS instance) throws Exception {
		Collection<ActiveNodeInfoRS> list = new Vector<ActiveNodeInfoRS>();

		String url = super.urlForm("process.management.activeNodeInfo", instance.getId());
		String json = super.getDataFromRestService(url, HttpRequestType.GET);
		Gson gson = new Gson();
		Type collectionType = new TypeToken<Collection<ActiveNodeInfoRS>>() {
		}.getType();

		list = gson.fromJson(json, collectionType);
		return list;
	}

	@Deprecated
	public DefinitionsRS getDefinition(DefinitionsRS definition) throws Exception {
		ProcessDefinitionsRS proc = this.getDefinitions();
		Collection<DefinitionsRS> list = proc.getDefinitions();
		for (DefinitionsRS definitionsRS : list) {
			if (definition.getId().equals(definitionsRS.getId())) {
				definition = definitionsRS;
			}
		}

		return definition;
	}

	public GroupParticipationTasks getGroupParticipationTasks(String username) throws Exception {
		String url = super.urlForm("task.list.participation", username);

		String json = super.getDataFromRestService(url, HttpRequestType.GET);

		return new Gson().fromJson(json, GroupParticipationTasks.class);
	}

	public boolean hasDefinition(DefinitionsRS search) throws Exception {
		String sversion = search.getVersion();

		for (DefinitionsRS def : this.getDefinitions().getDefinitions()) {
			if (def.getId().equals(search.getId())) {
				if (def.getVersion().equals(sversion)) {
					return true;
				}

				return false;
			}
		}

		return false;
	}
}
