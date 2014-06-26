package org.epnoi.uia.learner.terms;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
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

public class TermsExtractor {

	TermsDetector termDetector;

	public TermsExtractor() {
		this.termDetector = new TermsDetector();
	}

	// -------------------------------------------------------------------------------------------------------

	private List<List<AnnotatedWord<String>>> _generateAnnotatedSentences(
			String content) {
		TermsDetector termDetector = new TermsDetector();
		List<List<AnnotatedWord<String>>> annotatedSentences = new ArrayList<List<AnnotatedWord<String>>>();
		InputStream modelIn = null;
		InputStream POSmodelIn = null;
		try {
			modelIn = new FileInputStream(TermsExtractor.class.getResource(
					"en-sent.bin").getPath());
			POSmodelIn = new FileInputStream(TermsExtractor.class.getResource(
					"en-pos-maxent.bin").getPath());
			POSModel posModel = new POSModel(POSmodelIn);
			POSTaggerME tagger = new POSTaggerME(posModel);

			SentenceModel model = new SentenceModel(modelIn);

			SentenceDetector detector = new SentenceDetectorME(model);

			String[] result = detector.sentDetect(content);
			for (int i = 0; i < result.length; i++) {
				List<AnnotatedWord<String>> annotatedSentence = this
						._generateAnnotatedSentence(result[i], tagger);

				annotatedSentences.add(annotatedSentence);
			}
/*
			for (List<AnnotatedWord<String>> s : annotatedSentences) {
				System.out.println("ASentence-> " + s);

			}
			*/
			/*
			System.out.println("==term detection result ----> "
					+ termDetector.detect(annotatedSentences));
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

	// -------------------------------------------------------------------------------------------------------

	List<AnnotatedWord<String>> _generateAnnotatedSentence(String sentence,
			POSTaggerME tagger) {
		// System.out.println("Sentence: " + _cleanSentence(result[i]));

		// System.out.println("Tagging:> ");
		String[] words = SimpleTokenizer.INSTANCE
				.tokenize(_cleanSentence(sentence));
		// System.out.println("words length> " + words.length);
		String[] tags = tagger.tag(words);

		List<AnnotatedWord<String>> annotatedWords = new ArrayList<>();
		// System.out.println("tags length> " + tags.length);
		for (int j = 0; j < tags.length; j++) {
			// System.out.println(tags[j] + " ,");
			AnnotatedWord<String> annotatedWord = new AnnotatedWord<String>();
			annotatedWord.setWord(words[j]);
			annotatedWord.setAnnotation(tags[j]);
			annotatedWords.add(annotatedWord);

		}
		return annotatedWords;
	}

	// -------------------------------------------------------------------------------------------------------

	public Map<String, Long> countTermsCandidates(String content) {
		Map<String, Long> candidatesCount = new HashMap<String, Long>();

		List<List<AnnotatedWord<String>>> annotatedSentences = _generateAnnotatedSentences(content);
		List<AnnotatedWord<Long>> termCandidates = _generateTermCandidates(annotatedSentences);
		System.out.println("TERM CANDIDATES " + termCandidates);

		return candidatesCount;
	}

	// -------------------------------------------------------------------------------------------------------

	public List<AnnotatedWord<TermMetadata>> extractTerms(String content) {

		List<List<AnnotatedWord<String>>> annotatedSentences = _generateAnnotatedSentences(content);
		List<AnnotatedWord<TermMetadata>> detectedTerms = this.termDetector
				.detect(annotatedSentences);
		System.out.println("detected Terms >>>>> " + detectedTerms);

		Collections.sort(detectedTerms);
		return detectedTerms;
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

		for (Entry<String, Long> entry : termDictionary.entrySet()) {
			AnnotatedWord<Long> annotatedWord = new AnnotatedWord<Long>();
			annotatedWord.setWord(entry.getKey());
			annotatedWord.setAnnotation(entry.getValue());
			termCandidates.add(annotatedWord);
		}

		Collections.sort(termCandidates, Collections.reverseOrder());
		return termCandidates;
	}

	// -------------------------------------------------------------------------------------------------------

	private String _cleanSentence(String sentence) {

		String cleanSentence = sentence.replaceAll("\n", " ").replaceAll(
				"\\s+", " ");
		return cleanSentence;
	}

	// -------------------------------------------------------------------------------------------------------

	public interface Comparable<T> {
		int compareTo(T o);
	}

	// -------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out
				.println("Testing the TermCandidatesCounter...........................................");

		TermsExtractor termExtractor = new TermsExtractor();
		/*
		 * Core core = CoreUtility.getUIACore(); Item item = (Item) core
		 * .getInformationAccess() .get(
		 * "http://rss.slashdot.org/~r/Slashdot/slashdot/~3/-FraYC4r__w/story01.htm"
		 * , FeedRDFHelper.ITEM_CLASS); List<AnnotatedWord<TermMetadata>>
		 * extractedTerms = termExtractor .extractTerms(item.getContent());
		 */
		List<AnnotatedWord<TermMetadata>> extractedTerms = termExtractor
				.extractTerms("My father mother is rich. And my great father mother is in the big kitchen. I love normal distribution. Normal Fourier Distribution is great!.");
		System.out.println("........> " + extractedTerms);
	}

}
