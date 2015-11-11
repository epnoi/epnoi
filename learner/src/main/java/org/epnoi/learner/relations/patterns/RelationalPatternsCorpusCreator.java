package org.epnoi.learner.relations.patterns;

import org.epnoi.model.RelationalSentence;
import org.epnoi.model.RelationalSentencesCorpus;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;

import java.util.List;
import java.util.logging.Logger;

public class RelationalPatternsCorpusCreator {
	private static final Logger logger = Logger
			.getLogger(RelationalPatternsCorpusCreator.class.getName());
	RelationalPatternGenerator patternsGenerator;
	Core core;

	// ----------------------------------------------------------------------------------

	/**
	 * Initialization method for the RelationalPatternsCorpusCreator. Must be
	 * called before
	 * 
	 * @param core
	 *            The UIA core.
	 * @param relationalPatternGenerator
	 *            The object responsible of the generation of patterns. The
	 *            nature of the patterns of the corpus depends on the kind of
	 *            RelationalPatterns that this object generates.
	 * @throws EpnoiInitializationException
	 */
	public void init(Core core,
			RelationalPatternGenerator relationalPatternGenerator)
			throws EpnoiInitializationException {
		logger.info("Initalizing the RelationalPatternsCorpusCreator");
		this.core = core;
		this.patternsGenerator = relationalPatternGenerator;

	}

	// ----------------------------------------------------------------------------------

	/**
	 * Creates a corpus of relational patterns from a corpus of definitional
	 * sentences.
	 * 
	 * @param relationalSentencesCorpus
	 * @return
	 */
	public RelationalPatternsCorpus buildCorpus(
			RelationalSentencesCorpus relationalSentencesCorpus) {
		logger.info("Building a RelationalPatternsCorpus "+relationalSentencesCorpus);
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
		logger.info("A RelationalPatternsCorpus was built with "
				+ numberOfPatterns + " patters, with an average length "
				+ average / numberOfPatterns);
		return patternsCorpus;
	}
}
