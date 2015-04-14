package org.epnoi.uia.learner.relations.curated;

import org.epnoi.uia.exceptions.EpnoiInitializationException;
import org.epnoi.uia.learner.nlp.wordnet.WordNetHandler;
import org.epnoi.uia.learner.nlp.wordnet.WordNetParameters;

public class CuratedRelationsTableCreator {
	private WordNetHandler wordnetHandler;

	// ------------------------------------------------------------------------------------

	public void init(WordNetParameters parameters)
			throws EpnoiInitializationException {
		this.wordnetHandler = new WordNetHandler();
		this.wordnetHandler.init(parameters);
	}

	// ------------------------------------------------------------------------------------

	public CuratedRelationsTable build() {
		CuratedRelationsTable curatedRealtionsTable = new CuratedRelationsTable(
				this.wordnetHandler);
		for (String noun : wordnetHandler.getNouns()) {
			/*
			 * Set<String> nounHypernyms = wordnetHandler
			 * .getNounFirstMeaningHypernyms(noun);
			 */
			curatedRealtionsTable.addHypernym(noun,
					wordnetHandler.getNounFirstMeaningHypernyms(noun));

		}
		return curatedRealtionsTable;
	}

	// ------------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out.println("Starting the CuratedRelationsTableCreator test!!");

		String filepath = "/epnoi/epnoideployment/wordnet/dictWN3.1/";
		WordNetParameters parameters = new WordNetParameters();
		parameters
				.setParameter(WordNetParameters.DICTIONARY_LOCATION, filepath);

		CuratedRelationsTableCreator curatedRelationsTableCreator = new CuratedRelationsTableCreator();
		try {
			curatedRelationsTableCreator.init(parameters);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CuratedRelationsTable curatedRelationsTable = curatedRelationsTableCreator
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
