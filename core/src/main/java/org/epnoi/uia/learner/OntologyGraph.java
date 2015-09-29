package org.epnoi.uia.learner;

import org.epnoi.uia.learner.relations.RelationEdge;
import org.epnoi.uia.learner.terms.TermVertice;
import org.jgrapht.EdgeFactory;
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
