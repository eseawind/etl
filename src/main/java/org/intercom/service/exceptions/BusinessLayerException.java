package org.intercom.service.exceptions;

public class BusinessLayerException extends Exception {

	private static final long serialVersionUID = 1L;

	private String message;

	public BusinessLayerException(String message) {
		// call super class constructor
		super(message); 
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
