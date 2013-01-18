package rh6pd.processController.model;

import java.io.Serializable;

public class InstancesRS implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8369132192357526352L;

	private String id;
	private String definitionId;
	private String startDate;
	private String suspended;
	private RootTokenRS rootToken = new RootTokenRS();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDefinitionId() {
		return definitionId;
	}

	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getSuspended() {
		return suspended;
	}

	public void setSuspended(String suspended) {
		this.suspended = suspended;
	}

	public RootTokenRS getRootToken() {
		return rootToken;
	}

	public void setRootToken(RootTokenRS rootToken) {
		this.rootToken = rootToken;
	}

}
