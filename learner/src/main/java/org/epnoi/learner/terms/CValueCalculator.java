package org.epnoi.learner.terms;

import org.epnoi.model.AnnotatedWord;
import org.epnoi.model.TermMetadata;

public class CValueCalculator {
	private static final double LENGHT_ONE_WEIGTH = 0.5;

	public static double calculateCValue(
			AnnotatedWord<TermMetadata> termCandidate) {

		double lenghtWeight;
		if (termCandidate.getAnnotation().getLength() == 1) {
			lenghtWeight = LENGHT_ONE_WEIGTH;
		} else {
			lenghtWeight = termCandidate.getAnnotation().getLength()
					* (Math.log(termCandidate.getAnnotation().getLength()) / Math
							.log(2));
		}

		double cValue;

		if (termCandidate.getAnnotation().getNumberOfSuperterns() == 0) {
			cValue = lenghtWeight
					* termCandidate.getAnnotation().getOcurrences();

		} else {
			cValue = lenghtWeight
					* (termCandidate.getAnnotation().getOcurrences() - (termCandidate
							.getAnnotation().getOcurrencesAsSubterm() / termCandidate
							.getAnnotation().getNumberOfSuperterns()));

		}

		return cValue;
	}
}
