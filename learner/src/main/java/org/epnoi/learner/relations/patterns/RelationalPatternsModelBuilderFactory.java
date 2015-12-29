package org.epnoi.learner.relations.patterns;

import org.epnoi.learner.relations.patterns.lexical.BigramSoftPatternModelBuilder;
import org.epnoi.learner.relations.patterns.lexical.RelaxedBigramSoftPatternModelBuilder;
import org.epnoi.learner.relations.patterns.syntactic.SyntacticRelationalPatternsModelBuilder;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;

public class RelationalPatternsModelBuilderFactory {

	public static RelationalPatternsModelBuilder build(
			RelationalPatternsModelCreationParameters parameters)
			throws EpnoiResourceAccessException {

		RelationalPatternsModelBuilder relationalPatternsGenerator = null;
		String type = (String) parameters
				.getParameterValue(RelationalPatternsModelCreationParameters.TYPE);
		switch (type) {
		case PatternsConstants.LEXICAL:
			relationalPatternsGenerator = new RelaxedBigramSoftPatternModelBuilder(
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
