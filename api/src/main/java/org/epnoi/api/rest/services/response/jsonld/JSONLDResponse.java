package org.epnoi.api.rest.services.response.jsonld;

import org.codehaus.jackson.annotate.JsonProperty;
import org.epnoi.model.DublinCoreMetadataElementsSetHelper;
import org.epnoi.model.ResearchObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JSONLDResponse {

	ResearchObject researchObject;

	@JsonProperty("@context")
	List<Object> context;

	@JsonProperty("describes")
	JSONLDAggregation aggregation;

	private String URI;

	// -----------------------------------------------------------------------------------------------

	public JSONLDResponse(ResearchObject researchObject) {
		this.researchObject = researchObject;
		this.URI = researchObject.getUri() + "/Manifest";

		this.aggregation = new JSONLDAggregation(researchObject);

		this.context = new ArrayList<Object>();
		Map<String, Object> contextTerms = new HashMap<String, Object>();
		contextTerms.put("foaf", "http://xmlns.com/foaf/0.1/");
		contextTerms.put("dc", "http://purl.org/dc/terms/");
		_addDCPropertiesToContext(contextTerms);
		context.add("https://w3id.org/ore/context");
		context.add(contextTerms);

	}

	// -----------------------------------------------------------------------------------------------

	public Map<String, Object> convertToMap() {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("@context", this.context);
		responseMap.put("@id", this.URI);
		responseMap.put("@type",
				new String[] { "ResourceMap", "ResearchObject" });
		responseMap.put("describes", aggregation);

		_addDCProperties(responseMap);
		return responseMap;
	}

	// -----------------------------------------------------------------------------------------------

	private void _addDCPropertiesToContext(Map<String, Object> context) {
		for (String property : this.researchObject.getDcProperties()
				.getProperties()) {

			String propertyName = DublinCoreMetadataElementsSetHelper
					.getPropertyName(property);

			context.put("dc:" + propertyName, property);

		}

	}

	// -----------------------------------------------------------------------------------------------

	private void _addDCProperties(Map<String, Object> responseMap) {
		for (Entry<String, List<String>> dcPropety : researchObject
				.getDcProperties().getDublinCoreProperties().entrySet()) {
			responseMap.put(dcPropety.getKey(), dcPropety.getValue());

		}

	}

	// -----------------------------------------------------------------------------------------------
}
