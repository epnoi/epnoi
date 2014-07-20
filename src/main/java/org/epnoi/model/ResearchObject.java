package org.epnoi.model;

import java.util.ArrayList;
import java.util.List;

public class ResearchObject implements Resource {

	private DublinCoreMetadataElementsSet dcProperties;

	private String URI;
	private List<String> aggregatedResources;

	public ResearchObject() {
		this.aggregatedResources = new ArrayList<String>();
		this.dcProperties = new DublinCoreMetadataElementsSet();
	}

	// --------------------------------------------------------------------------

	public String getURI() {
		return URI;
	}

	// --------------------------------------------------------------------------

	public void setURI(String uRI) {
		URI = uRI;
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

	public DublinCoreMetadataElementsSet getDCProperties() {
		return dcProperties;
	}

	// --------------------------------------------------------------------------

	public void setDCProperties(DublinCoreMetadataElementsSet dcProperties) {
		this.dcProperties = dcProperties;
	}

	// --------------------------------------------------------------------------

	@Override
	public String toString() {
		return "ResearchObject [dcProperties=" + dcProperties + ", URI=" + URI
				+ ", resources=" + aggregatedResources + "]";
	}
	// --------------------------------------------------------------------------
}
