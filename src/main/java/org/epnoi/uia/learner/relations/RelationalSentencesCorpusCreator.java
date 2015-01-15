package org.epnoi.uia.learner.relations;

import java.util.List;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.WikipediaPage;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.exceptions.EpnoiInitializationException;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

public class RelationalSentencesCorpusCreator {
	private Core core;

	private RelationalSentencesCorpus corpus;

	// ----------------------------------------------------------------------------------------------------------------------

	public void init(Core core,
			RelationSentencesCorpusCreationParameters parameters)
			throws EpnoiInitializationException {
		this.core = core;
		this.corpus = new RelationalSentencesCorpus();
	}

	// ----------------------------------------------------------------------------------------------------------------------

	public RelationalSentencesCorpus createCorpus() {
		
		
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.WIKIPEDIA_PAGE_CLASS);
		String uri = "http://en.wikipedia.org/wiki/AccessibleComputing";
		//for (String uri : getWikipediaArticles()) {
			
			System.out.println("Retrieving " + uri);
			WikipediaPage wikipediaPage = (WikipediaPage) this.core
					.getInformationHandler().get(uri,
							RDFHelper.WIKIPEDIA_PAGE_CLASS);
			System.out.println(">" + wikipediaPage);
			
			selector.setProperty(SelectorHelper.URI, uri);
			selector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI, uri+"/first/"+AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE);
			Content<String> annotatedContent =  this.core
					.getInformationHandler().getAnnotatedContent(selector);
			
			System.out.println("----> "+annotatedContent);
		//}

		return this.corpus;

	}

	public List<String> getWikipediaArticles() {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String queryExpression = "SELECT DISTINCT  ?uri FROM <{GRAPH}>"
				+ " { ?uri a <{WIKIPEDIA_PAPER_CLASS}> " + "}";

		queryExpression = queryExpression.replace(
				"{GRAPH}",
				((VirtuosoInformationStoreParameters) informationStore
						.getParameters()).getGraph()).replace(
				"{WIKIPEDIA_PAPER_CLASS}", RDFHelper.WIKIPEDIA_PAGE_CLASS);

		System.out.println("----> QUERY EXPRESSION " + queryExpression);
		List<String> queryResults = informationStore.query(queryExpression);

		return queryResults;
	}

	// ----------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out.println("Starting the Relation Sentences Corpus Creator");

		RelationalSentencesCorpusCreator relationSentencesCorpusCreator = new RelationalSentencesCorpusCreator();

		Core core = CoreUtility.getUIACore();

		RelationSentencesCorpusCreationParameters parameters = new RelationSentencesCorpusCreationParameters();
		try {
			relationSentencesCorpusCreator.init(core, parameters);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}

		RelationalSentencesCorpus corpus = relationSentencesCorpusCreator
				.createCorpus();

		System.out.println("The result is " + corpus);

		System.out.println("Stopping the Relation Sentences Corpus Creator");
	}

}
