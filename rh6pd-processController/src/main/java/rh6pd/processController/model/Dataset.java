package rh6pd.processController.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Dataset implements Serializable {

	private String datasetRef;
	private ArrayList<DatasetValues> data;
	
	public String getDatasetRef() {
		return datasetRef;
	}
	public void setDatasetRef(String datasetRef) {
		this.datasetRef = datasetRef;
	}
	public ArrayList<DatasetValues> getData() {
		return data;
	}
	public void setData(ArrayList<DatasetValues> data) {
		this.data = data;
	}
}
