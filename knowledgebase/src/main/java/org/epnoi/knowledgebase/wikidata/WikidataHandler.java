package org.epnoi.knowledgebase.wikidata;

import java.util.Set;

public interface WikidataHandler {
	public Set<String> getRelated(String source, String type);
	public String stem(String word);

}
