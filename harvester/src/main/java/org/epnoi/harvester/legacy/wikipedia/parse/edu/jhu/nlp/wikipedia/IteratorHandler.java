package org.epnoi.harvester.legacy.wikipedia.parse.edu.jhu.nlp.wikipedia;

public class IteratorHandler implements PageCallbackHandler {

	private WikiXMLParser parser = null;
	
	public IteratorHandler(WikiXMLParser myParser) {
		parser = myParser;
	}
	
	public void processWikipediaPage(WikiPage page) {
		parser.notifyPage(page);
	}

}
