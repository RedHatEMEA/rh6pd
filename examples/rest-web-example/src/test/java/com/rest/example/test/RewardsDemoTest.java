package com.rest.example.test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rest.example.model.ActiveNodeInfoRS;
import com.rest.example.model.DefinitionsRS;
import com.rest.example.model.InstancesRS;
import com.rest.example.model.ProcessDefinitionInstancesRS;
import com.rest.example.model.ProcessDefinitionsRS;
import com.rest.example.service.Process;

public class RewardsDemoTest {

	private DefinitionsRS def;

	public void renderForm(DefinitionsRS definitionsRS, Map<String, Object> params) throws Exception {
		String response = Process.instance().processComplete(definitionsRS, params);

		if (response != null) {

			System.out.println("DEBUG: " + response);

		}
	}

	@Before
	public void setupATestingProcess() {
		this.def = new DefinitionsRS();
		this.def.setId("org.jbpm.approval.rewards");
	}

	@Test
	public void testGetAllDefinitions() throws Exception {
		// Retrieve a list of all definitions (flows) deployed in jBPM
		// Process process = new Process("admin", "admin");

		ProcessDefinitionsRS proc = Process.instance().getDefinitions();
		Collection<DefinitionsRS> definition = proc.getDefinitions();

		if (!definition.isEmpty()) {

			for (DefinitionsRS definitions : definition) {

				System.out.println("DEBUG: " + definitions.getName() + " : DefinitionID :" + definitions.getId());
			}
		} else {
			System.out.println("DEBUG: No definitions call returned");
			Assert.assertTrue(false);
		}
	}

	@Test
	public void testListAllAvailableHumanTasks() {

	}

	@Test
	public void testListProcessInstances() throws Exception {

		// List all instances open for a given Process Flow

		DefinitionsRS defnitionRS = new DefinitionsRS();
		// defnitionRS.setId("org.jbpm.approval.rewards.helloTest");
		defnitionRS.setId("org.jbpm.approval.rewards");

		ProcessDefinitionInstancesRS processInstance = Process.instance().getProcessInstances(defnitionRS);

		// Assert.notEmpty(processInstance.getInstances());

		if (processInstance != null) {

			for (InstancesRS instances : processInstance.getInstances()) {

				Collection<ActiveNodeInfoRS> activeNode = Process.instance().getActiveNodeInfo(instances);

				System.out.println("DEBUG: DefId - " + instances.getDefinitionId() + " : Instance ID : " + instances.getId() + " : StartDate :" + instances.getStartDate());

				for (ActiveNodeInfoRS activeNodeInstance : activeNode) {

					System.out.println("DEBUG: ActiveNodeInfo Name- " + activeNodeInstance.getActiveNode().getName());

				}

			}

		} else {

			System.out.println("DEBUG: No definitions call returned");
			// assertTrue(false);

		}

	}

	@Test
	public void testRenderForm() throws Exception {
		ProcessDefinitionInstancesRS process = Process.instance().getProcessInstances(this.def);

		// Assert.notEmpty(process.getInstances());

		// FIXME: This should go in @Before, so that we dont have tests that
		// depend on each other
		// If we put this @Before, then duplicate instances are created which
		// sucks!
		if (process.getInstances() == null) {
			try {
				Process.instance().testStartInstance(this.def);
			} catch (Exception e) {
				System.out.println("DEBUG: " + e);
			}
		}

		{
			String html = Process.instance().getProcessRenderHTML(this.def);
			Assert.assertNotNull(html);

			System.out.println("DEBUG (Form HTML): " + html);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("employee", "Ally");
			map.put("reason", "Rendering form via REST");

			try {
				this.renderForm(this.def, map);
			} catch (Exception e) {
				System.out.println("DEBUG: " + e);
			}
		}
	}
}
