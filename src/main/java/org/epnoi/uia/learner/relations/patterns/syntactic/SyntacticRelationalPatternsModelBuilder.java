package org.epnoi.uia.learner.relations.patterns.syntactic;

import org.epnoi.uia.learner.relations.patterns.RelationalPattern;
import org.epnoi.uia.learner.relations.patterns.RelationalPatternsModel;
import org.epnoi.uia.learner.relations.patterns.RelationalPatternsModelBuilder;
import org.epnoi.uia.learner.relations.patterns.RelationalPatternsModelCreationParameters;

public class SyntacticRelationalPatternsModelBuilder implements
		RelationalPatternsModelBuilder {

	public SyntacticRelationalPatternsModelBuilder(
			RelationalPatternsModelCreationParameters parameters) {
		System.out.println("Creating the SyntacticalModel!");
	}

	public void addPattern(RelationalPattern relationalPattern) {
		System.out.println("Adding the pattern " + relationalPattern);

	}

	public RelationalPatternsModel build() {
		System.out.println("Building the SyntacticalModel!");
		return new SyntacticRelationalPatternsModel();
	}

}
