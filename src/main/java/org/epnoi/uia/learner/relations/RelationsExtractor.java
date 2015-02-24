package org.epnoi.uia.learner.relations;

import java.util.Iterator;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.DocumentContent;
import gate.Factory;
import gate.Utils;
import gate.creole.ResourceInstantiationException;
import gate.util.InvalidOffsetException;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.uia.commons.Parameters;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.exceptions.EpnoiInitializationException;
import org.epnoi.uia.exceptions.EpnoiResourceAccessException;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.DomainsTable;
import org.epnoi.uia.learner.OntologyLearningParameters;
import org.epnoi.uia.learner.nlp.gate.NLPAnnotationsHelper;
import org.epnoi.uia.learner.relations.lexical.BigramSoftPatternModelSerializer;
import org.epnoi.uia.learner.relations.lexical.SoftPatternModel;
import org.epnoi.uia.learner.terms.AnnotatedWord;
import org.epnoi.uia.learner.terms.TermMetadata;

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

	public RelationsTable extract() {
		RelationsTable relationsTable = new RelationsTable();
		// The relations finding task is only performed in the target domain,
		// these are the resources that we should consider
		for (String domainResourceURI : domainsTable.getDomains().get(
				domainsTable.getTargetDomain())) {
			_findRelationsInResource(domainResourceURI);
		}
		return relationsTable;
	}

	// -----------------------------------------------------------------------------------

	private void _findRelationsInResource(String domainResourceURI) {
		Document annotatedResource = retrieveAnnotatedDocument(domainResourceURI);
		AnnotationSet sentenceAnnotations = annotatedResource.getAnnotations()
				.get("Sentence");
		DocumentContent sentenceContent = null;
		AnnotationSet resourceAnnotations = annotatedResource.getAnnotations();
		Iterator<Annotation> sentencesIt = sentenceAnnotations.iterator();
		while (sentencesIt.hasNext()) {
			Annotation sentenceAnnotation = sentencesIt.next();

			try {
				Long sentenceStartOffset = sentenceAnnotation.getStartNode()
						.getOffset();
				Long sentenceEndOffset = sentenceAnnotation.getEndNode()
						.getOffset();

				sentenceContent = annotatedResource.getContent().getContent(
						sentenceStartOffset, sentenceEndOffset);
				AnnotationSet senteceAnnotationSet = annotatedResource
						.getAnnotations().get(sentenceStartOffset,
								sentenceEndOffset);

				for (Annotation termAnnotation : senteceAnnotationSet
						.get(NLPAnnotationsHelper.TERM_CANDIDATE)) {
					System.out.println("TC> " + termAnnotation);
				}
				/*
				 * _testSentence(sentenceStartOffset, sentenceContent,
				 * annotatedResourceAnnotations.getContained(
				 * sentenceStartOffset, sentenceEndOffset));
				 */
			} catch (InvalidOffsetException e) {

				e.printStackTrace();
			}

		}

	}

	private Document retrieveAnnotatedDocument(String URI) {

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.URI, URI);
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.PAPER_CLASS);
		selector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI, URI + "/"
				+ AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE);

		Content<String> annotatedContent = core.getInformationHandler()
				.getAnnotatedContent(selector);
		Document document = null;
		try {
			document = (Document) Factory
					.createResource(
							"gate.corpora.DocumentImpl",
							Utils.featureMap(
									gate.Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME,
									annotatedContent.getContent(),
									gate.Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME,
									"text/xml"));

		} catch (ResourceInstantiationException e) { // TODO Auto-generated
			System.out
					.println("Couldn't retrieve the GATE document that represents the annotated content of "
							+ URI);
			e.printStackTrace();
		}
		return document;
	}

}
