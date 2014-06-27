package org.epnoi.model;

import java.util.List;

public class Paper implements Resource {
	String URI;
	List<String> authors;
	String description;
	String title;

	@Override
	public String getURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setURI(String URI) {
		// TODO Auto-generated method stub

	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
