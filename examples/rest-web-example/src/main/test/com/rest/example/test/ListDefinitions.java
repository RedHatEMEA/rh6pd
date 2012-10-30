package com.rest.example.test;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.Assert;

import com.rest.example.model.ActiveNodeInfoRS;
import com.rest.example.model.ActiveNodeRS;
import com.rest.example.model.DefinitionsRS;
import com.rest.example.model.InstancesRS;
import com.rest.example.model.ProcessDefinitionInstancesRS;
import com.rest.example.model.ProcessDefinitionsRS;
import com.rest.example.model.TaskRS;
import com.rest.example.model.UserTaskVO;
import com.rest.example.service.Process;

public class ListDefinitions {

	@Test
	@Ignore
	public void testGetAllDefinitions() throws Exception {

		// Retrieve a list of all definitions (flows) deployed in jBPM

		// Process process = new Process("admin", "admin");

		ProcessDefinitionsRS proc = Process.instance().getDefinitions();
		Collection<DefinitionsRS> definition = proc.getDefinitions();

		if (!definition.isEmpty()) {

			for (DefinitionsRS definitions : definition) {

				System.out.println("DEBUG: " + definitions.getName()
						+ " : DefinitionID :" + definitions.getId());
			}

		} else {

			System.out.println("DEBUG: No definitions call returned");
			assertTrue(false);

		}

	}

	// @Before
	@Ignore
	public void testBasicProcess() throws Exception {

		// Test a basic flow.
		System.out.println("FIRING!");

		DefinitionsRS defnitionRS = new DefinitionsRS();
		// defnitionRS.setId("org.jbpm.approval.rewards.helloTest");
		defnitionRS.setId("org.jbpm.approval.rewards");

		try {

			Process.instance().testStartInstance(defnitionRS);

		} catch (Exception e) {

			System.out.println("DEBUG: " + e);

		}

	}

	@Test
	@Ignore
	public void testListProcessInstances() throws Exception {

		// List all instances open for a given Process Flow

		DefinitionsRS defnitionRS = new DefinitionsRS();
		// defnitionRS.setId("org.jbpm.approval.rewards.helloTest");
		defnitionRS.setId("org.jbpm.approval.rewards");

		ProcessDefinitionInstancesRS processInstance = Process.instance()
				.getProcessInstances(defnitionRS);

		// Assert.notEmpty(processInstance.getInstances());

		if (processInstance != null) {

			for (InstancesRS instances : processInstance.getInstances()) {

				Collection<ActiveNodeInfoRS> activeNode = Process.instance()
						.getActiveNodeInfo(instances);

				System.out.println("DEBUG: DefId - "
						+ instances.getDefinitionId() + " : Instance ID : "
						+ instances.getId() + " : StartDate :"
						+ instances.getStartDate());

				for (ActiveNodeInfoRS activeNodeInstance : activeNode) {

					System.out.println("DEBUG: ActiveNodeInfo Name- "
							+ activeNodeInstance.getActiveNode().getName());

				}

			}

		} else {

			System.out.println("DEBUG: No definitions call returned");
			// assertTrue(false);

		}

	}

	@Test
	public void testRenderForm() throws Exception {
		DefinitionsRS def = new DefinitionsRS();
		def.setId("org.jbpm.approval.rewards");

		ProcessDefinitionInstancesRS process = Process.instance()
				.getProcessInstances(def);
		// UserTaskVO ut = new UserTaskVO("admin", "admin");

		// Assert.notNull(ut);
		// Assert.notEmpty(process.getInstances());

		if (process.getInstances() == null) {

			try {

				Process.instance().testStartInstance(def);

			} catch (Exception e) {

				System.out.println("DEBUG: " + e);

			}
		} else {

			String html = Process.instance().getProcessRenderHTML(def);
			Assert.notNull(html);

			System.out.println("DEBUG (Form HTML): " + html);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("employee", "Ally");
			map.put("reason", "Rendering form via REST");

			try {

				renderForm(def, map);

			} catch (Exception e) {

				System.out.println("DEBUG: " + e);

			}
		}
	}

	public void renderForm(DefinitionsRS definitionsRS,
			Map<String, Object> params) throws Exception {

		String response = Process.instance().processComplete(definitionsRS,
				params);

		if (response != null) {

			System.out.println("DEBUG: " + response);

		}
	}

}
