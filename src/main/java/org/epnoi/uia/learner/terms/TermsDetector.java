package org.epnoi.uia.learner.terms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.epnoi.uia.learner.automata.Automaton;
import org.epnoi.uia.learner.automata.AutomatonImpl;
import org.epnoi.uia.learner.automata.Input;

public class TermsDetector {

	// ---------------------------------------------------------------------------------------

	public List<AnnotatedWord<TermCandidateMetadata>> detect(
			List<List<AnnotatedWord<String>>> sentences) {
		Map<String, AnnotatedWord<TermCandidateMetadata>> detectedTerms = new HashMap<>();

		for (List<AnnotatedWord<String>> sentence : sentences) {
			List<AnnotatedWord<TermCandidateMetadata>> detectedTermsInSentence = _detectTermsInSentence(sentence);
			for (AnnotatedWord<TermCandidateMetadata> term : detectedTermsInSentence) {
				AnnotatedWord<TermCandidateMetadata> storedTerm = detectedTerms.get(term
						.getWord());
				if (storedTerm != null) {
					Long occurrences = (Long) storedTerm.getAnnotation().metadata
							.get(TermCandidateMetadata.OCURRENCES);
					storedTerm.getAnnotation().metadata.put(
							TermCandidateMetadata.OCURRENCES, ++occurrences);
				} else {
					term.getAnnotation().metadata.put(TermCandidateMetadata.OCURRENCES,
							new Long(1));
					detectedTerms.put(term.getWord(), term);
				}
			}

		}
		
		return new ArrayList(detectedTerms.values());
	}

	// ---------------------------------------------------------------------------------------

	private List<AnnotatedWord<TermCandidateMetadata>> _detectTermsInSentence(
			List<AnnotatedWord<String>> sentence) {
		List<AnnotatedWord<TermCandidateMetadata>> recognizedTermsInSentence = new ArrayList<AnnotatedWord<TermCandidateMetadata>>();

		Automaton<AnnotatedWord<String>> automaton = new AutomatonImpl();
		automaton.init();
		for (AnnotatedWord<String> word : sentence) {
			Input<AnnotatedWord<String>> input = new Input<>(word);
			automaton.transit(input);
			if (automaton.getCurrentState().isInitial()) {
				List<Input<AnnotatedWord<String>>> regonizedInput = automaton
						.getRecognizedInputString();
				// System.out.println("recognizedItem > " + regonizedInput);

				if (regonizedInput.size() > 0) {
					AnnotatedWord<TermCandidateMetadata> term = _buidTerm(regonizedInput);
					recognizedTermsInSentence.add(term);
				}
				automaton.init();
			}

		}
		List<Input<AnnotatedWord<String>>> regonizedInput = automaton
				.getRecognizedInputString();
		if (regonizedInput.size() > 1) {
			AnnotatedWord<TermCandidateMetadata> term = _buidTerm(regonizedInput);
			recognizedTermsInSentence.add(term);
		}

		return recognizedTermsInSentence;
	}

	// ---------------------------------------------------------------------------------------

	private AnnotatedWord<TermCandidateMetadata> _buidTerm(
			List<Input<AnnotatedWord<String>>> regonizedInput) {
		AnnotatedWord<TermCandidateMetadata> term = new AnnotatedWord<>(new TermCandidateMetadata());
		term.setAnnotation(new TermCandidateMetadata());

		Iterator<Input<AnnotatedWord<String>>> recognizedInputIt = regonizedInput
				.iterator();
		Input<AnnotatedWord<String>> recognizedInput = recognizedInputIt.next();
		String termWord = recognizedInput.getContent().getWord().toLowerCase();

		while (recognizedInputIt.hasNext()) {
			recognizedInput = recognizedInputIt.next();
			termWord += " "
					+ recognizedInput.getContent().getWord().toLowerCase();
		}

		term.getAnnotation().setLength(regonizedInput.size());
		term.setWord(termWord);
		return term;
	}

	// ---------------------------------------------------------------------------------------

}
