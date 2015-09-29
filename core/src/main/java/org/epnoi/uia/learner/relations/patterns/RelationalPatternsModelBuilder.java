package org.epnoi.uia.learner.relations.patterns;

public interface RelationalPatternsModelBuilder {

	public void addPattern(RelationalPattern relationalPattern);

	public void addNegativePattern(RelationalPattern relationalPattern);

	public RelationalPatternsModel build();

}
