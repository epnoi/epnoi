package org.epnoi.model.exceptions;

public class EpnoiInitializationException extends Exception {

	public EpnoiInitializationException(String message) {
		super(message);

	}

	public EpnoiInitializationException(String message, Exception cause){
		super(message,cause);
	}

}
