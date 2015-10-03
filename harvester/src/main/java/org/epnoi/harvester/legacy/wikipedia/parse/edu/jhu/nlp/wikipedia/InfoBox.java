package org.epnoi.harvester.legacy.wikipedia.parse.edu.jhu.nlp.wikipedia;

/**
 * A class abstracting Wiki infobox
 * @author Delip Rao
 */
public class InfoBox {
  String infoBoxWikiText = null;
  InfoBox(String infoBoxWikiText) {
    this.infoBoxWikiText = infoBoxWikiText;
  }
  public String dumpRaw() {
    return infoBoxWikiText;
	}

	@Override
	public String toString() {
		return "InfoBox [infoBoxWikiText=" + infoBoxWikiText + "]";
	}
}
