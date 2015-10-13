package org.epnoi.learner.relations.corpus.parallel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.Selector;
import org.epnoi.model.WikipediaPage;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.SelectorHelper;

import gate.Document;
import gate.corpora.DocumentImpl;
import scala.util.parsing.ast.Binders.ReturnAndDo;

public class DocumentRetrievalFlatMapFunction implements FlatMapFunction<String, Document> {

	@Override
	public Iterable<Document> call(String URI) throws Exception {
		List<Document> sectionsAnnotatedContent = new ArrayList<>();

		Document annotatedContent = _obtainAnnotatedContent(URI);

		if (annotatedContent != null) {
			sectionsAnnotatedContent.add(annotatedContent);
		}
		return sectionsAnnotatedContent;
	}

	// --------------------------------------------------------------------------------------------------------------------

	private Document _obtainAnnotatedContent(String URI) {

		//Selector selector = new Selector();
		//selector.setProperty(SelectorHelper.TYPE, RDFHelper.WIKIPEDIA_PAGE_CLASS);

		//selector.setProperty(SelectorHelper.URI, uri);

		// Content<Object> content =
		// core.getInformationHandler().getAnnotatedContent(selector);

		// Document sectionAnnotatedContent = (Document) content.getContent();
		Document annotatedDocument = new DocumentImpl();

		return annotatedDocument;
	}

	// --------------------------------------------------------------------------------------------------------------------

}
