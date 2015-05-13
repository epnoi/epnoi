package org.epnoi.uia.learner.relations;

import java.util.logging.Logger;

import org.epnoi.model.RelationsTable;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.commons.Parameters;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;

public class RelationsRetriever {
	private static final Logger logger = Logger
			.getLogger(RelationsRetriever.class.getName());

	private Core core;
	private Parameters parameters;

	// ------------------------------------------------------------------------------------------------------------

	public void init(Core core, Parameters parameters)
			throws EpnoiInitializationException {
		this.core = core;
		this.parameters = parameters;
	}
	
	// ------------------------------------------------------------------------------------------------------------


	public RelationsTable retrieve() {
		String URI = "";
		return (RelationsTable) this.core.getInformationHandler().get(URI,
				RDFHelper.RELATIONS_TABLE_CLASS);
	}
}
