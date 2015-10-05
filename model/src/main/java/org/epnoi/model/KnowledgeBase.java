package org.epnoi.model;

import java.util.Set;

import org.epnoi.model.modules.KnowledgeBaseParameters;

public interface KnowledgeBase {

	void init(KnowledgeBaseParameters parameters);

	boolean areRelated(String source, String target, String type);

	boolean areRelatedInWordNet(String source, String target);

	boolean areRelatedInWikidata(String source, String target);

	// -----------------------------------------------------------------------------------------------
	/**
	 * Method that returns the hypermyms of a given term
	 * 
	 * @param source
	 *            We assume that the source has been stemmed using the stemmer
	 *            associated with the handler
	 * @return
	 */
	Set<String> getHypernyms(String source);

	Set<String> stem(String term);
/*
	WordNetHandler getWordNetHandler();

	void setWordNetHandler(WordNetHandler wordNetHandler);

	WikidataHandler getWikidataHandler();

	void setWikidataHandler(WikidataHandler wikidataHandler);
*/
}