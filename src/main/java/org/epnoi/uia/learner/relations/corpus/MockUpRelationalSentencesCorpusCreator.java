package org.epnoi.uia.learner.relations.corpus;

import gate.Document;

import java.util.logging.Logger;

import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.learner.nlp.TermCandidatesFinder;
import org.epnoi.uia.learner.relations.RelationalSentence;

public class MockUpRelationalSentencesCorpusCreator {
	private static final Logger logger = Logger
			.getLogger(MockUpRelationalSentencesCorpusCreator.class.getName());

	private Core core;
	private TermCandidatesFinder termCandidatesFinder;
	private RelationalSentencesCorpus corpus;

	// ----------------------------------------------------------------------------------------------------------------------

	public void init(Core core) throws EpnoiInitializationException {
		this.core = core;
		this.corpus = new RelationalSentencesCorpus();
		this.termCandidatesFinder = new TermCandidatesFinder();
		this.termCandidatesFinder.init(core);

	}

	// ----------------------------------------------------------------------------------------------------------------------

	public RelationalSentencesCorpus createTestCorpus() {
		String relationalSentenceURI = "http://tinytestcorpus/drinventor";
		this.corpus
				.setDescription("The tiny test corpus created by the mockup");
		this.corpus.setURI(relationalSentenceURI);
		// relationalSentencesCorpus.setType(RelationHelper.HYPERNYM);

		Document annotatedContentA = termCandidatesFinder
				.findTermCandidates("A dog is a canine");
		RelationalSentence relationalSentenceA = new RelationalSentence(
				new OffsetRangeSelector(2L, 5L), new OffsetRangeSelector(11L,
						17L), "A dog is a canine", annotatedContentA.toXml());

		Document annotatedContentB = termCandidatesFinder
				.findTermCandidates("A dog, is a canine (and other things!)");

		RelationalSentence relationalSentenceB = new RelationalSentence(
				new OffsetRangeSelector(2L, 5L), new OffsetRangeSelector(12L,
						18L), "A dog, is a canine (and other things!)",
				annotatedContentB.toXml());

		this.corpus.getSentences().add(relationalSentenceA);

		this.corpus.getSentences().add(relationalSentenceB);
		return this.corpus;
	}

}
