package org.epnoi.model.commons;

import java.util.HashMap;
import java.util.Map;

public class Parameters<T> {

    protected Map<String, T> parameters = new HashMap<String, T>();

    // --------------------------------------------------------------------------------------------------

    public Parameters() {

    }
    // --------------------------------------------------------------------------------------------------

    // --------------------------------------------------------------------------------------------------

    public void setParameter(String parameter, T value) {
        this.parameters.put(parameter, value);
    }

    // --------------------------------------------------------------------------------------------------

    public Map<String, T> getParameters() {
        return this.parameters;
    }

    // --------------------------------------------------------------------------------------------------

    public T getParameterValue(String parameter) {
        return this.parameters.get(parameter);
    }

    // --------------------------------------------------------------------------------------------------

    public Map<String, String> getParametersMap() {
        Map<String, String> parametersMap = new HashMap<>();
        for (Map.Entry<String, T> entry : this.parameters.entrySet()) {
            parametersMap.put(entry.getKey(), entry.getValue().toString());
        }
        return parametersMap;
    }

    @Override
    public String toString() {
        /*
        String expression = " \n ---------------------------------------------------------------------------------------------------------------------";
		for (Entry<String, T> parametersEntry : parameters.entrySet()) {
			expression += " \n " + parametersEntry.getKey() + " -> "
					+ parametersEntry.getValue() + " ";
		}
		return expression + " \n ---------------------------------------------------------------------------------------------------------------------";
*/
        return this.parameters.toString();
    }

}