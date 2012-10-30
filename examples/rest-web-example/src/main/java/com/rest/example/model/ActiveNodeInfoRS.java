package com.rest.example.model;

import java.io.Serializable;

public class ActiveNodeInfoRS implements Serializable {

	private static final long serialVersionUID = 1929213378380137142L;

	private String width;
	private String height;
	private ActiveNodeRS activeNode = new ActiveNodeRS();

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public ActiveNodeRS getActiveNode() {
		return activeNode;
	}

	public void setActiveNode(ActiveNodeRS activeNode) {
		this.activeNode = activeNode;
	}

}