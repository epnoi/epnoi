package org.epnoi.uia.learner.relations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.epnoi.uia.learner.nlp.wordnet.WordNetHandler;

public class CuratedRelationsTable {
	private Map<String, Set<String>> hypernyms;
	WordNetHandler wordNetHandler;

	// -----------------------------------------------------------------------------------------------

	public CuratedRelationsTable(WordNetHandler wordNetHandler) {
		this.hypernyms = new HashMap<String, Set<String>>();
		this.wordNetHandler = wordNetHandler;
	}

	// -----------------------------------------------------------------------------------------------

	public void addHypernym(String word, Set<String> wordHypernyms) {

		if (wordHypernyms.size() > 0) {

			this.hypernyms.put(word, wordHypernyms);

		}
	}

	// -----------------------------------------------------------------------------------------------

	public boolean areRelated(String source, String target) {
		String stemmedSource = this.wordNetHandler.stemNoun(source);
		String stemmedTarget = this.wordNetHandler.stemNoun(target);
		Set<String> sourceHypernyms = this.hypernyms.get(stemmedSource);
		return (sourceHypernyms != null && sourceHypernyms
				.contains(stemmedTarget));

	}

	// -----------------------------------------------------------------------------------------------

	public Set<String> getHypernyms(String source) {
		Set<String> sourceHypernyms = this.hypernyms.get(source);
		return ((sourceHypernyms == null) ? new HashSet<String>()
				: (HashSet<String>) ((HashSet<String>) sourceHypernyms).clone());
	}
	
	// -----------------------------------------------------------------------------------------------

	public String stemTerm(String term) {
		System.out.println("TERM TO STEMM> "+term);
		return (this.wordNetHandler.stemNoun(term));
	}

	// -----------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "CuratedRelationsTable [hypernyms=" + hypernyms + "]";
	}

	// -----------------------------------------------------------------------------------------------

}
