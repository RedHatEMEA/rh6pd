package org.rh6pd.processController;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rh6pd.processController.model.Dataset;
import rh6pd.processController.model.DefinitionsRS;
import rh6pd.processController.model.HumanTaskHtmlForm;
import rh6pd.processController.model.InstancesRS;
import rh6pd.processController.model.ProcessDefinitionInstancesRS;
import rh6pd.processController.model.TaskRS;
import rh6pd.processController.model.TaskUserRS;
import rh6pd.processController.model.UserTaskVO;
import rh6pd.processController.service.Environment;
import rh6pd.processController.service.Process;
import rh6pd.processController.utils.ParseXML;

/**
 * Unit Test to execute multiple Human Tasks & test the initial Insurance
 * Workflow
 */

public class InsuranceFlowTest {
	private final Logger log = LoggerFactory.getLogger(RewardsDemoTests.class);

	private final Map<String, Object> map = new HashMap<String, Object>();
	private final int timestamp = (int) (System.currentTimeMillis() / 1000); // Easy
																				// unique
																				// identifier
																				// for
																				// our
																				// process
	private int processID; // Instance Process ID

	@Test
	public void testRunProcess() throws Exception {
		DefinitionsRS defnitionRS = new DefinitionsRS();
		defnitionRS.setId("org.jbpm.rewards.basicInsuranceFlow");

		Process process = new Process(defnitionRS);

		startProcess(process);
		processID = Integer.parseInt(getInstanceID(defnitionRS, process));

		if (submitDriverHistory(process) == true) {
			if (submitCarDetails(process) == true) {
				log.info("Successfully Processed: " + defnitionRS.getId() + " with a ProcessID of : " + processID);
			}
		} else {
			this.log.debug("Failed to complete Basic Insurance Flow Test");
		}
	}

	public void startProcess(Process process) throws Exception {

		String htmlProcessStart = process.getProcessRenderHTML();
		Assert.assertNotNull(htmlProcessStart);

		log.debug("HTML for starting a new process" + htmlProcessStart);

		HumanTaskHtmlForm htStartProcessHtml = new HumanTaskHtmlForm(htmlProcessStart);
		Assert.assertEquals(3, htStartProcessHtml.getElementCount());

		map.put("driverName", "Test Subject");
		map.put("driverAge", 18);
		map.put("instanceID", timestamp);

		process.processComplete(map);
	}

	public boolean submitDriverHistory(Process process) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("DriverPoints", 2);
		params.put("DriverAccidents", 2);

		if (completeTaskForm(process, params) == true) {
			return true;
		}
		return false;
	}

	public boolean submitCarDetails(Process process) throws Exception {

		// BRMS can be tempramental in its response times,
		// this little sleep should allow it to catchup with itself!
		Thread.sleep(100);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("CarMake", "BMW");
		params.put("CarModel", "3 Series");
		params.put("CarEngine", 3);

		if (completeTaskForm(process, params) == true) {
			return true;
		}
		return false;
	}

	public UserTaskVO getUserTaskVO() throws Exception {

		Environment e = new Environment();
		UserTaskVO userTaskVO = new UserTaskVO("admin", "admin");
		TaskUserRS tasks = e.getListUserTask(userTaskVO);

		for (TaskRS task : tasks.getTasks()) {
			if (Integer.parseInt(task.getProcessInstanceId()) == processID) {
				userTaskVO.setTaskRS(task);
				return userTaskVO;
			}
		}
		return null;
	}

	public boolean completeTaskForm(Process p, Map<String, Object> params) throws Exception {

		UserTaskVO userTaskVO = getUserTaskVO();
		Assert.assertNotNull(userTaskVO);
		Assert.assertNotNull(userTaskVO.getTaskRS());

		if (userTaskVO.getTaskRS() != null) {
			String html = p.getRenderTaskFromUserTaskVO(userTaskVO);
			Assert.assertNotNull(html);
			this.log.debug(html);

			String response = p.taskComplete(userTaskVO.getTaskRS(), params);

			Assert.assertNotNull(response);
			Assert.assertTrue(response.contains("Successfully processed input"));

			if (response.contains("Successfully processed input") == true) {
				return true;
			}
			this.log.debug(response);
		}
		return false;
	}

	public void renderForm(DefinitionsRS definitionsRS, Process p, Map<String, Object> params) throws Exception {
		String response = p.processComplete(params);

		Assert.assertNotNull(response);
		Assert.assertTrue(response.contains("Successfully processed input"));
	}

	// Obtain Instance using our timestamp
	public String getInstanceID(DefinitionsRS defnitionRS, Process p) throws Exception {
		ProcessDefinitionInstancesRS processInstance = p.getProcessInstances(defnitionRS);

		if (!processInstance.getInstances().isEmpty()) {
			for (InstancesRS instances : processInstance.getInstances()) {
				Dataset dataset = ParseXML.ParseDataSetXML((p.getDataSetByInstanceId(instances)));
				if (!dataset.getData().isEmpty() && !map.isEmpty() && map.size() == dataset.getData().size()) {
					if (map.containsKey("instanceID")) {
						if (map.get("instanceID").equals(timestamp)) {
							return dataset.getDatasetRef();
						}
					}
				}
			}
		}
		return null;
	}
}
