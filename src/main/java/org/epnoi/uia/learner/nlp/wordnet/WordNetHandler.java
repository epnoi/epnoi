package org.epnoi.uia.learner.nlp.wordnet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.epnoi.uia.exceptions.EpnoiInitializationException;

import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

public class WordNetHandler {
	WordNetParameters parameters;
	IRAMDictionary wordNetDictionary;

	// ---------------------------------------------------------------------------------------------------------------

	public void init(WordNetParameters parameters)
			throws EpnoiInitializationException {
		this.parameters = parameters;
		String filePath = (String) parameters
				.getParameterValue(WordNetParameters.DICTIONARY_LOCATION);
		try {
			File folder = new File(
					(String) parameters
							.getParameterValue(WordNetParameters.DICTIONARY_LOCATION));

			this.wordNetDictionary = new RAMDictionary(folder,
					ILoadPolicy.IMMEDIATE_LOAD);

			this.wordNetDictionary.open();
		} catch (IOException e) {
			//e.printStackTrace();
			throw new EpnoiInitializationException(
					"The WordNetHandler was not able to open the WordNet dictionary at "
							+ filePath);
		}

	}

	// ---------------------------------------------------------------------------------------------------------------

	public List<String> getNounFirstMeaning(String noun) {
		List<String> meanings = new ArrayList<String>();

		IIndexWord idxWord = this.wordNetDictionary
				.getIndexWord(noun, POS.NOUN);
		IWordID wordID = idxWord.getWordIDs().get(0); // 1st meaning
		IWord word = this.wordNetDictionary.getWord(wordID);
		ISynset synset = word.getSynset();
		List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);

		// print out each h y p e r n y m s id and synonyms
		List<IWord> words;
		for (ISynsetID sid : hypernyms) {
			words = this.wordNetDictionary.getSynset(sid).getWords();
			System.out.print(sid + " {");
			for (Iterator<IWord> i = words.iterator(); i.hasNext();) {
				System.out.print(i.next().getLemma());
				if (i.hasNext())
					System.out.print(", ");
			}
			System.out.println("}");
		}

		return meanings;

	}

	public static void main(String[] args) {
		System.out.println("Starting WordNetHandlerTest");
		String filepath = "/epnoi/epnoideployment/wordnet/dictWN3.1/dict";
		WordNetParameters parameters = new WordNetParameters();
		parameters
				.setParameter(WordNetParameters.DICTIONARY_LOCATION, filepath);
		WordNetHandler handler = new WordNetHandler();

		try {
			handler.init(parameters);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		handler.getNounFirstMeaning("dog");
		handler.getNounFirstMeaning("lion");
		System.out.println("Ending WordNetHandlerTest");

	}
}
