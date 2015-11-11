package org.epnoi.model;

public class Item implements Resource {

	private String title = "";
	private String description = "";
	private String link;
	private String author = "";
	private String guid = "";
	private String uri;
	private String pubDate = "";
	private String content = "";

	// --------------------------------------------------------------------------

	public void setTitle(String title) {
		this.title = title;
	}

	// --------------------------------------------------------------------------

	public String getDescription() {
		return description;
	}

	// --------------------------------------------------------------------------

	public void setDescription(String description) {
		this.description = description;
	}

	// --------------------------------------------------------------------------

	public String getLink() {
		return link;
	}

	// --------------------------------------------------------------------------

	public void setLink(String link) {
		this.link = link;
	}

	// --------------------------------------------------------------------------

	public String getAuthor() {
		return author;
	}

	// --------------------------------------------------------------------------

	public void setAuthor(String author) {
		this.author = author;
	}

	// --------------------------------------------------------------------------

	public String getGuid() {
		return guid;
	}

	// --------------------------------------------------------------------------

	public void setGuid(String guid) {
		this.guid = guid;
	}



	// --------------------------------------------------------------------------

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getPubDate() {
		return pubDate;
	}

	// --------------------------------------------------------------------------

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	// --------------------------------------------------------------------------

	public String getContent() {
		return content;
	}

	// --------------------------------------------------------------------------

	public void setContent(String content) {
		this.content = content;
	}

	// --------------------------------------------------------------------------

	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return "Item [title=" + title + ", description=" + description
				+ ", link=" + link + ", author=" + author + ", guid=" + guid
				+ ", URI=" + uri + ", pubDate=" + pubDate + ", content="
				+ content + "]";
	}

	public String getURI() {
		// TODO Auto-generated method stub
		return null;
	}

	// --------------------------------------------------------------------------

	

}