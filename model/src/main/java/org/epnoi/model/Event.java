package org.epnoi.model;

public class Event {
	private String message = null;

	public Event(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Event [message=" + message + "]";
	}

}
