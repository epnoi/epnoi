package org.epnoi.learner.relations.corpus.parallel;

import org.apache.spark.api.java.function.Function;
import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.model.RelationalSentence;

public class RelationalSentenceMapFunction
		implements Function<RelationalSentenceCandidate, RelationalSentence> {

	
	@Override
	public RelationalSentence call(RelationalSentenceCandidate currentRelationalSentenceCandidate)
			throws Exception {
		
		RelationalSentence relationalSentence = _createRelationalSentence(currentRelationalSentenceCandidate);

		
		return relationalSentence;
	}

	private RelationalSentence _createRelationalSentence(RelationalSentenceCandidate relationalSentenceCandidate) {
		

		Sentence sentence = relationalSentenceCandidate.getSentence();
		
		Long initialOffset = sentence.getAnnotation().getStartNode().getOffset();
		
		// Note that the offset is relative to the beginning of the
		// sentence
		OffsetRangeSelector source = new OffsetRangeSelector(
				relationalSentenceCandidate.getSource().getStartNode().getOffset() - initialOffset,
				relationalSentenceCandidate.getSource().getEndNode().getOffset() - initialOffset);
		// For each target term a relational sentence is created
		
		

			OffsetRangeSelector target = new OffsetRangeSelector(
					relationalSentenceCandidate.getTarget().getStartNode().getOffset() - initialOffset,
					relationalSentenceCandidate.getTarget().getStartNode().getOffset() - initialOffset);

			String annotatedSentence = _annotate(relationalSentenceCandidate.getSentence().getContent().toString());
				
			RelationalSentence relationalSentence = new RelationalSentence(source, target, sentence.toString(), annotatedSentence);
		return relationalSentence;
	}

	private String _annotate(String sentence) {
		return "";
	}
	// ----------------------------------------------------------------------------------------------------------------------

}
