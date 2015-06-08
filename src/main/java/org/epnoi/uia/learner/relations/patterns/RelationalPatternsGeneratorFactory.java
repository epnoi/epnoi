package org.epnoi.uia.learner.relations.patterns;

import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.uia.learner.relations.patterns.lexical.LexicalRelationalPatternGenerator;
import org.epnoi.uia.learner.relations.patterns.syntactic.SyntacticRelationalPatternGenerator;

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
