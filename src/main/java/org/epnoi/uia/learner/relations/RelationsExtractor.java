package org.epnoi.uia.learner.relations;

import org.epnoi.uia.commons.Parameters;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.exceptions.EpnoiInitializationException;
import org.epnoi.uia.exceptions.EpnoiResourceAccessException;
import org.epnoi.uia.learner.DomainsTable;
import org.epnoi.uia.learner.OntologyLearningParameters;
import org.epnoi.uia.learner.relations.lexical.BigramSoftPatternModelSerializer;
import org.epnoi.uia.learner.relations.lexical.SoftPatternModel;

public class RelationsExtractor {

	private Core core;
	private SoftPatternModel softPatternModel;
	private Parameters parameters;
	private DomainsTable domainsTable;

	// ------------------------------------------------------------------------------------------------------------------------------------

	public void init(Core core, DomainsTable domainsTable, Parameters parameters)
			throws EpnoiInitializationException {
		this.core = core;
		this.parameters = parameters;
		String hypernymModelPath = (String) parameters
				.getParameterValue(OntologyLearningParameters.HYPERNYM_MODEL_PATH);
		try {
			BigramSoftPatternModelSerializer.deserialize(hypernymModelPath);
		} catch (EpnoiResourceAccessException e) {
			throw new EpnoiInitializationException(e.getMessage());
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------

	public static RelationsTable extract() {
		RelationsTable relationsTable = new RelationsTable();
		//List<String> documentCorpus = this.retrieveCorpus()
		return relationsTable;
	}

}
