package org.revolut.moneytransfer.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorEntity {
	
	private String errorMsg;

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	

}
