package org.epnoi.uia.learner.relations.knowledgebase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.epnoi.uia.learner.relations.knowledgebase.wikidata.WikidataHandler;
import org.epnoi.uia.learner.relations.knowledgebase.wordnet.WordNetHandler;

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
		String stemmedSource = this.wordNetHandler.stemNoun(source);
		String stemmedTarget = this.wordNetHandler.stemNoun(target);
		Set<String> sourceHypernyms = this.wordNetHandler.getNounFirstMeaningHypernyms(stemmedSource);
		return (sourceHypernyms != null && sourceHypernyms
				.contains(stemmedTarget));

	}

	// -----------------------------------------------------------------------------------------------

	public Set<String> getHypernyms(String source) {
		Set<String> sourceHypernyms = this.wordNetHandler.getNounFirstMeaningHypernyms(source);
		return ((sourceHypernyms == null) ? new HashSet<String>()
				: (HashSet<String>) ((HashSet<String>) sourceHypernyms).clone());
	}

	// -----------------------------------------------------------------------------------------------

	public String stemTerm(String term) {
		// System.out.println("TERM TO STEMM> "+term);
		return (this.wordNetHandler.stemNoun(term));
	}


	// -----------------------------------------------------------------------------------------------

}
