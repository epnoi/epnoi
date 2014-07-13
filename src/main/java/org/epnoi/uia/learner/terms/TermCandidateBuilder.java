package org.epnoi.uia.learner.terms;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class TermCandidateBuilder {

	private Document document;

	static final Comparator<Annotation> ANNOTATION_ORDER = new Comparator<Annotation>() {
		public int compare(Annotation annotation1, Annotation annotation2) {
			return annotation1.getStartNode().getOffset()
					.compareTo(annotation2.getStartNode().getOffset());
		}
	};

	// ------------------------------------------------------------------------------------------------------------

	public TermCandidateBuilder(Document document) {
		this.document = document;
	}

	// ------------------------------------------------------------------------------------------------------------

	public AnnotatedWord<TermCandidateMetadata> buildTermCandidate(
			Annotation annotation) {
		AnnotatedWord<TermCandidateMetadata> termCandidate = new AnnotatedWord<TermCandidateMetadata>(
				new TermCandidateMetadata());

		Long startOffset = annotation.getStartNode().getOffset();
		Long endOffset = annotation.getEndNode().getOffset();

		AnnotationSet annotations = this.document.getAnnotations();

		/*
		 * try {
		 * 
		 * System.out.println(this.document.getContent().getContent(
		 * annotation.getStartNode().getOffset(),
		 * annotation.getEndNode().getOffset()));
		 * 
		 * } catch (InvalidOffsetException e) {
		 * 
		 * e.printStackTrace(); }
		 */
		// length calculation
		// int length = annotations.get("Token", startOffset, endOffset).size();
		// System.out.println("La longitud seria > " + length);
		// termCandidate.getAnnotation().setLength(length);
		// words

		ArrayList<String> words = new ArrayList<String>();

		List<Annotation> tokeAnnotations = new ArrayList<Annotation>();
		for (Annotation tokenAnnotation : annotations.get("Token", startOffset,
				endOffset)) {
			tokeAnnotations.add(tokenAnnotation);
		}

		Collections.sort(tokeAnnotations, ANNOTATION_ORDER);

		for (Annotation tokenAnnotation : tokeAnnotations) {

			words.add(((String) tokenAnnotation.getFeatures().get("string"))
					.toLowerCase());
		}
		termCandidate.getAnnotation().setWords(
				Arrays.copyOf(words.toArray(), words.size(), String[].class));
		termCandidate.getAnnotation().setLength(words.size());

		String word = this._generateWord(words);

		termCandidate.setWord(word);

		/*
		 * for (Annotation
		 * termCandidateAnnotation:annotations.get("Token",startOffset,
		 * endOffset)){ System.out.println("---> "+ termCandidateAnnotation); }
		 */

		return termCandidate;
	}

	// ------------------------------------------------------------------------------------------------------------

	private String _generateWord(List<String> words) {
		String word = "";
		Iterator<String> wordsIt = words.iterator();
		while (wordsIt.hasNext()) {
			word += wordsIt.next();
			if (wordsIt.hasNext()) {
				word += " ";
			}
		}
		return word;
	}

	// ------------------------------------------------------------------------------------------------------------

	public AnnotatedWord<TermCandidateMetadata> generateSubTermCandidate(
			String[] words) {
		AnnotatedWord<TermCandidateMetadata> termCandidate = new AnnotatedWord<TermCandidateMetadata>(
				new TermCandidateMetadata());
		termCandidate.getAnnotation().setLength(words.length);
		termCandidate.getAnnotation().setWords(words);
		termCandidate.setWord(this._generateWord(Arrays.asList(words)));

		return termCandidate;
	}

	// ------------------------------------------------------------------------------------------------------------

	public List<AnnotatedWord<TermCandidateMetadata>> splitTermCandidate(
			AnnotatedWord<TermCandidateMetadata> termCandidate) {
		List<AnnotatedWord<TermCandidateMetadata>> termCandidates = new ArrayList<AnnotatedWord<TermCandidateMetadata>>();

		String[] words = termCandidate.getAnnotation().getWords();
		List<String[]> listSubtermsWords = _generateSubtermsWords(words);
		for (String[] subtermWords : listSubtermsWords) {
			termCandidates.add(generateSubTermCandidate(subtermWords));
		}

		return termCandidates;
	}

	// ------------------------------------------------------------------------------------------------------------

	private List<String[]> _generateSubtermsWords(String[] words) {
		ArrayList<String[]> subtermWords = new ArrayList<String[]>();
		int lenght = words.length;

		for (int i = 0; i < lenght; i++) {
			for (int j = i; j < lenght; j++) {

				String[] aux = Arrays.copyOfRange(words, i, j + 1);
				if (aux.length < lenght) {
					subtermWords.add(aux);
				}
			}

		}

		return subtermWords;
	}

	// ------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		TermCandidateBuilder termBuilder = new TermCandidateBuilder(null);
		AnnotatedWord<TermCandidateMetadata> termCandidate = new AnnotatedWord<TermCandidateMetadata>(
				new TermCandidateMetadata());
		termCandidate.getAnnotation().setWords(
				new String[] { "a", "b", "c", "d" });
		System.out.println(termBuilder.splitTermCandidate(termCandidate));
		
		for(int i =0; i<100; i++){
		System.out.println(i+"->"+(Math.log(i) / Math
				.log(2)) );
		}
	}
}
