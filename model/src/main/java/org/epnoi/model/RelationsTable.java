package org.epnoi.model;

import java.util.*;



public class RelationsTable implements Resource {

	private String uri;
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

	/**
	 * 
	 * @param sourceURI
	 * @param expansionProbabilityThreshold
	 * @return
	 */

	public List<Relation> getRelations(String sourceURI,
			double expansionProbabilityThreshold) {
		List<Relation> relations = new ArrayList<>();
		for (Relation relationFromSource : this.relationsBySource
				.get(sourceURI)) {
			if (relationFromSource.calculateRelationhood() >= expansionProbabilityThreshold) {
				relations.add(relationFromSource);
			}
		}
		return relations;
	}

	// --------------------------------------------------------------------

	/**
	 * 
	 * @param sourceURI
	 * @param expansionProbabilityThreshold
	 * @return
	 */

	public List<Relation> getRelations(String sourceURI, String type,
			double expansionProbabilityThreshold) {
		System.out.println("size> " + this.orderedRelations.size());
		List<Relation> relations = new ArrayList<>();
		for (Relation relationFromSource : this.relationsBySource
				.get(sourceURI)) {
			if (type.equals(relationFromSource.getType())
					&& relationFromSource.calculateRelationhood() >= expansionProbabilityThreshold) {
				relations.add(relationFromSource);
			}
		}
		return relations;
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
			if (relationA.calculateRelationhood() < relationB.calculateRelationhood()) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	// --------------------------------------------------------------------

	public void introduceRelation(String domain, Term sourceTerm,
			Term targetTerm, String type, String provenanceSentence,
			double relationhood) {

		String relationURI = Relation.buildURI(sourceTerm.getAnnotatedTerm()
				.getWord(), targetTerm.getAnnotatedTerm().getWord(), type,
				domain);
		System.out.println("RelationURI > " + relationURI);
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
			System.out
					.println("ME SALE QUE ESTABA! " + orderedRelations.size());

		} else {
			// If the relation is not already stored, we simply add it
			Relation relation = new Relation();
			relation.setUri(relationURI);
			relation.setSource(sourceTerm.getUri());
			relation.setTarget(targetTerm.getUri());
			relation.setType(type);
			relation.addProvenanceSentence(provenanceSentence, relationhood);

		
			
			
			this.orderedRelations.put(relation, relation.getUri());
			this.relations.put(relation.getUri(), relation);
			List<Relation> relations = this.relationsBySource.get(relation
					.getSource());
			if (relations == null) {
				relations = new ArrayList<>();
				this.relationsBySource.put(relation.getSource(), relations);
			}

			relations.add(relation);
			System.out.println("NO ESTABA " + orderedRelations.size());

		}
	}

	// --------------------------------------------------------------------

	public void addRelation(Relation relation) {

		this.orderedRelations.put(relation, relation.getUri());
		this.relations.put(relation.getUri(), relation);

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

	public Collection<Relation> getRelations(String type) {
		List<Relation> relations = new ArrayList<>();
		for (Relation relation : this.relations.values()) {
			if (type.equals(relation.getType())) {
				relations.add(relation);
			}
		}
		return relations;
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

	public String getUri() {
		return uri;
	}

	// --------------------------------------------------------------------

	public void setUri(String uRI) {
		uri = uRI;
	}

	// --------------------------------------------------------------------

	@Override
	public String toString() {
		return "RelationsTable [uri= " + uri + ", relations=" + relations + "]";
	}

	// --------------------------------------------------------------------

	public void show(int numberOfDeatiledTerms) {

		System.out
				.println("=====================================================================================================================");
		System.out.println("Terms Table");

		System.out
				.println("=====================================================================================================================");

		System.out.println("# of candidate relations: " + this.size());
		System.out.println("The top most " + numberOfDeatiledTerms
				+ " probable relations are: ");
		int i = 1;
		for (Relation term : this.getMostProbable(numberOfDeatiledTerms)) {
			System.out.println("(" + i++ + ")" + term.getSource() + " > "
					+ term.getType() + " > " + term.getTarget());

			System.out
					.println("------------------------------------------------------");
			System.out.println(term);
			System.out
					.println("------------------------------------------------------");

		}

		System.out
				.println("=====================================================================================================================");
		System.out
				.println("=====================================================================================================================");
	}

}
