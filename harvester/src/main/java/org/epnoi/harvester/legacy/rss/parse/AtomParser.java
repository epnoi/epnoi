package org.epnoi.harvester.legacy.rss.parse;

import org.epnoi.model.Feed;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class AtomParser {
	static final String TITLE = "title";
	static final String DESCRIPTION = "description";
	static final String ITEM = "feed";
	static final String LANGUAGE = "language";
	static final String RIGHTS = "rights";
	static final String LINK = "link";
	static final String AUTHOR = "author";
	static final String ENTRY = "entry";
	static final String PUBLISHED = "published";
	static final String ID = "id";

	final URL url;

	public AtomParser(String feedUrl) {
		try {
			this.url = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public Feed readFeed() {
		Feed feed = null;
		try {
			boolean isFeedHeader = true;
			// Set header values intial to the empty string
			String description = "";
			String title = "";
			String link = "";
			String language = "";
			String copyright = "";
			String author = "";
			String pubdate = "";
			String guid = "";

			// First create a new XMLInputFactory
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// Setup a new eventReader
			InputStream in = read();
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			// Read the XML document
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					// Start
					// element--------------------------------------------------------------------
					String localPart = event.asStartElement().getName()
							.getLocalPart();
					if (ENTRY.equals(localPart)) {

						if (isFeedHeader) {
							isFeedHeader = false;
							feed = new Feed(title, link, description, language,
									copyright, pubdate);
						}
						event = eventReader.nextEvent();
					} else if (TITLE.equals(localPart)) {

						title = getCharacterData(event, eventReader);

					} else if (DESCRIPTION.equals(localPart)) {
						description = getCharacterData(event, eventReader);
					} else if (LINK.equals(localPart)) {
						link = getCharacterData(event, eventReader);
					} else if (ID.equals(localPart)) {
						guid = getCharacterData(event, eventReader);
					} else if (LANGUAGE.equals(localPart)) {
						language = getCharacterData(event, eventReader);
					} else if (AUTHOR.equals(localPart)) {
						author = getCharacterData(event, eventReader);
					} else if (PUBLISHED.equals(localPart)) {
						pubdate = getCharacterData(event, eventReader);
					} else if (RIGHTS.equals(localPart)) {
						copyright = getCharacterData(event, eventReader);
					}

				} else if (event.isEndElement()) {
					// End
					// element--------------------------------------------------------------------
					
					/*PARTE QUITADA
					if (event.asEndElement().getName().getLocalPart() == (ITEM)) {
						FeedMessage message = new FeedMessage();
						message.setAuthor(author);
						message.setDescription(description);
						message.setGuid(guid);
						message.setLink(link);
						message.setTitle(title);
						feed.getMessages().add(message);
						event = eventReader.nextEvent();
						continue;
					}
					*/
				}
			}
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
		return feed;
	}

	private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
			throws XMLStreamException {
		String result = "";
		event = eventReader.nextEvent();
		if (event instanceof Characters) {
			result = event.asCharacters().getData();
		}
		return result;
	}

	private InputStream read() {
		try {
			return url.openStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}