package com.rest.example.manager;

public enum EnumJBPMRestType {

	POST("POST"), GET("GET"), MULTIPART("MULTIPART"), GET_BYTE("GET_BYTE");
	//codigo = code
	public String code;

	private EnumJBPMRestType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
