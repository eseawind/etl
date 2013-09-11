package org.intercom.dao.exceptions;

public class DAOLayerException extends Exception {

	private static final long serialVersionUID = 1L;

	private String message;

	public DAOLayerException(String message) {
		// call super class constructor
		super(message); 
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
