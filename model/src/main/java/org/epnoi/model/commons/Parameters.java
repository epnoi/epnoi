package org.epnoi.model.commons;

import java.util.HashMap;
import java.util.Map;

public class Parameters<T> {
	
	protected HashMap<String, T> parameters = new HashMap<String, T>();

	// --------------------------------------------------------------------------------------------------

	public Parameters() {
		super();
	}

	// --------------------------------------------------------------------------------------------------

	public void setParameter(String parameter, T value) {
		this.parameters.put(parameter, value);
	}
	
	// --------------------------------------------------------------------------------------------------

	public Map<String, T> getParameters(){
		return this.parameters;
	}

	// --------------------------------------------------------------------------------------------------

	public T getParameterValue(String parameter) {
		return this.parameters.get(parameter);
	}

	// --------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		/*
		String expression = " \n ---------------------------------------------------------------------------------------------------------------------";
		for (Entry<String, T> parametersEntry : parameters.entrySet()) {
			expression += " \n " + parametersEntry.getKey() + " -> "
					+ parametersEntry.getValue() + " ";
		}
		return expression + " \n ---------------------------------------------------------------------------------------------------------------------";
*/return this.parameters.toString();
	}

}