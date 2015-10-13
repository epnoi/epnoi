package org.epnoi.model;

import java.util.ArrayList;
import java.util.List;

public class ResearchObject implements Resource {

	private DublinCoreMetadataElementsSet dcProperties;

	private String uri;
	private List<String> aggregatedResources;
	
	// --------------------------------------------------------------------------

	public ResearchObject() {
		this.aggregatedResources = new ArrayList<String>();
		this.dcProperties = new DublinCoreMetadataElementsSet();
	}

	// --------------------------------------------------------------------------

	public String getUri() {
		return uri;
	}

	// --------------------------------------------------------------------------

	public void setUri(String uRI) {
		uri = uRI;
	}

	// --------------------------------------------------------------------------

	public List<String> getAggregatedResources() {
		return aggregatedResources;
	}

	// --------------------------------------------------------------------------

	public void setAggregatedResources(List<String> aggregatedResources) {
		this.aggregatedResources = aggregatedResources;
	}

	// --------------------------------------------------------------------------

	public DublinCoreMetadataElementsSet getDcProperties() {
		return dcProperties;
	}

	// --------------------------------------------------------------------------

	public void setDcProperties(DublinCoreMetadataElementsSet dcProperties) {
		this.dcProperties = dcProperties;
	}

	// --------------------------------------------------------------------------

	@Override
	public String toString() {
		return "ResearchObject [dcProperties=" + dcProperties + ", uri=" + uri
				+ ", resources=" + aggregatedResources + "]";
	}
	// --------------------------------------------------------------------------

	
}
