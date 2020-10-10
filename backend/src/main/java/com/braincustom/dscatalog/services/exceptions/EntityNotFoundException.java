package com.braincustom.dscatalog.services.exceptions;

public class EntityNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	//construtor
	public EntityNotFoundException(String msg) {
		super(msg);
	}

}
