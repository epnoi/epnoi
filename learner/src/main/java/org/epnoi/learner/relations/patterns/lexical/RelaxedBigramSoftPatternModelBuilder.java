package org.epnoi.learner.relations.patterns.lexical;

import org.epnoi.learner.relations.patterns.RelationalPattern;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelBuilder;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelCreationParameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class RelaxedBigramSoftPatternModelBuilder implements
		RelationalPatternsModelBuilder {
	private static final Logger logger = Logger
			.getLogger(BigramSoftPatternModelBuilder.class.getName());
	private static final long LAPLACE_CONSTANT = 1;
	private HashMap<String, NodeInformation> nodesInformation;
	private Long nodesCount;

	private RelationalPatternsModelCreationParameters parameters;
	private int maxPatternLenght = 20;
	private int minPatternLenght = 2;
	private double interpolationConstant;
	private Map<String, Map<String, Double>> bigramProbability;

	private Map<String, Double> unigramProbability;

	public static final LexicalRelationalPatternNode startNode = new LexicalRelationalPatternNode();
	public static final LexicalRelationalPatternNode endNode = new LexicalRelationalPatternNode();
	static {
		startNode.setGeneratedToken("<start>");
		startNode.setOriginialToken("none");
		endNode.setGeneratedToken("<end>");
		endNode.setOriginialToken("none");

	}

	// ----------------------------------------------------------------------------------------------------------------

	public RelaxedBigramSoftPatternModelBuilder(
			RelationalPatternsModelCreationParameters parameters) {
		this.parameters = parameters;
		this.nodesInformation = new HashMap<>();

		this.nodesCount = 0L;
		this.bigramProbability = new HashMap<>();
		this.unigramProbability = new HashMap<>();
		this.interpolationConstant = (double) parameters
				.getParameterValue(RelationalPatternsModelCreationParameters.INTERPOLATION_CONSTANT);
	}

	// ----------------------------------------------------------------------------------------------------------------
	@Override
	public void addPattern(RelationalPattern pattern) {

		LexicalRelationalPattern lexicalRelationalPattern = (LexicalRelationalPattern) pattern;

		if (lexicalRelationalPattern.getLength() < maxPatternLenght) {

			List<LexicalRelationalPatternNode> nodes = lexicalRelationalPattern
					.getNodes();

			_addStartEndNodes(nodes);

			int position = 0;
			int patternLength = lexicalRelationalPattern.getNodes().size();

			while (position < patternLength) {
				LexicalRelationalPatternNode node = lexicalRelationalPattern
						.getNodes().get(position);

				LexicalRelationalPatternNode nextNode = null;
				if (position != patternLength - 1) {
					nextNode = lexicalRelationalPattern.getNodes().get(
							position + 1);
				}
				// Updates to the pattern node local numbers
				_updateNodeInformation(node, nextNode);
				// Update the global number
				nodesCount++;
				position++;
			}
		}
	}

	private void _addStartEndNodes(List<LexicalRelationalPatternNode> nodes) {
		nodes.add(0, RelaxedBigramSoftPatternModelBuilder.startNode);
		nodes.add(RelaxedBigramSoftPatternModelBuilder.endNode);
	}

	// ----------------------------------------------------------------------------------------------------------------

	@Override
	public void addNegativePattern(RelationalPattern pattern) {
		logger.info("Negative patterns are ignored since this is a generative model that only uses positive patterns!");
	}

	// ----------------------------------------------------------------------------------------------------------------

	private void _updateNodeInformation(LexicalRelationalPatternNode node,
			LexicalRelationalPatternNode nextNode) {

		NodeInformation nodeInformation = this.nodesInformation.get(node
				.getGeneratedToken());

		if (nodeInformation == null) {
			nodeInformation = new NodeInformation();
			this.nodesInformation
					.put(node.getGeneratedToken(), nodeInformation);
		}

		nodeInformation.setCardinality(nodeInformation.getCardinality() + 1);
		if (nextNode != null) {

			Long followersFrequency = nodeInformation.getFollowers().get(
					nextNode.getGeneratedToken());

			if (followersFrequency == null) {
				followersFrequency = 1L;

				nodeInformation.getFollowers().put(
						nextNode.getGeneratedToken(), followersFrequency);
			} else {
				nodeInformation.getFollowers().put(
						nextNode.getGeneratedToken(), followersFrequency + 1);
			}
		}

	}

	// ----------------------------------------------------------------------------------------------------------------

	private class NodeInformation {

		// private LexicalRelationalPatternNode node;//We store the token that
		// represents the node

		private long cardinality;// # ocurrences in the corpus
		private HashMap<String, Long> followersCardinality; // Frequencies of
															// the

		// tokens
		// that follow this one
		// on each of the
		// possible positions

		// ----------------------------------------------------------------------------------------------------------------

		public NodeInformation() {
			this.cardinality = 0;
			this.followersCardinality = new HashMap<>();
		}

		// ----------------------------------------------------------------------------------------------------------------

		public long getCardinality() {
			return cardinality;
		}

		// ----------------------------------------------------------------------------------------------------------------

		public void setCardinality(long cardinality) {
			this.cardinality = cardinality;
		}

		// ----------------------------------------------------------------------------------------------------------------

		@Override
		public String toString() {
			return "NodeInformation [cardinality=" + cardinality
					+ ", followers=" + followersCardinality + "]";
		}

		// ----------------------------------------------------------------------------------------------------------------

		public HashMap<String, Long> getFollowers() {
			return followersCardinality;
		}

		// ----------------------------------------------------------------------------------------------------------------

		public void setFollowers(HashMap<String, Long> followers) {
			this.followersCardinality = followers;
		}

		// ----------------------------------------------------------------------------------------------------------------

	}

	// ----------------------------------------------------------------------------------------------------------------

	public RelaxedBigramSoftPatternModel build() {
		logger.info("Building the RelaxedBigramSoftPatternModel");
		for (Entry<String, NodeInformation> nodeInformationEntry : this.nodesInformation
				.entrySet()) {
			String nodeToken = nodeInformationEntry.getKey();
			NodeInformation nodeInformation = nodeInformationEntry.getValue();
			_calculateUnigramProbability(nodeToken, nodeInformation);
			_calculateBigramProbability(nodeToken, nodeInformation);

		}

		return new RelaxedBigramSoftPatternModel(
				this.unigramProbability, this.bigramProbability, this.interpolationConstant);

	}

	// ----------------------------------------------------------------------------------------------------------------

	private void _calculateUnigramProbability(String nodeToken,
			NodeInformation nodeInformation) {

		/*
		 * Double unigramProbability = ((double)
		 * (nodeInformation.getCardinality() + this.LAPLACE_CONSTANT) / (double)
		 * (nodesCount + this.LAPLACE_CONSTANT this.nodesInformation.size()));
		 */
		Double unigramProbability = ((double) (nodeInformation.getCardinality()) / (double) (nodesCount));
		this.unigramProbability.put(nodeToken, unigramProbability);
	}

	// ----------------------------------------------------------------------------------------------------------------

	private void _calculateBigramProbability(String nodeToken,
			NodeInformation nodeInformation) {
		HashMap<String, Double> nodeBigramProbabilities = new HashMap<>();

		for (Entry<String, Long> followerEntry : nodeInformation.getFollowers()
				.entrySet()) {

			String followerToken = followerEntry.getKey();

			Long followerCount = followerEntry.getValue();
			Double bigramProbability = ((double) followerCount / (double) nodeInformation
					.getCardinality());

			nodeBigramProbabilities.put(followerToken, bigramProbability);

		}
		this.bigramProbability.put(nodeToken, nodeBigramProbabilities);
	}

	// ----------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {

		Long[] positions = new Long[10];
		for (int i = 0; i < positions.length; i++)
			System.out.println(":> " + positions[i]);
	}

}
