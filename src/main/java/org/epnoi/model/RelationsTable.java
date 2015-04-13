package org.epnoi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.epnoi.uia.learner.terms.TermVertice;

public class RelationsTable implements Resource {

	private String URI;
	private Map<String, Relation> relations;
	private Map<Relation, String> orderedRelations;
	private Map<String, List<Relation>> relationsBySource;

	// --------------------------------------------------------------------

	public RelationsTable() {

		this.orderedRelations = new TreeMap<Relation, String>(
				new RelationsComparator());
		this.relations = new HashMap<>();
		this.relationsBySource = new HashMap<>();
	}

	// --------------------------------------------------------------------

	public List<Relation> getRelations(TermVertice termToExpand,
			double expansionProbabilityThreshold) {
		this.relationsBySource.get(termToExpand.getTerm());
		return new ArrayList<Relation>();
	}

	// --------------------------------------------------------------------

	public List<Relation> getMostProbable(int initialNumberOfRelations) {
		List<Relation> mostProblableRelations = new ArrayList<Relation>();
		Iterator<Relation> relationsIt = this.orderedRelations.keySet()
				.iterator();
		int i = 0;
		while (i < initialNumberOfRelations && relationsIt.hasNext()) {

			Relation relation = relationsIt.next();
			mostProblableRelations.add(relation);
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

	public void introduceRelation(String domain, Term source, Term target,
			String type, String provenanceSentence, double relationhood) {

		String relationURI = Relation.buildURI(source.getAnnotatedTerm()
				.getWord(), target.getAnnotatedTerm().getWord(), type, domain);

		if (this.hasRelation(relationURI)) {
			// If the relation is already in the Relations Table, we have to
			// update just
			// add the new provenance sentence along with its relationhood
			Relation storedRelation = this.getRelation(relationURI);
			storedRelation.addProvenanceSentence(provenanceSentence,
					relationhood);

			// Since the relationhood of the relation has been update, we must
			// update its position in the ordered MapTree
			this.orderedRelations.remove(storedRelation);
			this.orderedRelations.put(storedRelation, relationURI);

		} else {
			// If the relation is not already stored, we simply add it
			Relation relation = new Relation();
			relation.setURI(relationURI);
			relation.setSource(source.getURI());
			relation.setTarget(target.getURI());

			relation.addProvenanceSentence(provenanceSentence, relationhood);

			this.orderedRelations.put(relation, relation.getURI());
			this.relations.put(relation.getURI(), relation);
			List<Relation> relations = this.relationsBySource.get(relation
					.getSource());
			if (relations == null) {
				relations = new ArrayList<>();
				this.relationsBySource.put(relation.getSource(), relations);
			}
			relations.add(relation);
			System.out.println("RELATION-----------------> " + relation);
		}
	}

	// --------------------------------------------------------------------

	public void addRelation(Relation relation) {

		this.orderedRelations.put(relation, relation.getURI());
		this.relations.put(relation.getURI(), relation);

		List<Relation> relations = this.relationsBySource.get(relation
				.getSource());
		if (relations == null) {
			relations = new ArrayList<>();
			this.relationsBySource.put(relation.getSource(), relations);
		}
		relations.add(relation);

	}

	// --------------------------------------------------------------------

	public Relation getRelation(String URI) {
		return this.relations.get(URI);
	}

	// --------------------------------------------------------------------

	public Collection<Relation> getRelations() {
		return this.relations.values();
	}

	// --------------------------------------------------------------------

	public boolean hasRelation(String URI) {
		return (this.relations.get(URI) != null);
	}

	// --------------------------------------------------------------------

	public int size() {
		return this.relations.size();
	}

	// --------------------------------------------------------------------

	public String getURI() {
		return URI;
	}

	// --------------------------------------------------------------------

	public void setURI(String uRI) {
		URI = uRI;
	}

	// --------------------------------------------------------------------

	@Override
	public String toString() {
		return "RelationsTable [relations=" + relations + "]";
	}

	// --------------------------------------------------------------------

}
