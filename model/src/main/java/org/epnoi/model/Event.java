package org.epnoi.model;

import lombok.Data;

@Data
public class Event {

	private String message = null;

	public Event(String message) {
		this.message = message;
	}

}
