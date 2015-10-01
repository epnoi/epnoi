package org.epnoi.model.parameterization;

public class RSSFeedParameters {

	private String name;
	private String URI;
	private String URL;
	private Integer interval;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	@Override
	public String toString() {
		return "RSSFeedParameters [name=" + name + ", URI=" + URI + ", URL="
				+ URL + ", interval=" + interval + "]";
	}

}
