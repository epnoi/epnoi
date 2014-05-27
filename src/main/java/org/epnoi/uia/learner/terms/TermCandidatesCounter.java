package org.epnoi.uia.learner.terms;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.dao.rdf.FeedRDFHelper;

import epnoi.model.Item;

public class TermCandidatesCounter {

	private List<List<AnnotatedWord<String>>> _generateAnnotatedSentences(
			String content) {
		List<List<AnnotatedWord<String>>> annotatedSentences = new ArrayList<List<AnnotatedWord<String>>>();
		InputStream modelIn = null;
		InputStream POSmodelIn = null;
		try {
			modelIn = new FileInputStream(TermCandidatesCounter.class
					.getResource("en-sent.bin").getPath());
			POSmodelIn = new FileInputStream(TermCandidatesCounter.class
					.getResource("en-pos-maxent.bin").getPath());
			POSModel posModel = new POSModel(POSmodelIn);
			POSTaggerME tagger = new POSTaggerME(posModel);

			SentenceModel model = new SentenceModel(modelIn);

			SentenceDetector detector = new SentenceDetectorME(model);

			String[] result = detector.sentDetect(content);
			for (int i = 0; i < result.length; i++) {
				//System.out.println("Sentence: " + _cleanSentence(result[i]));

				//System.out.println("Tagging:> ");
				String[] words = SimpleTokenizer.INSTANCE
						.tokenize(_cleanSentence(result[i]));
				//System.out.println("words length> " + words.length);
				String[] tags = tagger.tag(words);

				List<AnnotatedWord<String>> annotatedWords = new ArrayList<>();
				//System.out.println("tags length> " + tags.length);
				for (int j = 0; j < tags.length; j++) {
					//System.out.println(tags[j] + " ,");
					AnnotatedWord<String> annotatedWord = new AnnotatedWord<String>();
					annotatedWord.setWord(words[j]);
					annotatedWord.setAnnotation(tags[j]);
					annotatedWords.add(annotatedWord);

				}
				annotatedSentences.add(annotatedWords);
			}
			/*
			for (List<AnnotatedWord<String>> s : annotatedSentences) {
				System.out.println("ASentence-> " + s);
			}
*/
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
				}
			}
		}
		return annotatedSentences;
	}

	public Map<String, Long> countTermsCandidates(String content) {
		Map<String, Long> candidatesCount = new HashMap<String, Long>();

		List<List<AnnotatedWord<String>>> annotatedSentences = _generateAnnotatedSentences(content);
		List<AnnotatedWord<Long>> termCandidates = _generateTermCandidates(annotatedSentences);
		System.out.println(termCandidates);

		return candidatesCount;
	}

	// ----------------------------------------------------------------------------------------------

	private List<AnnotatedWord<Long>> _generateTermCandidates(
			List<List<AnnotatedWord<String>>> annotatedSentences) {
		List<AnnotatedWord<Long>> termCandidates = new ArrayList<AnnotatedWord<Long>>();
		Map<String, Long> termDictionary = new HashMap<String, Long>();
		for (List<AnnotatedWord<String>> sentence : annotatedSentences) {
			
			for (AnnotatedWord<String> word : sentence) {
				Long termCount = termDictionary.get(word.getWord());
				if (termCount == null) {
					termCount = new Long(0);

				}
				termDictionary.put(word.getWord(), ++termCount);

			}
		}
		
		
		for(Entry<String, Long> entry:termDictionary.entrySet()){
			AnnotatedWord<Long> annotatedWord = new AnnotatedWord<Long>();
			annotatedWord.setWord(entry.getKey());
			annotatedWord.setAnnotation(entry.getValue());
			termCandidates.add(annotatedWord);
		}
		
		Collections.sort(termCandidates,Collections.reverseOrder());
		return termCandidates;
	}

	private String _cleanSentence(String sentence) {

		String cleanSentence = sentence.replaceAll("\n", " ").replaceAll(
				"\\s+", " ");
		return cleanSentence;
	}
	
	
	public interface Comparable<T> {
		  int compareTo(T o);
		}

	// ----------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out
				.println("Testing the TermCandidatesCounter...........................................");

		TermCandidatesCounter termCandidatesCounter = new TermCandidatesCounter();
		Core core = CoreUtility.getUIACore();
		Item item = (Item) core
				.getInformationAccess()
				.get("http://rss.slashdot.org/~r/Slashdot/slashdot/~3/-FraYC4r__w/story01.htm",
						FeedRDFHelper.ITEM_CLASS);
		termCandidatesCounter.countTermsCandidates(item.getContent());

		System.out
				.println("............................................................................");
	}

}
