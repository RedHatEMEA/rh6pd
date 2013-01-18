package rh6pd.processController.service;

import java.util.ArrayList;
import java.util.Collection;

import rh6pd.processController.model.TaskRS;

public class GroupParticipationTasks {
	private static final long serialVersionUID = 3063536327762217431L;

	private final ArrayList<TaskRS> tasks = new ArrayList<TaskRS>();

	public ArrayList<TaskRS> getTasks() {
		return tasks;
	}

	public void setTasks(Collection<TaskRS> tasks) {
		this.tasks.clear();
		this.tasks.addAll(tasks);
	}

}
