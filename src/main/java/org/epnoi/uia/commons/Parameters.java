package org.epnoi.uia.commons;

import java.util.HashMap;

public class Parameters {

	private HashMap<String, Object> parameters = new HashMap<String, Object>();

	public Parameters() {
		super();
	}

	public void setParameter(String parameter, Object value) {
		this.parameters.put(parameter, value);
	
	}

	public Object getParameterValue(String parameter) {
		return this.parameters.get(parameter);
	}

}