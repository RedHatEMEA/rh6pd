package org.rh6pd.processController;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rh6pd.processController.model.ActiveNodeInfoRS;
import rh6pd.processController.model.DefinitionsRS;
import rh6pd.processController.model.InstancesRS;
import rh6pd.processController.model.ProcessDefinitionInstancesRS;
import rh6pd.processController.model.TaskRS;
import rh6pd.processController.model.TaskUserRS;
import rh6pd.processController.model.UserTaskVO;
import rh6pd.processController.service.Environment;

@RunWith(Theories.class)
public class ProcessEnumerationTest {
	private static final transient Logger LOG = LoggerFactory.getLogger(ProcessEnumerationTest.class);

	@DataPoints
	public static String[] processes() {
		ArrayList<String> processIds = new ArrayList<String>();
		processIds.add("org.jbpm.approval.rewards");

		return processIds.toArray(new String[] {});
	}

	@Theory
	public void testProcess(String processName) {
		ProcessEnumerationTest.LOG.debug("Test process: " + processName);
		Assert.assertTrue(true);
	}

	@Test
	public void testListAllAvailableHumanTasks() throws Exception {
		Environment e = new Environment();

		UserTaskVO creds = new UserTaskVO("admin", "admin");
		TaskUserRS tasks = e.getListUserTask(creds);

		Assert.assertNotNull(tasks);

		for (TaskRS task : tasks.getTasks()) {
			ProcessEnumerationTest.LOG.debug("UT: " + task.getId() + " name: " + task.getName());
		}
	}

	@Test
	public void testListProcessDefinitionsAndInstances() throws Exception {
		Environment e = new Environment();

		for (DefinitionsRS def : e.getDefinitions().getDefinitions()) {
			LOG.info("Found process definition: " + def.getId());

			ProcessDefinitionInstancesRS listInstances = e.getProcessInstances(def);

			LOG.info("-- Instance count: " + listInstances.getInstances().size());

			for (InstancesRS instance : listInstances.getInstances()) {
				ProcessEnumerationTest.LOG.info("---- Instance ID: " + instance.getDefinitionId() + " : Instance ID : " + instance.getId() + " : StartDate :" + instance.getStartDate());

				for (ActiveNodeInfoRS activeNodeInstance : e.getActiveNodeInfo(instance)) {
					ProcessEnumerationTest.LOG.debug("------ ActiveNodeInfo Name- " + activeNodeInstance.getActiveNode().getName());
				}
			}
		}
	}

	/**
	 * This method is pretty much a utility, to set off a thermoneuclear device
	 * inside BRMS, killing all process instances. This is really useful when
	 * BRMS decides to start crashing a bunch of instances that it leaves open.
	 */
	@Test
	@Ignore
	public void testTerminateAllInstances() throws Exception {
		new Environment().terminateAll();
	}
}
