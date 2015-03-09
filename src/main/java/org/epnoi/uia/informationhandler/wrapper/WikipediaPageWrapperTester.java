package org.epnoi.uia.informationhandler.wrapper;

import gate.Document;
import gate.Factory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.WikipediaPage;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.harvester.wikipedia.WikipediaHarvester;
import org.epnoi.uia.informationstore.CassandraInformationStore;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.MapInformationStore;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.cassandra.WikipediaPageCassandraDAO;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.nlp.TermCandidatesFinder;
import org.epnoi.uia.parameterization.MapInformationStoreParameters;

public class WikipediaPageWrapperTester {
	Core core;
	TermCandidatesFinder termCandidatesFinder;
	WikipediaPage wikipediaPage = _createWikipediaPage();

	// -------------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out.println("WikipediaPage Cassandra Test--------------");
		System.out
				.println("Initialization --------------------------------------------");

		System.out.println(" --------------------------------------------");
		WikipediaPageWrapperTester wikipediaPageWrapperTester = new WikipediaPageWrapperTester();

		wikipediaPageWrapperTester.process();
		wikipediaPageWrapperTester.read();
	}

	private void read() {

		for (int i = 0; i < this.wikipediaPage.getSections().size(); i++) {

			String annotatedContentURI = _extractURI(wikipediaPage.getURI(),
					wikipediaPage.getSections().get(i),
					AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE);

			Selector selector = new Selector();
			selector.setProperty(SelectorHelper.URI, wikipediaPage.getURI());
			selector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI,
					annotatedContentURI);
			selector.setProperty(SelectorHelper.TYPE,
					RDFHelper.WIKIPEDIA_PAGE_CLASS);
			System.out.println("the annotated content ["
					+ this.wikipediaPage.getSections().get(i)
					+ "]> "
					+ this.core.getInformationHandler().getAnnotatedContent(
							selector));
		}
	}

	public void process() {
		this.wikipediaPage = _createWikipediaPage();
		_introduceAnnotatedContent(wikipediaPage, Context.getEmptyContext());
	}

	public WikipediaPageWrapperTester() {
		this.core = CoreUtility.getUIACore();
		this.termCandidatesFinder = new TermCandidatesFinder();
		this.termCandidatesFinder.init();

	}

	private static WikipediaPage _createWikipediaPage() {
		WikipediaPage wikipediaPage = new WikipediaPage();
		wikipediaPage.setURI("http://externalresourceuri");
		wikipediaPage.setTerm("Proof Term");
		wikipediaPage.setTermDefinition("Proof Term is whatever bla bla bla");
		wikipediaPage.setSections(Arrays.asList("first", "middle section",
				"references"));
		wikipediaPage.setSectionsContent(new HashMap<String, String>());
		wikipediaPage.getSectionsContent().put("first",
				"This is the content of the first section");
		wikipediaPage.getSectionsContent().put("middle section",
				"This is the content of the middle section");
		wikipediaPage.getSectionsContent().put("references",
				"This is the content for the references");
		return wikipediaPage;
	}

	// -------------------------------------------------------------------------------------------------------------------

	private String _extractURI(String URI, String section, String annotationType) {

		String cleanedSection = section.replaceAll("\\s+$", "").replaceAll(
				"\\s+", "_");

		return URI + "/" + cleanedSection + "/" + annotationType;
	}

	// -------------------------------------------------------------------------------------------------------------------

	public void _introduceAnnotatedContent(WikipediaPage wikipediaPage,
			Context context) {
		String serializedAnnotatedContent = null;
		List<String> sections = wikipediaPage.getSections();
		// System.out.println("S> " + sections + " " +
		// wikipediaPage.getURI());
		for (int i = sections.size() - 1; i >= 0; i--) {

			String sectionContent = wikipediaPage.getSectionsContent().get(
					sections.get(i));
			/*
			 * System.out.println(" " + sections.get(i) + "  " +
			 * wikipediaPage.getSectionsContent().get( sections.get(i)));
			 */

			String annotatedContentURI = _extractURI(wikipediaPage.getURI(),
					sections.get(i),
					AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE);
			Document annotatedContent = this.termCandidatesFinder
					.findTermCandidates(sectionContent);
			serializedAnnotatedContent = annotatedContent.toXml();

			// Once it has been serialized, we must free the associated GATE
			// resources
			Factory.deleteResource(annotatedContent);

			Selector selector = new Selector();
			selector.setProperty(SelectorHelper.URI, wikipediaPage.getURI());
			selector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI,
					annotatedContentURI);
			selector.setProperty(SelectorHelper.TYPE,
					RDFHelper.WIKIPEDIA_PAGE_CLASS);

			core.getInformationHandler().setAnnotatedContent(
					selector,
					new Content<>(serializedAnnotatedContent,
							AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE));
		}

	}

}