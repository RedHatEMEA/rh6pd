package com.rest.example.model;

import java.io.Serializable;

public class TaskRS implements Serializable {

	private static final long serialVersionUID = 117065476711370847L;

	private String id;
	private String processInstanceId;
	private String processId;
	private String name;
	private String assignee;
	private String isBlocking;
	private String isSignalling;
	// private outcomes;
	private String currentState;
	// private participantUsers
	// private participantGroups
	private String url;
	private String priority;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getIsBlocking() {
		return isBlocking;
	}

	public void setIsBlocking(String isBlocking) {
		this.isBlocking = isBlocking;
	}

	public String getIsSignalling() {
		return isSignalling;
	}

	public void setIsSignalling(String isSignalling) {
		this.isSignalling = isSignalling;
	}

	public String getCurrentState() {
		return currentState;
	}

	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

}