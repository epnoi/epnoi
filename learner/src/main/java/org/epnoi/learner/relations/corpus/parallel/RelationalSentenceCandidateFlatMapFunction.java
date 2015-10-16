package org.epnoi.learner.relations.corpus.parallel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.model.RelationalSentence;
import org.epnoi.nlp.gate.NLPAnnotationsConstants;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import gate.Annotation;
import gate.Document;
import gate.DocumentContent;

public class RelationalSentenceCandidateFlatMapFunction
		implements FlatMapFunction<Sentence, RelationalSentenceCandidate> {

	private final int MIN_TERM_LENGTH = 2;

	@Override
	public Iterable<RelationalSentenceCandidate> call(Sentence currentSentence) throws Exception {
		List<RelationalSentenceCandidate> relationalSentencesCandidate = new ArrayList<>();
		//System.out.println(currentSentence);
		_testSentence(currentSentence);
		return relationalSentencesCandidate;
	}

	private void _testSentence(Sentence sentence) {
		Long sentenceStartOffset = sentence.getAnnotation().getStartNode().getOffset();
		Long sentenceEndOffset = sentence.getAnnotation().getEndNode().getOffset();

		Set<String> sentenceTerms = new HashSet<String>();
		// This table stores the string representation of each sentence terms
		// and their corresponding annotation
		Map<String, Annotation> termsAnnotationsTable = _initTermsAnnotationsTable(sentence, sentenceTerms);

		Iterator<String> termsIt = sentenceTerms.iterator();
		boolean found = false;
		while (termsIt.hasNext() && !found) {
			String term = termsIt.next();
			if (term != null && term.length() > 0) {
				// For each term we retrieve its well-known hypernyms

				Set<String> termHypernyms = _retrieveHypernyms(term);
				termHypernyms.retainAll(sentenceTerms);
				// termHypernyms.removeAll(this.knowledgeBase.stem(term));

				// If the intersection of the well-known hypernyms and the terms
				// that belong to the sentence, this is a relational sentence
				if (termHypernyms.size() > 0) {

					System.out.println("FOUND SENTENCE BETWEEN " + sentenceStartOffset + "," + sentenceEndOffset
							+ " when testing for the term " + sentenceTerms);
					/*
					 * ESTAS AQUI CREANDO
					 * _createRelationalSentence(sentenceContent,
					 * sentenceStartOffset, termsAnnotationsTable, term,
					 * termHypernyms);
					 */
					found = true;

				}
			}

		}

	}

	// ----------------------------------------------------------------------------------------------------------------------

	private Set<String> _retrieveHypernyms(String term) {
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(config);
		String knowledgeBasePath = "/uia/knowledgebase";

		WebResource service = client.resource("http://localhost:8080/epnoi/rest");

		Set<String> hypernyms = service.path(knowledgeBasePath + "/relations/hypernymy/targets")
				.queryParam("source", term).type(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(Set.class);
		return hypernyms;
	}
	
	// ----------------------------------------------------------------------------------------------------------------------

	private Set<String> _stem(String term) {
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(config);
		String knowledgeBasePath = "/uia/knowledgebase";

		WebResource service = client.resource("http://localhost:8080/epnoi/rest");

		Set<String> hypernyms = service.path(knowledgeBasePath + "/relations/hypernymy/targets")
				.queryParam("source", term).type(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(Set.class);
		return hypernyms;
	}

	// ----------------------------------------------------------------------------------------------------------------------

	private Map<String, Annotation> _initTermsAnnotationsTable(Sentence sentence, Set<String> sentenceTerms) {
		HashMap<String, Annotation> termsAnnotationsTable = new HashMap<String, Annotation>();
		for (Annotation termAnnotation : sentence.getContainedAnnotations()
				.get(NLPAnnotationsConstants.TERM_CANDIDATE)) {
			Long startOffset = termAnnotation.getStartNode().getOffset()
					- sentence.getAnnotation().getStartNode().getOffset();
			Long endOffset = termAnnotation.getEndNode().getOffset()
					- sentence.getAnnotation().getStartNode().getOffset();

			String term = "";
			try {
				// First of all we retrieve the surface form of the term

				term = sentence.getContent().getContent(startOffset, endOffset).toString();

			} catch (Exception e) {
				e.printStackTrace();
				term = "";

			}

			// We stem the surface form (we left open the possibility of
			// different stemming results so we consider a set of stemmed
			// forms)
			_addTermToTermsTable(term, termAnnotation, sentenceTerms, termsAnnotationsTable);
		}
		return termsAnnotationsTable;
	}

	// ----------------------------------------------------------------------------------------------------------------------

	private void _addTermToTermsTable(String term, Annotation termAnnotation, Set<String> sentenceTerms,
			HashMap<String, Annotation> termsAnnotationsTable) {

		if (term.length() > MIN_TERM_LENGTH) {
			for (String stemmedTerm : _stem(term)) {

				termsAnnotationsTable.put(stemmedTerm, termAnnotation);
				sentenceTerms.add(stemmedTerm);

			}

			termsAnnotationsTable.put(term, termAnnotation);
			sentenceTerms.add(term);
		}

	}

	// ----------------------------------------------------------------------------------------------------------------------

}
