package org.epnoi.learner;

import org.epnoi.learner.relations.RelationEdge;
import org.epnoi.learner.terms.TermVertice;
import org.jgrapht.graph.DirectedMultigraph;

public class OntologyGraph extends
		DirectedMultigraph<TermVertice, RelationEdge> {

	public OntologyGraph() {
		super(RelationEdge.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

}
