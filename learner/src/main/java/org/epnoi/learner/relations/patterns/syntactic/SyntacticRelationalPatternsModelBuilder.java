package org.epnoi.learner.relations.patterns.syntactic;

import org.apache.mahout.classifier.sgd.L2;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.epnoi.learner.relations.patterns.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SyntacticRelationalPatternsModelBuilder implements
		RelationalPatternsModelBuilder {
	private static final Logger logger = Logger
			.getLogger(SyntacticRelationalPatternsModelBuilder.class.getName());

	private OnlineLogisticRegression logisticRegressionClassifier;
	private SyntacticRelationalPatternsModel model;
	private List<SyntacticRelationalPattern> patterns;
	private List<SyntacticRelationalPattern> negativePatterns;

	private int repeatedPatterns;

	// ------------------------------------------------------------------------------------------------------

	public SyntacticRelationalPatternsModelBuilder(
			RelationalPatternsModelCreationParameters parameters) {
		logger.info("Initializing the SyntacticRelationalPatternsMoldel builder");
		this.model = new SyntacticRelationalPatternsModel(
				logisticRegressionClassifier);
		this.patterns = new ArrayList<>();
		this.negativePatterns = new ArrayList<>();
		// this.matchingMatrix = new ArrayList<>();
		this.repeatedPatterns = 0;

	}

	// ------------------------------------------------------------------------------------------------------

	private Vector _obtainPatternFeatures(
			SyntacticRelationalPattern relationalPattern) {
		Vector features = new RandomAccessSparseVector(this.patterns.size());
		int i = 0;
		for (SyntacticRelationalPattern pattern : this.patterns) {
			// The bias is always set to 1
			features.set(0, 1);
			// For each
			features.set(i, (relationalPattern.matches(pattern)) ? 1 : 0);

			i++;
		}
		return features;
	}

	// ------------------------------------------------------------------------------------------------------

	public void addPattern(RelationalPattern relationalPattern) {
		if (!this.patterns.contains(relationalPattern)) {
			this.patterns.add((SyntacticRelationalPattern) relationalPattern);
		} else {
			this.repeatedPatterns++;
		}
	}

	// ------------------------------------------------------------------------------------------------------

	public void addNegativePattern(RelationalPattern relationalPattern) {

		this.negativePatterns
				.add((SyntacticRelationalPattern) relationalPattern);
	}

	// ------------------------------------------------------------------------------------------------------

	public RelationalPatternsModel build() {
		logger.info("Building the SyntacticalRelationalPatternsModel");

		int numberOfFeatures = this.patterns.size();

		logger.info("The model has " + numberOfFeatures + " patterns, "
				+ this.repeatedPatterns
				+ " were removed since were considered redundant");

		this.logisticRegressionClassifier = new OnlineLogisticRegression(2,
				numberOfFeatures, new L2(1));

		logger.info("The model has " + patterns.size() + " patterns, "
				+ this.repeatedPatterns
				+ " were removed since were considered redundant");

		_trainClassifier();

		return this.model;

	}

	// ----------------------------------------------------------------------------------------

	private void _trainClassifier() {
		List<Vector> patternsFeatures = new ArrayList<>();
		for (SyntacticRelationalPattern pattern : this.patterns) {
			patternsFeatures.add(_obtainPatternFeatures(pattern));
		}
		List<Vector> negativePatternsFeatures = new ArrayList<>();
		for (SyntacticRelationalPattern negativePattern : this.patterns) {
			negativePatternsFeatures
					.add(_obtainPatternFeatures(negativePattern));
		}

	}

	// ----------------------------------------------------------------------------------------

	class SyntacticRelationalPatternsModel implements RelationalPatternsModel {

		private OnlineLogisticRegression logisticRegressionClassifier;

		// ------------------------------------------------------------------------------------

		private SyntacticRelationalPatternsModel(
				OnlineLogisticRegression classifier) {
			this.logisticRegressionClassifier = logisticRegressionClassifier;

		}

		// ------------------------------------------------------------------------------------

		@Override
		public double calculatePatternProbability(
				RelationalPattern relationalPattern) {
			Vector patternFeatures = _obtainPatternFeatures((SyntacticRelationalPattern) relationalPattern);
			double relationalesult = this.logisticRegressionClassifier
					.classifyFull(patternFeatures).get(
							PatternsConstants.RELATIONAL);

			return relationalesult;
		}

		// ------------------------------------------------------------------------------------

		@Override
		public void show() {
			System.out
					.println("The model is composed by the logistic regression classifier "
							+ this.logisticRegressionClassifier.toString());
		}

	}
}
