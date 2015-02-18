package org.epnoi.uia.learner.relations.lexical;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BigramSoftPatternModel implements SoftPatternModel {

	private Map<String, Double[]> unigramProbability;
	private Map<String, Map<String, Double[]>> bigramProbability;
	private LexicalRelationalModelCreationParameters parmeters;
	//private int maxPatternLength;
//	private LexicalRelationalModelCreationParameters parameters;
	private double interpolation_constant = 0.3d; // Set to this value using the
													// experimental value set in
													// Generic Soft Pattern
													// Models for Definitional
													// Question Answering

	// ---------------------------------------------------------------------------------------------------------

	protected BigramSoftPatternModel(
			LexicalRelationalModelCreationParameters parameters,
			Map<String, Double[]> unigramProbability,
			Map<String, Map<String, Double[]>> bigramProbability,
			double interpolationConstant) {
		//this.maxPatternLength = (Integer) this.parameters
			//	.getParameterValue(LexicalRelationalModelCreationParameters.MAX_PATTERN_LENGTH_PARAMETER);

		this.bigramProbability = bigramProbability;
		this.unigramProbability = unigramProbability;
		this.interpolation_constant = interpolationConstant;

	}

	// ---------------------------------------------------------------------------------------------------------

	public double getUnigramProbability(String nodeToken, int position) {
		Double[] unigramProbabilities = this.unigramProbability.get(nodeToken);
		if (unigramProbabilities != null) {
			return unigramProbabilities[position];
		} else {
			return 0d;
		}
	}

	// ---------------------------------------------------------------------------------------------------------

	public double getBigramProbability(String nodeToken,
			String followingNodeToken, int position) {
		Map<String, Double[]> nodeBigramProbabilities = this.bigramProbability
				.get(nodeToken);
		if (nodeBigramProbabilities != null) {
			Double[] probabilities = nodeBigramProbabilities
					.get(followingNodeToken);
			if (probabilities != null) {
				return probabilities[position];
			} else {
				return 0d;
			}
		} else {
			return 0d;
		}
	}

	// ---------------------------------------------------------------------------------------------------------

	@Override
	public double calculatePatternProbability(
			LexicalRelationalPattern relationalPattern) {

		if (relationalPattern.getLength() < 2) {
			return 0d;
		} else {// Generic case...
			String pastNodeToken;
			String nodeToken = relationalPattern.getNodes().get(0)
					.getGeneratedToken();

			double probability = Math.log(this.getUnigramProbability(nodeToken,
					0));
			for (int position = 1; position < relationalPattern.getLength(); position++) {
				pastNodeToken = relationalPattern.getNodes().get(position - 1)
						.getGeneratedToken();
				nodeToken = relationalPattern.getNodes().get(position)
						.getGeneratedToken();
				probability += this.interpolation_constant
						* Math.log(this.getUnigramProbability(nodeToken,
								position))
						+ (1 - this.interpolation_constant)
						* Math.log(this.getBigramProbability(pastNodeToken,
								nodeToken, position));
			}
			// The probability is normalized
			probability = probability / relationalPattern.getLength();

			// And finally brought back to the [0,1] range
			return Math.exp(probability);
		}
	}
		
	// ---------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		double whatever = Math.log(0.5d);
		System.out.println("---> " + whatever);
		whatever = Math.exp(whatever);
		System.out.println("2---> " + whatever);
	}
	
	// ---------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "BigramSoftPatternModel [unigramProbability="
				+ unigramProbability + ", bigramProbability="
				+ bigramProbability + ", parmeters=" + parmeters
				+ ", interpolation_constant="
				+ interpolation_constant + "]";
	}
}
