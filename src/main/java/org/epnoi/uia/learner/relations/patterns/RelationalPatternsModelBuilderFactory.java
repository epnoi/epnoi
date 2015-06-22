package org.epnoi.uia.learner.relations.patterns;

import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.uia.learner.relations.patterns.lexical.BigramSoftPatternModelBuilder;
import org.epnoi.uia.learner.relations.patterns.syntactic.SyntacticRelationalPatternsModelBuilder;

public class RelationalPatternsModelBuilderFactory {

	public static RelationalPatternsModelBuilder build(
			RelationalPatternsModelCreationParameters parameters)
			throws EpnoiResourceAccessException {

		RelationalPatternsModelBuilder relationalPatternsGenerator = null;
		String type = (String) parameters
				.getParameterValue(RelationalPatternsModelCreationParameters.TYPE);
		switch (type) {
		case PatternsConstants.LEXICAL:
			relationalPatternsGenerator = new BigramSoftPatternModelBuilder(
					parameters);
			break;
		case PatternsConstants.SYNTACTIC:
		
			relationalPatternsGenerator = new SyntacticRelationalPatternsModelBuilder(
					parameters);
			break;
		default:
			throw new EpnoiResourceAccessException(
					"Unknown RelationalPatternGenerator for type " + type);
		}
		return relationalPatternsGenerator;
	}
}
