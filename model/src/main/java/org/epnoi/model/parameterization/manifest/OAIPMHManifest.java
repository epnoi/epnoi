package org.epnoi.model.parameterization.manifest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "manifest")
public class OAIPMHManifest extends Manifest {

	String URL;
	String repository;

	public String getURL() {
		return URL;
	}

	public void setURL(String url) {
		this.URL = url;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

}
