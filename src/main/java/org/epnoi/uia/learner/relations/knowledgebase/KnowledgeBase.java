package org.epnoi.uia.learner.relations.knowledgebase;

import java.util.HashSet;
import java.util.Set;

import org.epnoi.model.RelationHelper;
import org.epnoi.uia.learner.relations.knowledgebase.wikidata.WikidataHandler;
import org.epnoi.uia.learner.relations.knowledgebase.wordnet.WordNetHandler;

import com.google.common.collect.Sets;

public class KnowledgeBase {

	WordNetHandler wordNetHandler;
	WikidataHandler wikidataHandler;

	// -----------------------------------------------------------------------------------------------

	public KnowledgeBase(WordNetHandler wordNetHandler,
			WikidataHandler wikidataHandler) {

		this.wordNetHandler = wordNetHandler;
		this.wikidataHandler = wikidataHandler;
	}

	// -----------------------------------------------------------------------------------------------

	public boolean areRelated(String source, String target) {

		return (areRelatedInWordNet(source, target)
				|| areRelatedInWikidata(source, target));

	}

	// -----------------------------------------------------------------------------------------------

	public boolean areRelatedInWordNet(String source, String target) {
		String stemmedSource = this.wordNetHandler.stemNoun(source);
		String stemmedTarget = this.wordNetHandler.stemNoun(target);
		Set<String> sourceHypernyms = this.wordNetHandler
				.getNounFirstMeaningHypernyms(stemmedSource);
		return (sourceHypernyms != null && sourceHypernyms
				.contains(stemmedTarget));

	}

	// -----------------------------------------------------------------------------------------------

	public boolean areRelatedInWikidata(String source, String target) {
		String stemmedSource = this.wikidataHandler.stem(source);
		String stemmedTarget = this.wikidataHandler.stem(target);
		Set<String> sourceHypernyms = this.wikidataHandler.getRelated(
				stemmedSource, RelationHelper.HYPERNYM);
		return (sourceHypernyms != null && sourceHypernyms
				.contains(stemmedTarget));

	}

	// -----------------------------------------------------------------------------------------------
	/**
	 * Method that returns the hypermyms of a given term
	 * 
	 * @param source
	 *            We assume that the source has been stemmed using the stemmer
	 *            associated with the handler
	 * @return
	 */
	public Set<String> getHypernyms(String source) {
		Set<String> wordNetHypernyms = this.wordNetHandler
				.getNounFirstMeaningHypernyms(source);
		Set<String> wikidataHypernyms = this.wikidataHandler.getRelated(source,
				RelationHelper.HYPERNYM);
		return (Sets.union(wordNetHypernyms, wikidataHypernyms));
	}

	// -----------------------------------------------------------------------------------------------

	public Set<String> stem(String term) {

		Set<String> stemmedTerm = new HashSet<String>();
		String wordNetStemmedTerm = this.wordNetHandler.stemNoun(term);
		if (wordNetStemmedTerm != null) {
			stemmedTerm.add(wordNetStemmedTerm);
		}
		stemmedTerm.add(this.wikidataHandler.stem(term));

		return stemmedTerm;
	}

	// -----------------------------------------------------------------------------------------------

}
