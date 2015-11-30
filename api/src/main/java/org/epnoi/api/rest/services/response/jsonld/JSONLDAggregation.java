package org.epnoi.api.rest.services.response.jsonld;

import org.codehaus.jackson.annotate.JsonProperty;
import org.epnoi.model.ResearchObject;

import java.util.ArrayList;
import java.util.List;

public class JSONLDAggregation {
	@JsonProperty("@type")
	String type = "Aggregation";

	@JsonProperty("aggregates")
	List<JSONLDAggregatedResource> aggregates;

	@JsonProperty("isDescribedBy")
	String isDescribedBy = "http://elResourceMap";

	// --------------------------------------------------------------------------------

	public JSONLDAggregation(ResearchObject researchObject) {
		this.aggregates=new ArrayList<JSONLDAggregatedResource>();
		for(String resourceURI:researchObject.getAggregatedResources()){
			JSONLDAggregatedResource aggregatedResource = new JSONLDAggregatedResource(resourceURI);
			this.aggregates.add(aggregatedResource);
		}
	}

	public String getType() {
		return type;
	}

	// --------------------------------------------------------------------------------

	public void setType(String type) {
		this.type = type;
	}

	// --------------------------------------------------------------------------------

	public List<JSONLDAggregatedResource> getAggregates() {
		return aggregates;
	}

	// --------------------------------------------------------------------------------

	public void setAggregates(List<JSONLDAggregatedResource> aggregates) {
		this.aggregates = aggregates;
	}
	
	// --------------------------------------------------------------------------------

	public String getIsDescribedBy() {
		return isDescribedBy;
	}
	
	// --------------------------------------------------------------------------------

	public void setIsDescribedBy(String isDescribedBy) {
		this.isDescribedBy = isDescribedBy;
	}

	// --------------------------------------------------------------------------------
}
