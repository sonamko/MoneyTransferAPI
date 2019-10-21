package org.revolut.moneytransfer.model;

import javax.ws.rs.core.Response.Status;

public class CustomResponse {
	private Object obj;
	private Status responseCode;
	
	
	public CustomResponse(Object obj, Status responseCode) {
		this.obj = obj;
		this.responseCode = responseCode;
	}
	
	public Object getObject() {
		return obj;
	}
	public void setObject(Object object) {
		this.obj = object;
	}
	public Status getResponse() {
		return responseCode;
	}
	public void setResponse(Status responseCode) {
		this.responseCode = responseCode;
	} 
	
	
	

}
