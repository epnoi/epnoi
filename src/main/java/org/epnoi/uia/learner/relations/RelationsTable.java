package org.epnoi.uia.learner.relations;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.epnoi.model.Term;
import org.epnoi.uia.learner.terms.TermVertice;

public class RelationsTable {

	private Map<String, Relation> relations;
	private Map<Relation, String> orderedRelations;

	// --------------------------------------------------------------------

	public RelationsTable() {

		this.orderedRelations = new TreeMap<Relation, String>(
				new RelationsComparator());
		this.relations = new HashMap<String, Relation>();
	}

	// --------------------------------------------------------------------

	public List<Relation> getRelations(TermVertice termToExpand,
			double expansionProbabilityThreshold) {
		// TODO Auto-generated method stub
		return new ArrayList<Relation>();
	}

	// --------------------------------------------------------------------

	public List<Relation> getMostProbable(int initialNumberOfRelations) {
		List<Relation> mostProblableRelations = new ArrayList<Relation>();
		Iterator<Relation> relationsIt = this.orderedRelations.keySet()
				.iterator();
		int i = 0;
		while (i < initialNumberOfRelations && relationsIt.hasNext()) {

			Relation term = relationsIt.next();
			mostProblableRelations.add(term);
			i++;
		}

		return mostProblableRelations;
	}

	// --------------------------------------------------------------------

	class RelationsComparator implements Comparator<Relation> {
		public RelationsComparator() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public int compare(Relation relationA, Relation relationB) {
			if (relationA.getRelationhood() < relationB.getRelationhood()) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	// --------------------------------------------------------------------

	public void addRelation(Relation relation) {
		this.orderedRelations.put(relation, relation.getURI());
		this.relations.put(relation.getURI(), relation);
	}

	// --------------------------------------------------------------------

	public Relation getRelation(String URI) {
		return this.relations.get(URI);
	}

	// --------------------------------------------------------------------

	public boolean hasRelation(String URI) {
		return (this.relations.get(URI) != null);
	}

	// --------------------------------------------------------------------

	public int size() {
		return this.relations.size();
	}

}
