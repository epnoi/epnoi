package org.epnoi.uia.learner.relations.lexical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BigramSoftPatternModelBuilder {

	private HashMap<String, NodeInformation> nodesInformation;
	private Long[] nodesPositionsCount;
	private Long nodesCount;

	private LexicalRelationalModelCreationParameters parameters;
	private int maxPatternLenght;
	private BigramSoftPatternModel model;

	private Map<String, Map<String, Double[]>> bigramProbability;
	private Double[] nodePositionProbability;
	private Map<String, Double[]> unigramProbability;
	private final int LAPLACE_CONSTANT = 2;

	// ----------------------------------------------------------------------------------------------------------------

	public BigramSoftPatternModelBuilder(
			LexicalRelationalModelCreationParameters parameters) {
		this.parameters = parameters;
		this.nodesInformation = new HashMap<>();
		maxPatternLenght = (Integer) this.parameters
				.getParameterValue(LexicalRelationalModelCreationParameters.MAX_PATTERN_LENGTH_PARAMETER);
		this.nodesPositionsCount = new Long[maxPatternLenght];
		this.nodesCount = 0L;
		for (int i = 0; i < nodesPositionsCount.length; i++) {
			this.nodesPositionsCount[i] = 0L;
		}

		this.bigramProbability = new HashMap<>();
		this.nodePositionProbability = new Double[maxPatternLenght];
		this.unigramProbability = new HashMap<>();
	}

	// ----------------------------------------------------------------------------------------------------------------

	public void addPattern(LexicalRelationalPattern pattern) {
		System.out.println("------> " + pattern);
		List<LexicalRelationalPatternNode> nodes = pattern.getNodes();
		int position = 0;
		for (LexicalRelationalPatternNode node : pattern.getNodes()) {
			NodeInformation nodeInformation = this.nodesInformation.get(node
					.getGeneratedToken());
			if (nodeInformation == null) {
				nodeInformation = new NodeInformation(this.maxPatternLenght);
				this.nodesInformation.put(node.getGeneratedToken(),
						nodeInformation);
			}
			// Updates to the pattern node local numbers
			_updateNodeInformation(nodeInformation, node, position, nodes);
			// Updates to the global model counts
			this.nodesPositionsCount[position] = this.nodesPositionsCount[position] + 1;
			this.nodesCount++;
			position++;
		}
	}

	// ----------------------------------------------------------------------------------------------------------------

	private void _updateNodeInformation(NodeInformation nodeInformation,
			LexicalRelationalPatternNode node, int position,
			List<LexicalRelationalPatternNode> nodes) {

		nodeInformation.setCardinality(nodeInformation.getCardinality() + 1);
		nodeInformation.getPositions()[position] = nodeInformation
				.getPositions()[position] + 1;

		if (position + 1 < nodes.size()) {
			String followerToken = nodes.get(position + 1).getGeneratedToken();
			Long[] followersFrequency = nodeInformation.getFollowers().get(
					followerToken);

			if (followersFrequency == null) {
				followersFrequency = new Long[maxPatternLenght];
				for (int i = 0; i < maxPatternLenght; i++) {
					followersFrequency[i] = 0L;
				}
				nodeInformation.getFollowers().put(followerToken,
						followersFrequency);
			}
			followersFrequency[position] = followersFrequency[position] + 1;
		}

	}

	// ----------------------------------------------------------------------------------------------------------------

	private class NodeInformation {

		// private LexicalRelationalPatternNode node;//We store the token that
		// represents the node

		private long cardinality;// # ocurrences in the corpus
		private HashMap<String, Long[]> followers; // Frequencies of the
													// tokens
													// that follow this one
													// on each of the
													// possible positions
		private Long[] positions; // Histogram of the positions that
									// the node occupies in the pattern

		// ----------------------------------------------------------------------------------------------------------------

		public NodeInformation(int maxPatternLength) {
			this.cardinality = 0;
			this.followers = new HashMap<>();

			this.positions = new Long[maxPatternLength];
			for (int i = 0; i < positions.length; i++) {
				// this.followers.add(new HashMap<String, Long>());
				this.positions[i] = 0L;
			}

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

		public Long[] getPositions() {
			return positions;
		}

		// ----------------------------------------------------------------------------------------------------------------

		public void setPositions(Long[] positions) {
			this.positions = positions;
		}

		// ----------------------------------------------------------------------------------------------------------------

		@Override
		public String toString() {
			return "NodeInformation [cardinality=" + cardinality
					+ ", followers=" + followers + ", positions="
					+ Arrays.toString(positions) + "]";
		}

		public HashMap<String, Long[]> getFollowers() {
			return followers;
		}

		public void setFollowers(HashMap<String, Long[]> followers) {
			this.followers = followers;
		}

		// ----------------------------------------------------------------------------------------------------------------

	}

	// ----------------------------------------------------------------------------------------------------------------

	public BigramSoftPatternModel build() {
		System.out.println("-> " + this.nodesInformation);
		for (Entry<String, NodeInformation> nodeInformationEntry : this.nodesInformation
				.entrySet()) {
			String nodeToken = nodeInformationEntry.getKey();
			NodeInformation nodeInformation = nodeInformationEntry.getValue();
			_calculateUnigramProbability(nodeToken, nodeInformation);
			_calculateBigramProbability(nodeToken, nodeInformation);

		}

		return this.model;
	}

	// ----------------------------------------------------------------------------------------------------------------

	private void _calculateUnigramProbability(String nodeToken,
			NodeInformation nodeInformation) {
		Double[] positionUnigramProbabilities = new Double[maxPatternLenght];
		for (int position = 0; position < maxPatternLenght; position++) {
			Double positionUnigramProbability = (double) (nodeInformation
					.getPositions()[position] + this.LAPLACE_CONSTANT)
					/ (double) (this.nodesPositionsCount[position] + this.LAPLACE_CONSTANT
							* this.nodesInformation.size());
			positionUnigramProbabilities[position] = positionUnigramProbability;
		}
		this.unigramProbability.put(nodeToken, positionUnigramProbabilities);
	}

	// ----------------------------------------------------------------------------------------------------------------
	
	private void _calculateBigramProbability(String nodeToken,
			NodeInformation nodeInformation) {
		for (Entry<String, Long[]> followerEntry : nodeInformation
				.getFollowers().entrySet()) {
			String followerToken = followerEntry.getKey();
			Long[] followerCounts = followerEntry.getValue();
			for (int i = 0; i < maxPatternLenght; i++) {
				if (nodeInformation.getPositions()[i] > 0) {
					/*
					System.out.println("For position" + i + " " + nodeToken
							+ " appears " + nodeInformation.getPositions()[i]
							+ "and is followed by " + followerToken + " "
							+ followerCounts[i] + "times");*/
					Double bigramProbability = ((double) followerCounts[i] / (double) nodeInformation
							.getPositions()[i]);
					/*
					System.out
							.println("------------------------------------------:> "
									+ bigramProbability);
									*/
				}
			}
		}
	}

	// ----------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {

		Long[] positions = new Long[10];
		for (int i = 0; i < positions.length; i++)
			System.out.println(":> " + positions[i]);
	}

}
