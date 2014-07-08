package org.epnoi.uia.learner.terms;

import java.util.Map;
import java.util.HashMap;

public class TermsIndex {
	private Map<String, AnnotatedWord<TermMetadata>> terms;

	// -------------------------------------------------------------------------------------------------------

	public TermsIndex() {
		this.terms = new HashMap<String, AnnotatedWord<TermMetadata>>();
	}

	// -------------------------------------------------------------------------------------------------------

	public AnnotatedWord<TermMetadata> lookUp(String word) {
		return terms.get(word);
	}

	// -------------------------------------------------------------------------------------------------------

	public void update(AnnotatedWord<TermMetadata> term) {
		this.terms.put(term.getWord(), term);
	}

}
