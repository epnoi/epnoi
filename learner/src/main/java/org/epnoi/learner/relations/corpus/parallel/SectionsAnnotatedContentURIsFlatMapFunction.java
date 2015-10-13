package org.epnoi.learner.relations.corpus.parallel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.Domain;
import org.epnoi.model.Selector;
import org.epnoi.model.WikipediaPage;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.SelectorHelper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import gate.Document;

public class SectionsAnnotatedContentURIsFlatMapFunction implements FlatMapFunction<String, String> {

	@Override
	public Iterable<String> call(String uri) throws Exception {
		List<String> sectionsAnnotatedContentURIs = new ArrayList<>();
		try {

			WikipediaPage page = _retrieveWikipediaPage(uri);
			if (page != null) {
				sectionsAnnotatedContentURIs = _obtainSectionsAnnotatedContentURIs(page);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sectionsAnnotatedContentURIs;
	}

	// --------------------------------------------------------------------------------------------------------------------

	private List<String> _obtainSectionsAnnotatedContentURIs(WikipediaPage page) {
		List<String> sectionUris = new ArrayList<String>();
		for (String section : page.getSections()) {
			sectionUris.add(_extractURI(page.getUri(), section, AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE));
		}
		return sectionUris;
	}

	// --------------------------------------------------------------------------------------------------------------------

	private String _extractURI(String URI, String section, String annotationType) {

		String cleanedSection = section.replaceAll("\\s+$", "").replaceAll("\\s+", "_");

		return URI + "/" + cleanedSection + "/" + annotationType;
	}

	// --------------------------------------------------------------------------------------------------------------------

	private WikipediaPage _retrieveWikipediaPage(String wikipediaPageURI) {
		String domainsPath = "/uia/resources/bytype/wikipediapages/resource";

		ClientConfig config = new DefaultClientConfig();

		Client client = Client.create(config);

		WebResource service = client.resource("http://localhost:8080/epnoi/rest");

		WikipediaPage retrievedDomain = service.path(domainsPath).queryParam("uri", wikipediaPageURI)
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(WikipediaPage.class);
		return retrievedDomain;
	}

}
