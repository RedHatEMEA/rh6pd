package rh6pd.processController.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class ProcessDefinitionInstancesRS implements Serializable {

	private static final long serialVersionUID = -9158303496971170892L;

	private Collection<InstancesRS> instances = new ArrayList<InstancesRS>();

	public Collection<InstancesRS> getInstances() {
		return instances;
	}

	public void setInstances(Collection<InstancesRS> instances) {
		this.instances = instances;
	}
}