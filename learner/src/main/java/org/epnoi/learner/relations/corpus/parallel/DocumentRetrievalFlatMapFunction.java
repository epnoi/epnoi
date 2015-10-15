package org.epnoi.learner.relations.corpus.parallel;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.commons.GateUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import gate.Document;

public class DocumentRetrievalFlatMapFunction implements FlatMapFunction<String, Document> {

	@Override
	public Iterable<Document> call(String uri) throws Exception {
		List<Document> sectionsAnnotatedContent = new ArrayList<>();

		Document annotatedContent = _obtainAnnotatedContent(uri);

		if (annotatedContent != null) {
			sectionsAnnotatedContent.add(annotatedContent);
		}
		return sectionsAnnotatedContent;
	}

	// --------------------------------------------------------------------------------------------------------------------

	private Document _obtainAnnotatedContent(String uri) {

		ClientConfig config = new DefaultClientConfig();

		Client client = Client.create(config);
		String knowledgeBasePath = "/uia/annotatedcontent";
		Document document = null;
		try {
			WebResource service = client.resource("http://localhost:8080/epnoi/rest");

			String content = service.path(knowledgeBasePath).queryParam("uri", uri)
					.queryParam("type", RDFHelper.WIKIPEDIA_PAGE_CLASS).type(javax.ws.rs.core.MediaType.APPLICATION_XML)
					.get(String.class);

			document = GateUtils.deserializeGATEDocument(content);
		} catch (Exception e) {
			e.printStackTrace();
			
			
		}
		
		//System.out.println("___> "+document);
		return document;
	}

	// --------------------------------------------------------------------------------------------------------------------

}
