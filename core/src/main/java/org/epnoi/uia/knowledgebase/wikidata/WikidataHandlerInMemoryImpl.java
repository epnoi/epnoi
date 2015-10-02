package org.epnoi.uia.knowledgebase.wikidata;

import java.util.Set;

import org.epnoi.model.WikidataView;

public class WikidataHandlerInMemoryImpl implements WikidataHandler {
	private WikidataStemmer stemmer = new WikidataStemmer();
	private WikidataView wikidataView;

	// --------------------------------------------------------------------------------------------------

	WikidataHandlerInMemoryImpl(WikidataView wikidataView) {
		this.wikidataView = wikidataView;
	}

	public WikidataView getWikidataView() {
		return this.wikidataView;
	}

	// --------------------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "WikidataHandlerInMemoryImpl [wikidataView=" + wikidataView
				+ "]";
	}
	// --------------------------------------------------------------------------------------------------
	@Override
	public String stem(String term) {
		return this.stemmer.stem(term);
	}

	// --------------------------------------------------------------------------------------------------

	@Override
	public Set<String> getRelated(String source, String type) {
		return this.wikidataView.getRelated(source, type);
	}

	// --------------------------------------------------------------------------------------------------

}
