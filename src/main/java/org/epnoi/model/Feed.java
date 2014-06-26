package org.epnoi.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Feed implements Resource{

	private String title;
	private String link;
	private String description;
	private String language;
	private String copyright;
	private String pubDate;
	private String URI;

	
	private List<Item> items = new ArrayList<Item>();

	//--------------------------------------------------------------------------------
	
	public Feed(){
		this.items = new ArrayList<Item>();
	}

	//--------------------------------------------------------------------------------
	
	public Feed(String title, String link, String description, String language,
			String copyright, String pubDate) {
		this.title = title;
		this.link = link;
		this.description = description;
		this.language = language;
		this.copyright = copyright;
		System.out.println("creation pubDate>  "+pubDate);
		this.pubDate = pubDate;
		
	}

	//--------------------------------------------------------------------------------

	public List<Item> getItems() {
		return items;
	}

	//--------------------------------------------------------------------------------
	
	public String getTitle() {
		return title;
	}
	
	//--------------------------------------------------------------------------------

	public String getLink() {
		return link;
	}

	//--------------------------------------------------------------------------------
	
	public String getDescription() {
		return description;
	}
	
	//--------------------------------------------------------------------------------

	public String getLanguage() {
		return language;
	}

	//--------------------------------------------------------------------------------

	public String getCopyright() {
		return copyright;
	}

	//--------------------------------------------------------------------------------

	public String getPubDate() {
		return pubDate;
	}
	
	// --------------------------------------------------------------------------
	@XmlElement(name="URI")
	public String getURI() {
		return URI;
	}
	
	// --------------------------------------------------------------------------

	public void setURI(String uRI) {
		URI = uRI;
	}
	
	// --------------------------------------------------------------------------

	public void setTitle(String title) {
		this.title = title;
	}

	// --------------------------------------------------------------------------
	
	public void setLink(String link) {
		this.link = link;
	}
	
	// --------------------------------------------------------------------------

	public void setDescription(String description) {
		this.description = description;
	}

	// --------------------------------------------------------------------------
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
	// --------------------------------------------------------------------------

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	// --------------------------------------------------------------------------

	public void setPubDate(String pubDate) {
		
		System.out.println("Este era "+this.pubDate+" y esta le meto "+pubDate);
		this.pubDate = pubDate;
	}

	// --------------------------------------------------------------------------
	
	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	// --------------------------------------------------------------------------
	
	public void addItem(Item item){
		this.items.add(item);
	}

	// --------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "Feed [URI="+this.URI+", copyright=" + copyright + ", description=" + description
				+ ", language=" + language + ", link=" + link + ", pubDate="
				+ pubDate + ", title=" + title + "]";
	}

}