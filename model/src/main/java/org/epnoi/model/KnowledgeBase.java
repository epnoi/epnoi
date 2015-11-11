package org.epnoi.model;

import org.epnoi.model.modules.KnowledgeBaseParameters;

import java.util.Set;

public interface KnowledgeBase {

	void init(KnowledgeBaseParameters parameters);


	/**
	 *
	 * @param source Surface form of the source
	 * @param target Surface form of the target
	 * @param type The type of the relationships
	 * @return Whether this relationship holds between the source and the target
	 */
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