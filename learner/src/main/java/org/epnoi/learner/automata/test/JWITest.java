package org.epnoi.learner.automata.test;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import edu.mit.jwi.item.*;
import edu.mit.jwi.morph.WordnetStemmer;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class JWITest {

	// -------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) throws Exception {
		System.out.println("Starting JWI test!");

		String filepath = "/epnoi/epnoideployment/wordnet/dictWN40K/dict";
		// String filepath = "/epnoi/epnoideployment/wordnet/dictWN3.1";
		JWITest test = new JWITest();

		File folder = new File(filepath);
		test.testRAMDictionary(folder);

		System.out.println("Ending JWI test!");
	}

	// -------------------------------------------------------------------------------------------------------------------

	public void testRAMDictionary(File wnDir) throws Exception {

		// construct the dictionary object and open it
		IRAMDictionary dict = new RAMDictionary(wnDir, ILoadPolicy.NO_LOAD);
		dict.open();

		// do something
		trek(dict);

		// now load into memory
		System.out.print("Loading Wordnet into memory ... ");

		System.out.print("Testing the stemmer ... ");

		edu.mit.jwi.morph.WordnetStemmer wordnetStemmer = new WordnetStemmer(
				dict);

		List<String> wordsToStem = Arrays.asList("lions", "cow", "wrer", "Domestic Animal");
		for (String word : wordsToStem) {
			System.out.println(word +"> "+wordnetStemmer.findStems(word, POS.NOUN));
		}

		long t = System.currentTimeMillis();
		dict.load(true);
		trek(dict);

		// this.getSynonyms(dict);
		long newt = System.currentTimeMillis();
		System.out.printf("done in " + String.valueOf(newt - t));

	}

	// -------------------------------------------------------------------------------------------------------------------

	public void trek(IDictionary dict) {
		int tickNext = 0;
		int tickSize = 20000;
		int seen = 0;
		System.out.print(" Treking across Wordnet ");
		long t = System.currentTimeMillis();
		for (POS pos : POS.values()) {
			for (Iterator<IIndexWord> i = dict.getIndexWordIterator(pos); i
					.hasNext();)
				for (IWordID wid : i.next().getWordIDs()) {
					seen += dict.getWord(wid).getSynset().getWords().size();
					if (seen > tickNext) {
						System.out.print('.');
						tickNext = seen + tickSize;
					}
				}
		}
		System.out.println("In my trek I saw " + seen + " words ");

		tickSize = 20000;
		seen = 0;

		for (POS pos : POS.values()) {
			for (Iterator<ISynset> i = dict.getSynsetIterator(pos); i.hasNext();) {
				seen++;
				i.next();
			}
		}
		System.out.println("In my trek I saw " + seen + " hypernyms ");

		long time = System.currentTimeMillis() - t;
		System.out.printf(" done in " + String.valueOf(time));

	}

	// -------------------------------------------------------------------------------------------------------------------

	public void getSynonyms(IDictionary dict) {

		for (POS pos : POS.values()) {
			for (Iterator<IIndexWord> i = dict.getIndexWordIterator(pos); i
					.hasNext();)
				for (IWordID wid : i.next().getWordIDs()) {
					IWord word = dict.getWord(wid);
					for (ISynsetID synetID : word.getSynset()
							.getRelatedSynsets(Pointer.HYPERNYM)) {
						System.out.println("H-->" + synetID);
						for (IWord synetWord : dict.getSynset(synetID)
								.getWords()) {
							System.out.println("   -->" + synetWord);
						}
					}
				}
		}

		// look up first sense of the word "dog "
		IIndexWord idxWord = dict.getIndexWord("dog", POS.NOUN);
		IWordID wordID = idxWord.getWordIDs().get(0); // 1st meaning
		IWord word = dict.getWord(wordID);
		ISynset synset = word.getSynset();

		// iterate over words associated with the synset
		for (IWord w : synset.getWords()) {
			System.out.println(w.getLemma());
		}
	}
}
