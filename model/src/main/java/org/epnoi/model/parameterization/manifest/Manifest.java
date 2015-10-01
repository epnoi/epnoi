package org.epnoi.model.parameterization.manifest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "manifest")
public class Manifest {

	String URI;
	String name;
	String URL;
	Integer interval;

	// ----------------------------------------------------------------------------------------

	public String getURL() {
		return URL;
	}

	// ----------------------------------------------------------------------------------------

	public void setURL(String uRL) {
		URL = uRL;
	}

	// ----------------------------------------------------------------------------------------

	public String getURI() {
		return URI;
	}

	// ----------------------------------------------------------------------------------------

	public void setURI(String uri) {
		this.URI = uri;
	}

	public String getName() {
		return name;
	}

	// ----------------------------------------------------------------------------------------

	public void setName(String name) {
		this.name = name;
	}

	// ----------------------------------------------------------------------------------------

	public Integer getInterval() {
		return interval;
	}

	// ----------------------------------------------------------------------------------------

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	// ----------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "Manifest [URI=" + URI + ", name=" + name + ", URL=" + URL
				+ ", interval=" + interval + "]";
	}

}
