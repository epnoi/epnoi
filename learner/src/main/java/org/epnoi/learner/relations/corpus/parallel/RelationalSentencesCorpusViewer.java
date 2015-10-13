package org.epnoi.learner.relations.corpus.parallel;

import org.epnoi.model.RelationalSentence;
import org.epnoi.model.RelationalSentencesCorpus;

public class RelationalSentencesCorpusViewer {
	// ----------------------------------------------------------------------------------------------------------------------

		public static void showRelationalSentenceCorpusInfo(RelationalSentencesCorpus corpus) {
			System.out
					.println("------------------------------------------------------------------------------------------");
			System.out.println("Information about the corpus " + corpus.getUri());
			System.out.println("Relations type: " + corpus.getType());
			System.out.println("Corpus description: " + corpus.getDescription());
			System.out.println("It has " + corpus.getSentences().size() + " relational sentences");
			/*
			 * for (RelationalSentence relationalSencente :
			 * this.corpus.getSentences()) {
			 * _showRelationalSentenceInfo(relationalSencente); }
			 */
			double average = 0.;
			for (RelationalSentence relationalSencente : corpus.getSentences()) {
				average += relationalSencente.getSentence().length();
			}
			System.out.println("The average length is " + average / corpus.getSentences().size());
			System.out
					.println("------------------------------------------------------------------------------------------");

		}
}
