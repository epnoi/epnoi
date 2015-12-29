package org.epnoi.learner.relations;

import gate.util.compilers.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.epnoi.model.Context;
import org.epnoi.model.Domain;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;

import java.util.logging.Logger;

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
		String URI = domain.getUri()+"/relations";
		return (RelationsTable) this.core.getInformationHandler().get(URI,
				RDFHelper.RELATIONS_TABLE_CLASS);
	}

	public RelationsTable retrieve(String domainUri) {
		String uri = domainUri+"/relations";

		System.out.println("Retrieving "+ uri);

		RelationsTable relationsTable = 	 (RelationsTable) this.core.getInformationHandler().get(uri,
				RDFHelper.RELATIONS_TABLE_CLASS);
		System.out.println("The relation table > "+relationsTable);
		return relationsTable;
	}

	public void store(RelationsTable relationsTable){
		System.out.println("ESTORING > "+relationsTable);
			this.core.getInformationHandler().put(relationsTable, Context.getEmptyContext());

		}


}
