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

import gate.Annotation;
import gate.Document;
import gate.DocumentContent;

public class RelationalSentenceFlatMapFunction implements FlatMapFunction<RelationalSentenceCandidate, RelationalSentence> {

	private final int MIN_TERM_LENGTH = 2;

	@Override
	public Iterable<RelationalSentence> call(RelationalSentenceCandidate currentRelationalSentenceCandidate) throws Exception {
		List<RelationalSentence> relationalSentences = new ArrayList<>();
		return relationalSentences;
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
				Set<String> termHypernyms = new HashSet<>();//this.knowledgeBase.getHypernyms(term);
				termHypernyms.retainAll(sentenceTerms);
				// termHypernyms.removeAll(this.knowledgeBase.stem(term));

				// If the intersection of the well-known hypernyms and the terms
				// that belong to the sentence, this is a relational sentence
				if (termHypernyms.size() > 0) {
					/*
					 * System.out.println("FOUND SENTENCE BETWEEN " +
					 * sentenceStartOffset + "," + sentenceEndOffset +
					 * " when testing for the term " + sentenceTerms);
					 */
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

	private void _createRelationalSentence(DocumentContent sentenceContent, Long sentenceStartOffset,
			Map<String, Annotation> termsAnnotationsTable, String term, Set<String> termHypernyms) {
		Annotation sourceTermAnnotation = termsAnnotationsTable.get(term);

		// Note that the offset is relative to the beginning of the
		// sentence
		OffsetRangeSelector source = new OffsetRangeSelector(
				sourceTermAnnotation.getStartNode().getOffset() - sentenceStartOffset,
				sourceTermAnnotation.getEndNode().getOffset() - sentenceStartOffset);
		// For each target term a relational sentence is created
		for (String destinationTerm : termHypernyms) {

			Annotation destinationTermAnnotation = termsAnnotationsTable.get(destinationTerm);

			// Note that the offset is relative to the beginning of
			// the
			// sentence
			OffsetRangeSelector target = new OffsetRangeSelector(
					destinationTermAnnotation.getStartNode().getOffset() - sentenceStartOffset,
					destinationTermAnnotation.getEndNode().getOffset() - sentenceStartOffset);

			Document annotatedContent = null;
			/*
			 * try { annotatedContent =
			 * core.getNLPHandler().process(sentenceContent.toString()); } catch
			 * (EpnoiResourceAccessException e) { // TODO Auto-generated catch
			 * block e.printStackTrace(); }
			 */
			RelationalSentence relationalSentence = new RelationalSentence(source, target, sentenceContent.toString(),
					annotatedContent.toXml());
			annotatedContent.cleanup();
			// annotatedContent=null;

			if (!target.equals(source)) {

		//		corpus.getSentences().add(relationalSentence);
			}

		}
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
			;

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
			_addTermToTermsTable(sentenceTerms, termsAnnotationsTable, termAnnotation, term);
		}
		return termsAnnotationsTable;
	}

	// ----------------------------------------------------------------------------------------------------------------------

	private void _addTermToTermsTable(Set<String> sentenceTerms, HashMap<String, Annotation> termsAnnotationsTable,
			Annotation termAnnotation, String term) {
	/*
		if (term.length() > MIN_TERM_LENGTH) {
			for (String stemmedTerm : this.knowledgeBase.stem(term)) {

				termsAnnotationsTable.put(stemmedTerm, termAnnotation);
				sentenceTerms.add(stemmedTerm);

			}

			termsAnnotationsTable.put(term, termAnnotation);
			sentenceTerms.add(term);
		}
*/
	}

	// ----------------------------------------------------------------------------------------------------------------------

}
