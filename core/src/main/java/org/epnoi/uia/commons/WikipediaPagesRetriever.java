package org.epnoi.uia.commons;

import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.InformationStore;
import org.epnoi.model.modules.InformationStoreHelper;
import org.epnoi.model.parameterization.VirtuosoInformationStoreParameters;
import org.epnoi.model.rdf.RDFHelper;

import java.util.List;
import java.util.logging.Logger;

public class WikipediaPagesRetriever {
	private static final Logger logger = Logger
			.getLogger(WikipediaPagesRetriever.class.getName());
	public static List<String> getWikipediaArticles(Core core) {
		logger.info("Retrieving the URIs of the Wikipedia articles ");

		InformationStore informationStore = core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String queryExpression = "SELECT DISTINCT  ?uri FROM <{GRAPH}>"
				+ " { ?uri a <{WIKIPEDIA_PAPER_CLASS}> " + "}";

		queryExpression = queryExpression.replace(
				"{GRAPH}",
				((VirtuosoInformationStoreParameters) informationStore
						.getParameters()).getGraph()).replace(
				"{WIKIPEDIA_PAPER_CLASS}", RDFHelper.WIKIPEDIA_PAGE_CLASS);

		List<String> queryResults = informationStore.query(queryExpression);

		
		return queryResults;
		//return Arrays.asList("http://en.wikipedia.org/wiki/Autism");
	}

}
