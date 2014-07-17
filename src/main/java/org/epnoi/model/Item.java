package org.epnoi.model;

import javax.xml.bind.annotation.XmlElement;

public class Item implements Resource {

	private String title = "";
	private String description = "";
	private String link;
	private String author = "";
	private String guid = "";
	private String URI;
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
	@XmlElement(name = "URI")
	public String getURI() {
		return this.URI;
	}

	// --------------------------------------------------------------------------

	public void setURI(String URI) {
		this.URI = URI;
	}

	// --------------------------------------------------------------------------

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
				+ ", URI=" + URI + ", pubDate=" + pubDate + ", content="
				+ content + "]";
	}

	// --------------------------------------------------------------------------

	

}