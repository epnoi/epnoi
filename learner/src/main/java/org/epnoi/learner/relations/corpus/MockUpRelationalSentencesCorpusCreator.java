package org.epnoi.learner.relations.corpus;

import gate.Document;
import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.model.RelationalSentence;
import org.epnoi.model.RelationalSentencesCorpus;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.model.modules.Core;

import java.util.logging.Logger;

public class MockUpRelationalSentencesCorpusCreator {
	private static final Logger logger = Logger
			.getLogger(MockUpRelationalSentencesCorpusCreator.class.getName());

	private Core core;
	private RelationalSentencesCorpus corpus;

	// ----------------------------------------------------------------------------------------------------------------------

	public void init(Core core) throws EpnoiInitializationException {
		this.core = core;
		this.corpus = new RelationalSentencesCorpus();
		
	}

	// ----------------------------------------------------------------------------------------------------------------------

	public RelationalSentencesCorpus createTestCorpus() {
		String relationalSentenceURI = "http://tinytestcorpus/drinventor";
		this.corpus
				.setDescription("The tiny test corpus created by the mockup");
		this.corpus.setUri(relationalSentenceURI);
		// relationalSentencesCorpus.setType(RelationHelper.HYPERNYMY);

		Document annotatedContentA=null;
		try {
			annotatedContentA = core.getNLPHandler()
					.process("A dog is a canine");
		} catch (EpnoiResourceAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RelationalSentence relationalSentenceA = new RelationalSentence(
				new OffsetRangeSelector(2L, 5L), new OffsetRangeSelector(11L,
						17L), "A dog is a canine", annotatedContentA.toXml());

		Document annotatedContentB=null;
		try {
			annotatedContentB = core.getNLPHandler()
					.process("A dog, is a canine (and other things!)");
		} catch (EpnoiResourceAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RelationalSentence relationalSentenceB = new RelationalSentence(
				new OffsetRangeSelector(2L, 5L), new OffsetRangeSelector(12L,
						18L), "A dog, is a canine (and other things!)",
				annotatedContentB.toXml());

		this.corpus.getSentences().add(relationalSentenceA);
		this.corpus.getSentences().add(relationalSentenceA);
		this.corpus.getSentences().add(relationalSentenceB);
		return this.corpus;
	}

}
