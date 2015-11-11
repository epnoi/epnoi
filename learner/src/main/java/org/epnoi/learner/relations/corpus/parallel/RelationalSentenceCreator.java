package org.epnoi.learner.relations.corpus.parallel;

public class RelationalSentenceCreator {
	/*
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
			try {
				annotatedContent = core.getNLPHandler().process(sentenceContent.toString());
			} catch (EpnoiResourceAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			RelationalSentence relationalSentence = new RelationalSentence(source, target, sentenceContent.toString(),
					annotatedContent.toXml());
			annotatedContent.cleanup();
			// annotatedContent=null;

			if (!target.equals(source)) {

				corpus.getSentences().add(relationalSentence);
			}

		}
	}
	*/
}
