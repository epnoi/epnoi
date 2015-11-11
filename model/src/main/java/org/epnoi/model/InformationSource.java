package org.epnoi.model;

public class InformationSource implements Resource {
	String uri;
	String URL;
	String name;
	String type;
	String informationUnitType;

	// -----------------------------------------------------------------------------

	public String getType() {
		return type;
	}

	// -----------------------------------------------------------------------------


	// -----------------------------------------------------------------------------

	public String getName() {
		return this.name;
	}

	// -----------------------------------------------------------------------------

	public void setName(String name) {
		this.name = name;
	}



	// -----------------------------------------------------------------------------

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInformationUnitType() {
		return informationUnitType;
	}

	public void setInformationUnitType(String informationUnitType) {
		this.informationUnitType = informationUnitType;
	}

	@Override
	public String toString() {
		return "InformationSource [URI=" + uri + ", URL=" + URL + ", name="
				+ name + ", type=" + type + ", informationUnitType="
				+ informationUnitType + "]";
	}

	// -----------------------------------------------------------------------------

	
}
