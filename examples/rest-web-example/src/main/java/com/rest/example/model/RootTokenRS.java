package com.rest.example.model;

import java.io.Serializable;

public class RootTokenRS implements Serializable {

	private static final long serialVersionUID = 8061792768746476868L;

	private String id;
	private String name;
	private String currentNodeName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrentNodeName() {
		return currentNodeName;
	}

	public void setCurrentNodeName(String currentNodeName) {
		this.currentNodeName = currentNodeName;
	}

}