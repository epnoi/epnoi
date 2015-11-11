package org.epnoi.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InformationSourceNotification implements Resource {
	
	private String uri;
	private Resource resource;
	private String timestamp;
	private String informationSource;

	// ----------------------------------------------------------------------

	@Override
	public String toString() {
		return "InformationSourceNotification [URI=" + uri + ", resource="
				+ resource + ", timestamp=" + timestamp + "]";
	}

	public String getUri() {
		return uri;
	}

	// ----------------------------------------------------------------------

	public void setUri(String uRI) {
		uri = uRI;
	}

	// ----------------------------------------------------------------------

	public Resource getResource() {
		return resource;
	}

	// ----------------------------------------------------------------------

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	// ----------------------------------------------------------------------

	public String getTimestamp() {
		return timestamp;
	}

	// ----------------------------------------------------------------------

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	// ----------------------------------------------------------------------
	
	public String getInformationSource() {
		return informationSource;
	}
	
	// ----------------------------------------------------------------------
	
	public void setInformationSource(String informationSource) {
		this.informationSource = informationSource;
	}

}
