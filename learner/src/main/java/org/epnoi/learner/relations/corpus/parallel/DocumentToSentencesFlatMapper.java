package org.epnoi.learner.relations.corpus.parallel;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.DocumentContent;
import gate.annotation.AnnotationSetImpl;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.epnoi.nlp.gate.NLPAnnotationsConstants;
import org.epnoi.uia.commons.GateUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DocumentToSentencesFlatMapper implements FlatMapFunction<Document, Sentence> {

	@Override
	public Iterable<Sentence> call(Document currentDocument) throws Exception {
		List<Sentence> currentDocumentSentences = new ArrayList<>();
		DocumentContent sentenceContent;

		AnnotationSet sentencesAnnotations = currentDocument.getAnnotations().get(NLPAnnotationsConstants.SENTENCE);
		Iterator<Annotation> sentencesIt = sentencesAnnotations.iterator();
		while (sentencesIt.hasNext()) {
			Annotation sentenceAnnotation = sentencesIt.next();

			Long sentenceStartOffset = sentenceAnnotation.getStartNode().getOffset();
			Long sentenceEndOffset = sentenceAnnotation.getEndNode().getOffset();
			AnnotationSet containedAnnotations = new AnnotationSetImpl(currentDocument.getAnnotations().get(sentenceStartOffset, sentenceEndOffset));

			sentenceContent = GateUtils.extractAnnotationContent(sentenceAnnotation, currentDocument);
			Sentence sentence = new Sentence(sentenceContent,sentenceAnnotation,containedAnnotations);
			currentDocumentSentences.add(sentence);

		}
		return currentDocumentSentences;
	}

}
