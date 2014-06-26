package org.epnoi.model;

import javax.xml.bind.annotation.XmlElement;

public class InformationSource implements Resource {
	String URI;
	String URL;
	String name;
	String type;
	String informationUnitType;

	// -----------------------------------------------------------------------------

	public String getType() {
		return type;
	}

	// -----------------------------------------------------------------------------

	public void setType(String type) {
		this.type = type;
	}

	// -----------------------------------------------------------------------------
	@XmlElement(name="URI")
	public String getURI() {
		return URI;
	}

	// -----------------------------------------------------------------------------

	public void setURI(String uri) {
		this.URI = uri;
	}

	// -----------------------------------------------------------------------------

	public String getName() {
		return this.name;
	}

	// -----------------------------------------------------------------------------

	public void setName(String name) {
		this.name = name;
	}

	// -----------------------------------------------------------------------------

	public String getURL() {
		return URL;
	}

	// -----------------------------------------------------------------------------

	public void setURL(String URL) {
		this.URL = URL;
	}

	// -----------------------------------------------------------------------------

	public String getInformationUnitType() {
		return informationUnitType;
	}

	public void setInformationUnitType(String informationUnitType) {
		this.informationUnitType = informationUnitType;
	}

	@Override
	public String toString() {
		return "InformationSource [URI=" + URI + ", URL=" + URL + ", name="
				+ name + ", type=" + type + ", informationUnitType="
				+ informationUnitType + "]";
	}

	// -----------------------------------------------------------------------------

	
}
