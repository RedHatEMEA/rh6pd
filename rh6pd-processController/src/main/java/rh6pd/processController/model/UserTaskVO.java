package rh6pd.processController.model;

import java.io.Serializable;

public class UserTaskVO implements Serializable {

	private static final long serialVersionUID = -8392014487181046993L;

	private String username;
	private String password;
	private TaskRS taskRS = new TaskRS();

	public TaskRS getTaskRS() {
		return taskRS;
	}

	public void setTaskRS(TaskRS taskRS) {
		this.taskRS = taskRS;
	}

	public UserTaskVO(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
