package org.epnoi.learner.relations.patterns;

import org.epnoi.learner.relations.patterns.lexical.LexicalRelationalPatternGenerator;
import org.epnoi.learner.relations.patterns.syntactic.SyntacticRelationalPatternGenerator;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;

public class RelationalPatternsGeneratorFactory {
	
	public static RelationalPatternGenerator build(
			RelationalPatternsModelCreationParameters parameters)
			throws EpnoiResourceAccessException {

		RelationalPatternGenerator relationalPatternsGenerator = null;
		String type = (String) parameters
				.getParameterValue(RelationalPatternsModelCreationParameters.TYPE);
		switch (type) {
		case PatternsConstants.LEXICAL:
			relationalPatternsGenerator = new LexicalRelationalPatternGenerator();
			break;
		case PatternsConstants.SYNTACTIC:
			relationalPatternsGenerator = new SyntacticRelationalPatternGenerator();
			break;
		default:
			throw new EpnoiResourceAccessException(
					"Unknown RelationalPatternGenerator for type " + type);
		}
		return relationalPatternsGenerator;
	}
}
