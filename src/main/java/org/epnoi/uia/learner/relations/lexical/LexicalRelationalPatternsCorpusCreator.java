package org.epnoi.uia.learner.relations.lexical;

import java.util.List;

import org.epnoi.uia.learner.relations.RelationalPatternsCorpus;
import org.epnoi.uia.learner.relations.RelationalSentence;
import org.epnoi.uia.learner.relations.RelationalSentencesCorpus;

public class LexicalRelationalPatternsCorpusCreator {
	LexicalRelationalPatternGenerator patternsGenerator = new LexicalRelationalPatternGenerator();

	public RelationalPatternsCorpus buildCorpus(
			RelationalSentencesCorpus relationalSentencesCorpus) {
		RelationalPatternsCorpus patternsCorpus = new RelationalPatternsCorpus();
		for (RelationalSentence relationalSentence : relationalSentencesCorpus
				.getSentences()) {
			List<LexicalRelationalPattern> patterns = this.patternsGenerator
					.generate(relationalSentence);
			for (LexicalRelationalPattern pattern : patterns) {
				patternsCorpus.getPatterns().add(pattern);
			}
		}
		return patternsCorpus;
	}
}
