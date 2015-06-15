package org.epnoi.uia.learner.relations.patterns.syntactic;

import java.util.logging.Logger;

import org.epnoi.uia.learner.relations.patterns.RelationalPattern;
import org.epnoi.uia.learner.relations.patterns.RelationalPatternsModel;
import org.epnoi.uia.learner.relations.patterns.RelationalPatternsModelBuilder;
import org.epnoi.uia.learner.relations.patterns.RelationalPatternsModelCreationParameters;

public class SyntacticRelationalPatternsModelBuilder implements
		RelationalPatternsModelBuilder {
	private static final Logger logger = Logger
			.getLogger(SyntacticRelationalPatternsModelBuilder.class.getName());
	private SyntacticRelationalPatternsModel model;

	// ------------------------------------------------------------------------------------------------------

	public SyntacticRelationalPatternsModelBuilder(
			RelationalPatternsModelCreationParameters parameters) {
		logger.info("Initializing the SyntacticRelationalPatternsMoldel builder");
		this.model = new SyntacticRelationalPatternsModel();
	}

	// ------------------------------------------------------------------------------------------------------

	public void addPattern(RelationalPattern relationalPattern) {
		
		this.model.addPattern(relationalPattern);
	}

	// ------------------------------------------------------------------------------------------------------

	public RelationalPatternsModel build() {
		logger.info("Building the SyntacticalRelationalPatternsModel");
		return this.model;
	}

}
