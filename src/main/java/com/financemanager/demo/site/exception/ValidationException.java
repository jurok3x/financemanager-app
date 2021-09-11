package com.financemanager.demo.site.exception;

public class ValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	
	public ValidationException(String message){
	}

	public String getMessage() {
		return message;
	}
}
