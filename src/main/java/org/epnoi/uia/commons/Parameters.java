package org.epnoi.uia.commons;

import java.util.HashMap;

public class Parameters<T> {

	protected HashMap<String, T> parameters = new HashMap<String, T>();

	//--------------------------------------------------------------------------------------------------	
	
	public Parameters() {
		super();
	}
	
	//--------------------------------------------------------------------------------------------------

	public void setParameter(String parameter, T value) {
		this.parameters.put(parameter, value);
	}

	//--------------------------------------------------------------------------------------------------
	
	public T getParameterValue(String parameter) {
		return this.parameters.get(parameter);
	}

}