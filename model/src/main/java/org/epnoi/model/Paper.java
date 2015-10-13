package org.epnoi.model;

import java.util.ArrayList;
import java.util.List;

public class Paper implements Resource {
	String uri;
	List<String> authors;
	String description;
	String title;
	private String pubDate;

	// -------------------------------------------------------------------------------------------------------------

	public Paper() {
		this.authors = new ArrayList<String>();
	}
	
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public String getUri() {

		return this.uri;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Override
	public void setUri(String uri) {
		this.uri = uri;
	}

	// -------------------------------------------------------------------------------------------------------------

	public List<String> getAuthors() {
		return authors;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	// -------------------------------------------------------------------------------------------------------------

	public String getDescription() {
		return description;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void setDescription(String description) {
		this.description = description;
	}

	// -------------------------------------------------------------------------------------------------------------

	public String getTitle() {
		return title;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void setTitle(String title) {
		this.title = title;
	}

	// -------------------------------------------------------------------------------------------------------------

	public String getPubDate() {
		return pubDate;
	}

	// -------------------------------------------------------------------------------------------------------------

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	@Override
	public String toString() {
		return "Paper [URI=" + uri + ", authors=" + authors + ", description="
				+ description + ", title=" + title + ", pubDate=" + pubDate
				+ "]";
	}

}
