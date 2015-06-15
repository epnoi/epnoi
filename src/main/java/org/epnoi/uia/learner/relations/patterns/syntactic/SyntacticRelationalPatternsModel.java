package org.epnoi.uia.learner.relations.patterns.syntactic;

import java.util.ArrayList;
import java.util.List;

import org.epnoi.uia.learner.relations.patterns.RelationalPattern;
import org.epnoi.uia.learner.relations.patterns.RelationalPatternsModel;

public class SyntacticRelationalPatternsModel implements
		RelationalPatternsModel {
	private List<SyntacticRelationalPattern> patterns;

	// ------------------------------------------------------------------------------------

	public SyntacticRelationalPatternsModel() {
		this.patterns = new ArrayList<>();
	}

	// ------------------------------------------------------------------------------------

	@Override
	public double calculatePatternProbability(
			RelationalPattern relationalPattern) {
		// TODO Auto-generated method stub
		return 0;
	}

	// ------------------------------------------------------------------------------------

	@Override
	public void show() {
		System.out.println("The model has " + patterns.size() + " patterns");
		System.out.println("-----> " + patterns);
	}

	// ------------------------------------------------------------------------------------

	public void addPattern(RelationalPattern relationalPattern) {
		System.out.println(">> " + relationalPattern);
		if (!this.patterns.contains(relationalPattern)) {
			this.patterns.add((SyntacticRelationalPattern) relationalPattern);
		} else {
			System.out.println("----------------------------------------------------------The pattern " + relationalPattern
					+ " was already in the model");
		}
	}

}
