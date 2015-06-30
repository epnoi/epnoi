package org.epnoi.uia.knowledgebase.wikidata;

import java.util.Set;

public interface WikidataHandler {
	public Set<String> getRelated(String source, String type);
	public String stem(String word);
	public WikidataView getView();
}
