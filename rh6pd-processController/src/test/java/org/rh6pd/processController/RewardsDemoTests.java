package org.rh6pd.processController;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rh6pd.processController.model.DefinitionsRS;
import rh6pd.processController.model.HumanTaskHtmlForm;
import rh6pd.processController.model.TaskRS;
import rh6pd.processController.model.TaskUserRS;
import rh6pd.processController.model.UserTaskVO;
import rh6pd.processController.service.Environment;
import rh6pd.processController.service.GroupParticipationTasks;
import rh6pd.processController.service.Process;

public class RewardsDemoTests {
	private static Logger log = LoggerFactory.getLogger(RewardsDemoTests.class);
	private final Environment e = new Environment();

	@Test
	public void testRewardsDemoFromStartToFinish() throws Exception {
		DefinitionsRS def = new DefinitionsRS();
		def.setId("org.jbpm.approval.rewards");
		def.setVersion("2");

		Process process = new Process(def);

		Assert.assertTrue("Is rewards demo deployed to server? : " + def.getId() + " at version: " + def.getVersion(), e.hasDefinition(def));
		submitProcessStartForm(process);
		claimApprovalForm(process);
		submitManagerApprovalForm(process);
	}

	public void submitProcessStartForm(Process process) throws Exception {
		String htmlProcessStart = process.getProcessRenderHTML();

		Assert.assertNotNull(htmlProcessStart);

		log.debug("HTML for starting a new process" + htmlProcessStart);

		HumanTaskHtmlForm htStartProcessHtml = new HumanTaskHtmlForm(htmlProcessStart);
		Assert.assertEquals(3, htStartProcessHtml.getElementCount());

		HashMap<String, Object> startTaskProps = new HashMap<String, Object>();
		startTaskProps.put("employee", "Ally");
		startTaskProps.put("reason", "test");
		process.processComplete(startTaskProps);
	}

	public void claimApprovalForm(Process process) throws Exception {
		Environment e = new Environment();

		GroupParticipationTasks groupTasks = e.getGroupParticipationTasks("admin");
		assertThat(groupTasks.getTasks(), not(empty()));

	}

	private void submitManagerApprovalForm(Process process) throws Exception {
		Environment e = new Environment();
		UserTaskVO ut = new UserTaskVO("admin", "admin");
		TaskUserRS userTasks = e.getListUserTask(ut);

		assertThat("No human tasks in order to do manager approval", userTasks.getTasks(), not(empty()));

		// Assumes the first task in the admin task list is the 1 we want!
		TaskRS userTask = userTasks.getTasks().get(0);

		String htmlUserTask = process.getRenderTaskFromTaskRS(userTask, ut.getUsername(), ut.getPassword());
		Assert.assertFalse("The HTML for a Human User task looks like a Tomcat error report", htmlUserTask.contains("Error report"));

		HumanTaskHtmlForm htHtmlUserTask = new HumanTaskHtmlForm(htmlUserTask);

		log.warn("UT HTML: " + htmlUserTask);
		Assert.assertNotSame(0, htHtmlUserTask.getElementCount());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Explanation", "He's a tw*t");
		map.put("Outcome", "Approved");

		process.taskComplete(userTask, map);
	}

}
