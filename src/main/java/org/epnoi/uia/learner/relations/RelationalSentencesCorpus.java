package org.epnoi.uia.learner.relations;

import java.util.HashMap;
import java.util.Map;

public class RelationalSentencesCorpus {
	private Map<String, Object> sentences = new HashMap<String, Object>();

	// --------------------------------------------------------------------------------------------

	public Map<String, Object> getSentences() {
		return sentences;
	}

	// --------------------------------------------------------------------------------------------

	public void setSentences(Map<String, Object> sentences) {
		this.sentences = sentences;
	}
	
	// --------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "RelationalSentencesCorpus [sentences=" + sentences + "]";
	}

	// --------------------------------------------------------------------------------------------
	
	
	
}
