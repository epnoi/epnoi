package org.epnoi.uia.learner.relations;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.DocumentContent;
import gate.Factory;
import gate.Utils;
import gate.creole.ResourceInstantiationException;
import gate.util.InvalidOffsetException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.Term;
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
import org.epnoi.uia.learner.relations.lexical.LexicalRelationalPattern;
import org.epnoi.uia.learner.relations.lexical.LexicalRelationalPatternGenerator;
import org.epnoi.uia.learner.relations.lexical.SoftPatternModel;
import org.epnoi.uia.learner.terms.TermsTable;

public class RelationsExtractor {

	private Core core;
	private SoftPatternModel softPatternModel;
	private Parameters parameters;
	private DomainsTable domainsTable;
	private TermsTable termsTable;
	private LexicalRelationalPatternGenerator patternsGenerator;
	private RelationsTable relationsTable;
	private double hypernymExtractionThreshold;
	private String targetDomain;

	// ------------------------------------------------------------------------------------------------------------------------------------

	public void init(Core core, DomainsTable domainsTable, Parameters parameters)
			throws EpnoiInitializationException {
		this.core = core;
		this.parameters = parameters;
		String hypernymModelPath = (String) parameters
				.getParameterValue(OntologyLearningParameters.HYPERNYM_MODEL_PATH);
		this.targetDomain = (String) parameters
				.getParameterValue(OntologyLearningParameters.TARGET_DOMAIN);
		this.patternsGenerator = new LexicalRelationalPatternGenerator();
		this.domainsTable = domainsTable;
		this.relationsTable = new RelationsTable();
		try {
			BigramSoftPatternModelSerializer.deserialize(hypernymModelPath);
		} catch (EpnoiResourceAccessException e) {
			throw new EpnoiInitializationException(e.getMessage());
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------

	public RelationsTable extract(TermsTable termsTable) {
		this.termsTable = termsTable;
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

			Long sentenceStartOffset = sentenceAnnotation.getStartNode()
					.getOffset();
			Long sentenceEndOffset = sentenceAnnotation.getEndNode()
					.getOffset();

			_testSentence(sentenceStartOffset, sentenceEndOffset,
					annotatedResource);
			/*
			 * _testSentence(sentenceStartOffset, sentenceContent,
			 * annotatedResourceAnnotations.getContained( sentenceStartOffset,
			 * sentenceEndOffset));
			 */

		}

	}

	// ------------------------------------------------------------------------------------------------------------------------------------

	private void _testSentence(Long sentenceStartOffset,
			Long sentenceEndOffset, Document annotatedResource) {

		AnnotationSet senteceAnnotationSet = annotatedResource.getAnnotations()
				.get(sentenceStartOffset, sentenceEndOffset);
		List<Annotation> termAnnotations = new ArrayList<Annotation>();
		for (Annotation termAnnotation : senteceAnnotationSet
				.get(NLPAnnotationsHelper.TERM_CANDIDATE)) {
			termAnnotations.add(termAnnotation);
		}

		String sentenceContent = null;
		try {
			sentenceContent = annotatedResource.getContent()
					.getContent(sentenceStartOffset, sentenceEndOffset)
					.toString();
		} catch (InvalidOffsetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < termAnnotations.size(); i++)
			for (int j = i + 1; j < termAnnotations.size(); j++) {
				Annotation source = termAnnotations.get(i);
				Annotation target = termAnnotations.get(j);
				//For each pair of terms we check both as target and as source
				_extractProbableRelationsFromSentence(source, target,
						annotatedResource, sentenceContent);
				_extractProbableRelationsFromSentence(target, source,
						annotatedResource, sentenceContent);
			}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------

	private void _extractProbableRelationsFromSentence(Annotation source,
			Annotation target, Document annotatedResource,
			String sentenceContent) {
		List<LexicalRelationalPattern> generatedPatterns = this.patternsGenerator
				.generate(source, target, annotatedResource);
		for (LexicalRelationalPattern pattern : generatedPatterns) {
			double relationProbability = this.softPatternModel
					.calculatePatternProbability(pattern);
			if (relationProbability > this.hypernymExtractionThreshold) {
				Relation relation = new Relation();

				String sourceToken = (String) source.getFeatures()
						.get("string");
				Term sourceTerm = this.termsTable.getTerm(Term.buildURI(
						sourceToken, this.targetDomain));

				String targetToken = (String) target.getFeatures()
						.get("string");
				Term targetTerm = this.termsTable.getTerm(Term.buildURI(
						targetToken, this.targetDomain));

				relation.setSource(sourceTerm);
				relation.setTarget(targetTerm);

				relation.setProvenanceSentence(sentenceContent);
				relation.setRelationhood(relationProbability);

				this.relationsTable.addRelation(relation);
			}
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------

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

	// ------------------------------------------------------------------------------------------------------------------------------------
}
