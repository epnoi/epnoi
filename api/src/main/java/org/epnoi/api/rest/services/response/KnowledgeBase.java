package org.epnoi.api.rest.services.response;

import java.util.HashMap;
import java.util.Map;

public class KnowledgeBase {
	private boolean status;
	private Map<String, String> parameters = new HashMap<String, String>();

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Map<String, String> getParameters() {
		return this.parameters;
	}


}
