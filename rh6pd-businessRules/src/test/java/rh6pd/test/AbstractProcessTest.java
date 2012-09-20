package rh6pd.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.DecisionTableFactory;
import org.drools.io.ResourceFactory;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.process.WorkItem;
import org.jbpm.test.JbpmJUnitTestCase;

public abstract class AbstractProcessTest extends JbpmJUnitTestCase {
	private StatefulKnowledgeSession session;
	private final HashMap<String, Object> props = new HashMap<String, Object>();
	private boolean isStarted = false;

	public String convertDecisionTableToRuleFile(String decisionTableFileName)
			throws IOException {
		DecisionTableConfiguration decisionTableConfiguration = KnowledgeBuilderFactory
				.newDecisionTableConfiguration();
		decisionTableConfiguration.setInputType(DecisionTableInputType.XLS);

		String rule = DecisionTableFactory.loadFromInputStream(
				new ClassPathResource(decisionTableFileName).getInputStream(),
				decisionTableConfiguration);

		return rule;
	}

	protected void createNewSession(String... bpmnFiles) {
		this.isStarted = false;
		this.session = this.createKnowledgeSession(bpmnFiles);
	}

	// protected void insertWorkItemParameters(String name, String value) {
	// // TODO Auto-generated method stub
	//
	// }

	protected void insertVar(String name, Object o) {
		if (this.session == null) {
			throw new RuntimeException(
					"Cannot insert variable, no session has been created.");
		}

		if (this.isStarted) {
			throw new RuntimeException(
					"Cannot insert variables after the process has started.");
		} else {
			this.session.insert(o);
			this.props.put(name, o);
		}
	}

	protected void printBpmnErrors(String... listBpmnFiles) {
		KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();

		for (String bpmnFile : listBpmnFiles) {
			kb.add(ResourceFactory.newClassPathResource(bpmnFile),
					ResourceType.BPMN2);
		}

		KnowledgeBuilderErrors errs = kb.getErrors();

		for (KnowledgeBuilderError err : errs) {
			System.out.println(err.toString());
		}
	}

	protected void testHumanTask(String formFileName, String processId,
			String workItemName, Map humanTaskParameters,
			Map contentParameters, String... nodes) {
		TestWorkItemHandler testWorkItemHandler = new TestWorkItemHandler();
		this.session.getWorkItemManager().registerWorkItemHandler(workItemName,
				testWorkItemHandler);

		/**
		 * //add a freemarker resource type to be able to add the form to the
		 * classpath. if(formFileName!=null && ! formFileName.isEmpty()){
		 * KnowledgeBuilder kbuilder =
		 * KnowledgeBuilderFactory.newKnowledgeBuilder(); kbuilder.add(new
		 * ClassPathResource(formFileName), FtlResourceType.FTL);
		 * session.getKnowledgeBase
		 * ().addKnowledgePackages(kbuilder.getKnowledgePackages()); }
		 **/

		ProcessInstance processInstance = this.session.startProcess(processId,
				this.props);

		// check if the process instance has started
		this.assertProcessInstanceActive(processInstance.getId(), this.session);
		// assertNodeTriggered(processInstance.getId(), nodes);

		// assert Human Task Parameters
		WorkItem workItem = testWorkItemHandler.getWorkItem();
		Assert.assertNotNull(workItem);
		Assert.assertEquals("Human Task", workItem.getName());
		Assert.assertEquals(workItem.getParameter("ActorId"),
				humanTaskParameters.get("ActorId"));
		Assert.assertEquals(workItem.getParameter("GroupId"),
				humanTaskParameters.get("GroupId"));
		Assert.assertEquals(workItem.getParameter("TaskName"),
				humanTaskParameters.get("TaskName"));

		HashMap<String, String> actualContent = (HashMap<String, String>) humanTaskParameters
				.get("Content");
		HashMap<String, String> expectedContent = (HashMap<String, String>) workItem
				.getParameter("Content");
		if (actualContent != null) {
			int contentSize = actualContent.size();
			for (int i = 1; i <= contentSize; i++) {
				String key = "content" + i;
				String contentValue = (String) contentParameters.get(key);
				Assert.assertEquals(expectedContent.get(contentValue),
						actualContent.get(contentValue));
			}
		}

		// notify the engine the humanTask has been executed
		this.session.getWorkItemManager().abortWorkItem(workItem.getId());

		// check if processInstance has been successfully executed
		this.assertProcessInstanceCompleted(processInstance.getId(),
				this.session);
		this.assertNodeTriggered(processInstance.getId(), nodes);

	}

	public void testProcess(String ruleFileName, String decisionTableFileName,
			String processId, String... requiredNodes) {
		ProcessInstance processInstance = this.session.startProcess(processId,
				this.props);

		if (decisionTableFileName == null) {

			if ((ruleFileName != null) && !ruleFileName.isEmpty()) {
				KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
						.newKnowledgeBuilder();
				kbuilder.add(new ClassPathResource(ruleFileName),
						ResourceType.DRL);
				this.session.getKnowledgeBase().addKnowledgePackages(
						kbuilder.getKnowledgePackages());
				this.session.fireAllRules();
			}

		}

		else {
			DecisionTableConfiguration decisionTableConfiguration = KnowledgeBuilderFactory
					.newDecisionTableConfiguration();
			decisionTableConfiguration.setInputType(DecisionTableInputType.XLS);
			KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory
					.newKnowledgeBuilder();
			knowledgeBuilder.add(new ClassPathResource(decisionTableFileName),
					ResourceType.DTABLE, decisionTableConfiguration);
			if (knowledgeBuilder.hasErrors()) {
				System.out.println("KnowledgeBuilder Errors: "
						+ knowledgeBuilder.getErrors().toString());
			}
			this.session.getKnowledgeBase().addKnowledgePackages(
					knowledgeBuilder.getKnowledgePackages());
			this.session.fireAllRules();

		}

		// check whether the process instance has completed successfully
		// assertProcessInstanceCompleted(processInstance.getId(), session);
		this.assertNodeTriggered(processInstance.getId(), requiredNodes);
	}

}
