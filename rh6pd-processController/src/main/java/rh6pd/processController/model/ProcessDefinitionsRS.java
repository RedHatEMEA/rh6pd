package rh6pd.processController.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProcessDefinitionsRS implements Serializable {

	private static final long serialVersionUID = -555493363623066337L;

	private Collection<DefinitionsRS> definitions = new ArrayList<DefinitionsRS>();

	public Collection<DefinitionsRS> getDefinitions() {
		return definitions;
	}

	public void setDefinitions(List<DefinitionsRS> definitions) {
		this.definitions = definitions;
	}

}