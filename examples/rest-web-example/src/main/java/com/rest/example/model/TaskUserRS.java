package com.rest.example.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class TaskUserRS implements Serializable {

	private static final long serialVersionUID = 3063536387762217431L;

	private Collection<TaskRS> tasks = new ArrayList<TaskRS>();

	public Collection<TaskRS> getTasks() {
		return tasks;
	}

	public void setTasks(Collection<TaskRS> tasks) {
		this.tasks = tasks;
	}

}
