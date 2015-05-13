package org.epnoi.uia.learner.relations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.epnoi.model.Relation;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.RelationsTable;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.learner.knowledgebase.KnowledgeBase;

public class RelationsHandler {
	private Core core;
	private Map<String, RelationsTable> relationsTable;
	private KnowledgeBase knowledgeBase;
	private RelationsHandlerParameters parameters;

	// ---------------------------------------------------------------------------------------------------------------------

	public RelationsHandler() {
		this.relationsTable = new HashMap<String, RelationsTable>();
	}
	
	// ---------------------------------------------------------------------------------------------------------------------

	public void init(Core core, RelationsHandlerParameters parameters) {
		this.core = core;
		this.parameters = parameters;

	}

	// ---------------------------------------------------------------------------------------------------------------------

	public List<Relation> getRelations(String sourceTerm, String domain,
			double expansionProbabilityThreshold) {
		List<Relation> relations = new ArrayList<>();
		// First we retrieve the relations that we can find in the knowledge
		// base for the source term
		for (String targetTerm : this.knowledgeBase.getHypernyms(sourceTerm)) {

			relations.add(Relation.buildKnowledgeBaseRelation(sourceTerm,
					targetTerm, RelationHelper.HYPERNYM));
		}

		// Afterthat we add those relations for such source term in the
		// relations table
		relations.addAll(relationsTable.get(domain).getRelations(sourceTerm,
				expansionProbabilityThreshold));
		return relations;
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public List<Relation> getRelations(String sourceTerm,
			double expansionProbabilityThreshold) {
		List<Relation> relations = new ArrayList<>();
		// First we retrieve the relations that we can find in the knowledge
		// base for the source term
		for (String targetTerm : this.knowledgeBase.getHypernyms(sourceTerm)) {

			relations.add(Relation.buildKnowledgeBaseRelation(sourceTerm,
					targetTerm, RelationHelper.HYPERNYM));
		}

		// Afterthat we add those relations for such source term in the
		// relations table for all the considered domains
		for (String domain : relationsTable.keySet()) {
			relations.addAll(relationsTable.get(domain).getRelations(
					sourceTerm, expansionProbabilityThreshold));
		}
		return relations;
	}

	// ---------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {

	}
}
