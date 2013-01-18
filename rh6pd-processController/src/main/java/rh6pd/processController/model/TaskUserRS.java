package rh6pd.processController.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class TaskUserRS implements Serializable {
	private final ArrayList<TaskRS> tasks = new ArrayList<TaskRS>();

	public ArrayList<TaskRS> getTasks() {
		return tasks;
	}

	public void setTasks(Collection<TaskRS> tasks) {
		this.tasks.clear();
		this.tasks.addAll(tasks);
	}
}
