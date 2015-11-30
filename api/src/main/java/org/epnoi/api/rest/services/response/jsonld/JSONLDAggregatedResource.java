package org.epnoi.api.rest.services.response.jsonld;

import org.codehaus.jackson.annotate.JsonProperty;

public class JSONLDAggregatedResource {
	@JsonProperty("@type")
	private String type = "AggregatedResource";
	
	@JsonProperty("@id")
	private String URI;
	
	
	// --------------------------------------------------------------------------------
	
	public JSONLDAggregatedResource(String resourceURI) {
		this.URI=resourceURI;
	}
	
	// --------------------------------------------------------------------------------

	public String getType() {
		return type;
	}

	// --------------------------------------------------------------------------------
	
	public void setType(String type) {
		this.type = type;
	}

	// --------------------------------------------------------------------------------
	
	public String getURI() {
		return URI;
	}
	
	// --------------------------------------------------------------------------------

	public void setURI(String uRI) {
		URI = uRI;
	}
	
	// --------------------------------------------------------------------------------
}
