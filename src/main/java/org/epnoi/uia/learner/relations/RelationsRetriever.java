package org.epnoi.uia.learner.relations;

import java.util.logging.Logger;

import org.epnoi.model.Domain;
import org.epnoi.model.RelationsTable;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;

public class RelationsRetriever {
	private static final Logger logger = Logger
			.getLogger(RelationsRetriever.class.getName());

	private Core core;

	// ------------------------------------------------------------------------------------------------------------

	public RelationsRetriever(Core core) {
		this.core = core;
	}

	// ------------------------------------------------------------------------------------------------------------

	public RelationsTable retrieve(Domain domain) {
		String URI = "";
		return (RelationsTable) this.core.getInformationHandler().get(URI,
				RDFHelper.RELATIONS_TABLE_CLASS);
	}
}
