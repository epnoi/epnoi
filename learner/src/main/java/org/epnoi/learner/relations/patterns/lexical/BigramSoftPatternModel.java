package org.epnoi.learner.relations.patterns.lexical;

import gate.util.compilers.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.epnoi.learner.relations.patterns.RelationalPattern;
import org.epnoi.learner.relations.patterns.RelationalPatternsModel;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelCreationParameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BigramSoftPatternModel implements RelationalPatternsModel {

	private static final long serialVersionUID = 9103308220708737439L;
	private static final int MAX_PATTERN_LENGTH = 20;
	private Map<String, Double[]> unigramProbability;
	private Map<String, Map<String, Double[]>> bigramProbability;
	private RelationalPatternsModelCreationParameters parmeters;
	// private int maxPatternLength;
	// private LexicalRelationalModelCreationParameters parameters;
	private double interpolation_constant = 0.7d; // Set to this value using the
													// experimental value set in
													// Generic Soft Pattern
													// Models for Definitional
													// Question Answering

	// ---------------------------------------------------------------------------------------------------------

	public BigramSoftPatternModel() {
		this.unigramProbability = new HashMap<String, Double[]>();
		this.bigramProbability = new HashMap<String, Map<String, Double[]>>();
		this.unigramProbability.put("algo", new Double[] { 1.0, 2.0, 3.0 });
	}

	protected BigramSoftPatternModel(
			RelationalPatternsModelCreationParameters parameters,
			Map<String, Double[]> unigramProbability,
			Map<String, Map<String, Double[]>> bigramProbability,
			double interpolationConstant) {
		// this.maxPatternLength = (Integer) this.parameters
		// .getParameterValue(LexicalRelationalModelCreationParameters.MAX_PATTERN_LENGTH);

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
	public double calculatePatternProbability(RelationalPattern pattern) {
		long currentTime = System.currentTimeMillis();
		LexicalRelationalPattern relationalPattern = (LexicalRelationalPattern) pattern;
		// System.out.println(relationalPattern);
		if ((relationalPattern.getLength() < 2)
				|| (relationalPattern.getLength() > MAX_PATTERN_LENGTH)) {
			return 0d;
		} else {// Generic case...
			String pastNodeToken;
			String nodeToken = relationalPattern.getNodes().get(0)
					.getGeneratedToken();

			double probability = this.getUnigramProbability(nodeToken,
					0);
			for (int position = 1; position < relationalPattern.getLength(); position++) {
				pastNodeToken = relationalPattern.getNodes().get(position - 1)
						.getGeneratedToken();
				nodeToken = relationalPattern.getNodes().get(position)
						.getGeneratedToken();
				probability += (this.interpolation_constant
						* this.getUnigramProbability(nodeToken,
								position)
						+ (1 - this.interpolation_constant)
						* this.getBigramProbability(pastNodeToken,
								nodeToken, position));
			}
			// The probability is normalized
			probability = probability / relationalPattern.getLength();

			// And finally brought back to the [0,1] range
			// System.out.println(Math.exp(probability));
			System.out.println("Total time "+(System.currentTimeMillis()-currentTime));
			return probability;
		}
	}

	// ----------------------------------------------------------------------------------------------------------------

	public void show() {
		System.out.println("The model's vocabulary cardinality is "
				+ this.unigramProbability.size());
		System.out.println("They are " + this.unigramProbability.keySet());
		System.out
				.println("Unigrams=========================================================================================");
		for (Entry<String, Double[]> unigramProbability : this.unigramProbability
				.entrySet()) {
			System.out.println("<" + unigramProbability.getKey() + "|"
					+ Arrays.toString(unigramProbability.getValue()) + ">");
		}
		System.out.println();
		System.out
				.println("Bigrams=========================================================================================");
		System.out.println();
		for (Entry<String, Map<String, Double[]>> entry : this.bigramProbability
				.entrySet()) {
			for (Entry<String, Double[]> innerEntry : entry.getValue()
					.entrySet()) {
				System.out.println("<" + entry.getKey() + ","
						+ innerEntry.getKey() + ">= "
						+ Arrays.toString(innerEntry.getValue()));
			}
		}

	}

	// ---------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "BigramSoftPatternModel [unigramProbability="
				+ _unigramProbabilityToString() + ", bigramProbability="
				+ _bigramProbabilityToString() + ", parmeters=" + parmeters
				+ ", interpolation_constant=" + interpolation_constant + "]";
	}

	// ---------------------------------------------------------------------------------------------------------

	private String _unigramProbabilityToString() {
		String result = "";
		for (Entry<String, Double[]> entry : this.unigramProbability.entrySet()) {
			result += ", " + entry.getValue() + " "
					+ Arrays.toString(entry.getValue());
		}
		return result;
	}

	// ---------------------------------------------------------------------------------------------------------

	private String _bigramProbabilityToString() {
		String result = "";
		for (Entry<String, Map<String, Double[]>> entry : this.bigramProbability
				.entrySet()) {
			for (Entry<String, Double[]> innerEntry : entry.getValue()
					.entrySet()) {
				result += ",(" + entry.getValue() + "," + entry.getValue()
						+ "]= " + Arrays.toString(innerEntry.getValue());
			}
		}
		return result;
	}

}
