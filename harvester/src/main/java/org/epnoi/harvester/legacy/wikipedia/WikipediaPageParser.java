package org.epnoi.harvester.legacy.wikipedia;

import de.tudarmstadt.ukp.wikipedia.parser.Content;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import de.tudarmstadt.ukp.wikipedia.parser.Section;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParserFactory;
import org.epnoi.harvester.legacy.wikipedia.parse.edu.jhu.nlp.wikipedia.WikiPage;
import org.epnoi.model.Context;
import org.epnoi.model.WikipediaPage;
import org.epnoi.model.commons.StringUtils;

public class WikipediaPageParser {
	private static final String templateRegExp = "TEMPLATE\\[";
	private MediaWikiParserFactory mediaWikiParserFactory;
	private MediaWikiParser mediaWikiParser;
	
	
	public WikipediaPageParser(){
		this.mediaWikiParserFactory = new MediaWikiParserFactory();
		mediaWikiParser = mediaWikiParserFactory.createParser();
	}
	
	
	// -------------------------------------------------------------------------------------------------------------------

	public WikipediaPage parse(WikiPage page) {
		ParsedPage parsedPage = mediaWikiParser.parse(page.getWikiText());

		WikipediaPage wikipediaPage = new WikipediaPage();
		Context context = new Context();

		_parseWikipediaPageProperties(page, wikipediaPage);

		_parseWikipediaPageSections(parsedPage, wikipediaPage);
		return wikipediaPage;
	}

	// -------------------------------------------------------------------------------------------------------------------

	private void _parseWikipediaPageProperties(WikiPage page, WikipediaPage wikipediaPage) {
		String cleanedPageTitle = page.getTitle().replaceAll("\\n", "").replaceAll("\\s+$", "");

		String localPartOfTermURI = StringUtils.cleanOddCharacters(page.getTitle());

		localPartOfTermURI = localPartOfTermURI.replaceAll("\\n", "").replaceAll("\\s+$", "").replaceAll("\\s+", "_");

		wikipediaPage.setUri(WikipediaHarvester.wikipediaPath + localPartOfTermURI);
		wikipediaPage.setTerm(cleanedPageTitle);
	}

	// ------------------------------------------------------------------------------------------------------------------

	private void _parseWikipediaPageSections(ParsedPage parsedPage, WikipediaPage wikipediaPage) {
		for (Section section : parsedPage.getSections()) {

			_parseWikipediaPageSection(wikipediaPage, section);

		}

		_ensureFirstSectionExistence(wikipediaPage);
	}

	// ------------------------------------------------------------------------------------------------------------------

	private void _ensureFirstSectionExistence(WikipediaPage wikipediaPage) {
		if (!wikipediaPage.getSections().contains("first")) {
			wikipediaPage.getSections().add("first");
			wikipediaPage.getSectionsContent().put("first", "");
		}
	}

	// ------------------------------------------------------------------------------------------------------------------

	private void _parseWikipediaPageSection(WikipediaPage wikipediaPage, Section section) {
		String sectionName = (section.getTitle() == null) ? "first" : section.getTitle();
		wikipediaPage.addSection(sectionName);

		String sectionContent = "";

		sectionContent = _parseWikipediaPageSectionContent(section, sectionName);
		wikipediaPage.addSectionContent(sectionName, sectionContent);
	}

	// ------------------------------------------------------------------------------------------------------------------

	private String _parseWikipediaPageSectionContent(Section section, String sectionName) {
		String sectionContent = "";
		String parsedText;
		String lineWithoutTemplates;
		for (Content content : section.getContentList()) {

			parsedText = content.getText();
			lineWithoutTemplates = _cleanTemplates(parsedText);

			if (!lineWithoutTemplates.equals(sectionName)) {

				lineWithoutTemplates = lineWithoutTemplates.replaceAll("\\s+", " ");
				sectionContent = sectionContent + lineWithoutTemplates;
			}
		}
		return sectionContent;
	}

	// -------------------------------------------------------------------------------------------------------------------

	private String _cleanTemplates(String parsedText) {
		return StringUtils.outerMatching(parsedText, templateRegExp, '[', ']');

	}

}
