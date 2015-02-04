package org.epnoi.uia.learner.relations.lexical;

import java.util.List;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.exceptions.EpnoiInitializationException;
import org.epnoi.uia.learner.relations.RelationalPatternsCorpus;
import org.epnoi.uia.learner.relations.RelationalSentence;
import org.epnoi.uia.learner.relations.RelationalSentencesCorpus;

public class LexicalRelationalPatternsCorpusCreator {
	LexicalRelationalPatternGenerator patternsGenerator;
	Core core;

	// ----------------------------------------------------------------------------------

	public void init(Core core) throws EpnoiInitializationException {
		this.core = core;
		this.patternsGenerator = new LexicalRelationalPatternGenerator();
		this.patternsGenerator.init(core);

	}

	// ----------------------------------------------------------------------------------

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
