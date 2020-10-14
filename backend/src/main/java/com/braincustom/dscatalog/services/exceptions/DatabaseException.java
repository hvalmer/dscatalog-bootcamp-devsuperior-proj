package com.braincustom.dscatalog.services.exceptions;

public class DatabaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	//construtor
	public DatabaseException(String msg) {
		super(msg);
	}

}
