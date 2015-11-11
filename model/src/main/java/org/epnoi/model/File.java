package org.epnoi.model;

public class File implements Resource{
	Long id;
	String uri;
	String resource;
	String title;
	String uploaderURI;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUploaderURI() {
		return uploaderURI;
	}

	public void setUploaderURI(String uploaderURI) {
		this.uploaderURI = uploaderURI;
	}
	
	

	
}
