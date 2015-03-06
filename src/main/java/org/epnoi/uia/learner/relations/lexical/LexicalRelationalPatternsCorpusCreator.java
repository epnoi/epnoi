package org.epnoi.uia.learner.relations.lexical;

import java.util.List;
import java.util.logging.Logger;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.exceptions.EpnoiInitializationException;
import org.epnoi.uia.learner.relations.RelationalPatternsCorpus;
import org.epnoi.uia.learner.relations.RelationalSentence;
import org.epnoi.uia.learner.relations.RelationalSentencesCorpus;
import org.epnoi.uia.learner.relations.RelationalSentencesCorpusCreator;

public class LexicalRelationalPatternsCorpusCreator {
	private static final Logger logger = Logger
			.getLogger(LexicalRelationalPatternsCorpusCreator.class.getName());
	LexicalRelationalPatternGenerator patternsGenerator;
	Core core;

	// ----------------------------------------------------------------------------------

	public void init(Core core) throws EpnoiInitializationException {
		logger.info("Initalizing the LexicalRelationalPatternsCorpusCreator");
		this.core = core;
		this.patternsGenerator = new LexicalRelationalPatternGenerator();
		// this.patternsGenerator.init(core);

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
			List<LexicalRelationalPattern> patterns = this.patternsGenerator
					.generate(relationalSentence);
			for (LexicalRelationalPattern pattern : patterns) {
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
