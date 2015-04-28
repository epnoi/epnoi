package org.epnoi.uia.learner.relations.knowledgebase;

import org.epnoi.uia.exceptions.EpnoiInitializationException;
import org.epnoi.uia.learner.relations.knowledgebase.wordnet.WordNetHandler;
import org.epnoi.uia.learner.relations.knowledgebase.wordnet.WordNetParameters;

public class KnowledgeBaseCreator {
	private WordNetHandler wordnetHandler;

	// ------------------------------------------------------------------------------------

	public void init(WordNetParameters parameters)
			throws EpnoiInitializationException {
		this.wordnetHandler = new WordNetHandler();
		this.wordnetHandler.init(parameters);
	}

	// ------------------------------------------------------------------------------------

	public KnowledgeBase build() {
		KnowledgeBase knowledgeBase = new KnowledgeBase(
				this.wordnetHandler);
		for (String noun : wordnetHandler.getNouns()) {
			/*
			 * Set<String> nounHypernyms = wordnetHandler
			 * .getNounFirstMeaningHypernyms(noun);
			 */
			knowledgeBase.addHypernym(noun,
					wordnetHandler.getNounFirstMeaningHypernyms(noun));

		}
		return knowledgeBase;
	}

	// ------------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out.println("Starting the CuratedRelationsTableCreator test!!");

		String filepath = "/epnoi/epnoideployment/wordnet/dictWN3.1/";
		WordNetParameters parameters = new WordNetParameters();
		parameters
				.setParameter(WordNetParameters.DICTIONARY_LOCATION, filepath);

		KnowledgeBaseCreator curatedRelationsTableCreator = new KnowledgeBaseCreator();
		try {
			curatedRelationsTableCreator.init(parameters);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		KnowledgeBase curatedRelationsTable = curatedRelationsTableCreator
				.build();
		System.out
				.println("Testing for dog-canine-------------------------------------------------------");
		System.out.println(curatedRelationsTable.areRelated("dog", "canrine"));

		System.out
				.println("Testing for dogs-canine-------------------------------------------------------");
		System.out.println(curatedRelationsTable.areRelated("dogs", "canine"));

		System.out
				.println("Testing for dog-canines-------------------------------------------------------");
		System.out.println(curatedRelationsTable.areRelated("dog", "canines "));

		System.out.println("Starting the CuratedRelationsTableCreator test!!");
	}

}
