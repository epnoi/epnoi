package org.epnoi.api.rest.services.response.jsonld;

import org.epnoi.model.ResearchObject;

public class JSONLDResearchObjectResponseBuilder {

	public static JSONLDResponse build(ResearchObject researchObject) {
		JSONLDResponse researchObjectResponse = new JSONLDResponse(researchObject);
		return researchObjectResponse;
	}

}
