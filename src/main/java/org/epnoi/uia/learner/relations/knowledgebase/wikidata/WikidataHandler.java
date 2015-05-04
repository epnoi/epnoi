package org.epnoi.uia.learner.relations.knowledgebase.wikidata;

import java.util.Set;

public interface WikidataHandler {
	public Set<String> getRelated(String source, String type);
	public WikidataView getView();
}
