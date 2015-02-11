package org.epnoi.uia.learner.relations.lexical;

import java.util.HashMap;
import java.util.List;

public class BigramSoftPatternModel {

	private HashMap<String, NodeInformation> nodesInformation;

	private LexicalRelationalModelCreationParameters parameters;
	private int maxPatternLenght;

	// ----------------------------------------------------------------------------------------------------------------

	public BigramSoftPatternModel(
			LexicalRelationalModelCreationParameters parameters) {
		this.parameters = parameters;
		this.nodesInformation = new HashMap<>();
		maxPatternLenght = (Integer) this.parameters
				.getParameterValue(LexicalRelationalModelCreationParameters.MAX_PATTERN_LENGTH_PARAMETER);

	}

	// ----------------------------------------------------------------------------------------------------------------

	public void addPattern(LexicalRelationalPattern pattern) {
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
			_updateNodeInformation(nodeInformation, node, position, nodes);
		}
	}

	// ----------------------------------------------------------------------------------------------------------------

	public double calculatePatternProbability(
			LexicalRelationalModelCreationParameters parameters) {
		return 0d;
	}

	// ----------------------------------------------------------------------------------------------------------------

	private void _updateNodeInformation(NodeInformation nodeInformation,
			LexicalRelationalPatternNode node, int position,
			List<LexicalRelationalPatternNode> nodes) {

		nodeInformation.setCardinality(nodeInformation.getCardinality() + 1);

	}

	// ----------------------------------------------------------------------------------------------------------------

	private void _updateNodeInformationPosition(
			NodeInformation nodeInformation, int position,
			List<LexicalRelationalPatternNode> nodes) {

	}

	// ----------------------------------------------------------------------------------------------------------------

	private class NodeInformation {

		// private LexicalRelationalPatternNode node;//We store the token that
		// represents the node

		private long cardinality;// # ocurrences in the corpus
		private HashMap<String, Long> followers; // Frequencies of the tokens
													// that follow this one
		private Long[] positions; // Histogram of the positions that
									// the node occupies in the pattern

		// ----------------------------------------------------------------------------------------------------------------

		public NodeInformation(int maxPatternLength) {
			this.cardinality = 0;
			this.followers = new HashMap<>();
			this.positions = new Long[maxPatternLength];

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

		public HashMap<String, Long> getFollowers() {
			return followers;
		}

		public void setFollowers(HashMap<String, Long> followers) {
			this.followers = followers;
		}

		public Long[] getPositions() {
			return positions;
		}

		public void setPositions(Long[] positions) {
			this.positions = positions;
		}

	}

	// ----------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {

		Long[] positions = new Long[10];
		for (int i = 0; i < positions.length; i++)
			System.out.println(":> " + positions[i]);
	}
}
