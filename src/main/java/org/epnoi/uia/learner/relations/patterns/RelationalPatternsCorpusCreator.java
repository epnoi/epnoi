package org.epnoi.uia.learner.relations.patterns;

import java.util.List;
import java.util.logging.Logger;

import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.learner.relations.RelationalSentence;
import org.epnoi.uia.learner.relations.corpus.RelationalSentencesCorpus;
import org.epnoi.uia.learner.relations.patterns.lexical.LexicalRelationalPattern;
import org.epnoi.uia.learner.relations.patterns.lexical.LexicalRelationalPatternGenerator;

public class RelationalPatternsCorpusCreator {
	private static final Logger logger = Logger
			.getLogger(RelationalPatternsCorpusCreator.class.getName());
	RelationalPatternGenerator patternsGenerator;
	Core core;

	// ----------------------------------------------------------------------------------

	public void init(Core core, RelationalPatternGenerator relationalPatternGenerator) throws EpnoiInitializationException {
		logger.info("Initalizing the RelationalPatternsCorpusCreator");
		this.core = core;
		this.patternsGenerator = relationalPatternGenerator;
		

	}

	// ----------------------------------------------------------------------------------

	public RelationalPatternsCorpus buildCorpus(
			RelationalSentencesCorpus relationalSentencesCorpus) {
		logger.info("Building the LexicalRelationalPatternsCorpusCreator");
		RelationalPatternsCorpus patternsCorpus = new RelationalPatternsCorpus();
		double average = 0;
		long numberOfPatterns = 0;
		for (RelationalSentence relationalSentence : relationalSentencesCorpus
				.getSentences()) {
			List<RelationalPattern> patterns = this.patternsGenerator
					.generate(relationalSentence);
			for (RelationalPattern pattern : patterns) {
				patternsCorpus.getPatterns().add(pattern);

				average += pattern.getLength();
				numberOfPatterns++;
			}
		}
		logger.info("A LexicalRelationalPatternsCorpus was built with "
				+ numberOfPatterns + " patters, with an average length "
				+ average / numberOfPatterns);
		return patternsCorpus;
	}
}
