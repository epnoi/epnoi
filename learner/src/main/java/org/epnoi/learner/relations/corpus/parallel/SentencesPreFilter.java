package org.epnoi.learner.relations.corpus.parallel;

import org.apache.spark.api.java.function.Function;

public class SentencesPreFilter implements Function<Sentence, Boolean> {
	private static final int MAX_SENTENCE_LENGTH = 80;

	@Override
	public Boolean call(Sentence currentSentece) throws Exception {

		boolean isValid = (currentSentece.getAnnotation().getFeatures() != null
				&& currentSentece.getAnnotation().getFeatures().get("string") != null && currentSentece.getAnnotation()
						.getFeatures().get("string").toString().length() < MAX_SENTENCE_LENGTH);

		return isValid;
	}

}
