package org.epnoi.uia.learner.relations;

import java.util.HashMap;
import java.util.Map;

public class RelationalSentencesCorpus {
	
	private Map<String, RelationalSentence> sentences = new HashMap<String, RelationalSentence>();

	// --------------------------------------------------------------------------------------------

	public Map<String, RelationalSentence> getSentences() {
		return sentences;
	}

	// --------------------------------------------------------------------------------------------

	public void setSentences(Map<String, RelationalSentence> sentences) {
		this.sentences = sentences;
	}

	// --------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "RelationalSentencesCorpus [sentences=" + sentences + "]";
	}

	// --------------------------------------------------------------------------------------------

}
