package org.epnoi.learner.terms;

import org.epnoi.learner.automata.Automaton;
import org.epnoi.learner.automata.AutomatonImpl;
import org.epnoi.learner.automata.Input;
import org.epnoi.model.AnnotatedWord;
import org.epnoi.model.TermMetadata;

import java.util.*;

public class TermsDetector {

	// ---------------------------------------------------------------------------------------

	public List<AnnotatedWord<TermMetadata>> detect(
			List<List<AnnotatedWord<String>>> sentences) {
		Map<String, AnnotatedWord<TermMetadata>> detectedTerms = new HashMap<>();

		for (List<AnnotatedWord<String>> sentence : sentences) {
			List<AnnotatedWord<TermMetadata>> detectedTermsInSentence = _detectTermsInSentence(sentence);
			for (AnnotatedWord<TermMetadata> term : detectedTermsInSentence) {
				AnnotatedWord<TermMetadata> storedTerm = detectedTerms.get(term
						.getWord());
				if (storedTerm != null) {
					Long occurrences = (Long) storedTerm.getAnnotation().getOcurrences();
					storedTerm.getAnnotation().setOcurrences(
							 ++occurrences);
				} else {
					term.getAnnotation().setOcurrences(new Long(1));
					detectedTerms.put(term.getWord(), term);
				}
			}

		}
		
		return new ArrayList<AnnotatedWord<TermMetadata>>(detectedTerms.values());
	}

	// ---------------------------------------------------------------------------------------

	private List<AnnotatedWord<TermMetadata>> _detectTermsInSentence(
			List<AnnotatedWord<String>> sentence) {
		List<AnnotatedWord<TermMetadata>> recognizedTermsInSentence = new ArrayList<AnnotatedWord<TermMetadata>>();

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
					AnnotatedWord<TermMetadata> term = _buidTerm(regonizedInput);
					recognizedTermsInSentence.add(term);
				}
				automaton.init();
			}

		}
		List<Input<AnnotatedWord<String>>> regonizedInput = automaton
				.getRecognizedInputString();
		if (regonizedInput.size() > 1) {
			AnnotatedWord<TermMetadata> term = _buidTerm(regonizedInput);
			recognizedTermsInSentence.add(term);
		}

		return recognizedTermsInSentence;
	}

	// ---------------------------------------------------------------------------------------

	private AnnotatedWord<TermMetadata> _buidTerm(
			List<Input<AnnotatedWord<String>>> regonizedInput) {
		AnnotatedWord<TermMetadata> term = new AnnotatedWord<>(new TermMetadata());
		term.setAnnotation(new TermMetadata());

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
