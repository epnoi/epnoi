package org.epnoi.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class InformationSourceSubscription implements Resource {

	String URI;
	String informationSource;
	List<String> keywords = new ArrayList<String>();
	Integer numberOfItems;

	// -----------------------------------------------------------------------------

	public Integer getNumberOfItems() {
		return numberOfItems;
	}

	public void setNumberOfItems(Integer numberOfResults) {
		this.numberOfItems = numberOfResults;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String getInformationSource() {
		return informationSource;
	}

	// -----------------------------------------------------------------------------

	public void setInformationSource(String informationSource) {
		this.informationSource = informationSource;
	}

	// -----------------------------------------------------------------------------
	@XmlElement(name = "URI")
	public String getURI() {
		return URI;
	}

	// -----------------------------------------------------------------------------

	public void setURI(String uri) {
		this.URI = uri;
	}

	// -----------------------------------------------------------------------------

	@Override
	public String toString() {
		return "InformationSourceSubscription [URI=" + URI
				+ ", informationSource=" + informationSource + ", keywords="
				+ keywords + ", numberOfResults=" + numberOfItems + "]";
	}

	// -----------------------------------------------------------------------------

}
