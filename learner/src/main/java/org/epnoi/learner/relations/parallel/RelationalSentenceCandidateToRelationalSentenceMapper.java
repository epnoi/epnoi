package org.epnoi.learner.relations.parallel;

import gate.Document;
import gate.corpora.DocumentContentImpl;
import gate.util.InvalidOffsetException;
import org.apache.spark.api.java.function.Function;
import org.epnoi.learner.relations.corpus.parallel.RelationalSentenceCandidate;
import org.epnoi.learner.relations.corpus.parallel.Sentence;
import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.model.RelationalSentence;


public class RelationalSentenceCandidateToRelationalSentenceMapper implements Function<RelationalSentenceCandidate, RelationalSentence> {
    @Override
    public RelationalSentence call(RelationalSentenceCandidate currentRelationalSentenceCandidate) throws Exception {
        Long sourceStartOffset = currentRelationalSentenceCandidate.getSource().getStartNode().getOffset();
        Long sourceEndOffset = currentRelationalSentenceCandidate.getSource().getEndNode().getOffset();
        Long targetStartOffset = currentRelationalSentenceCandidate.getTarget().getStartNode().getOffset();
        Long targetEndOffset = currentRelationalSentenceCandidate.getTarget().getEndNode().getOffset();

        Document relationDocument = currentRelationalSentenceCandidate.getSentence().getContainedAnnotations().getDocument();
        _shrinkDocument(currentRelationalSentenceCandidate.getSentence(), relationDocument);

        RelationalSentence relationalSentence = new RelationalSentence(new OffsetRangeSelector(sourceStartOffset, sourceEndOffset), new OffsetRangeSelector(targetStartOffset, targetEndOffset), relationDocument.getContent().toString(), relationDocument.toXml());


        return relationalSentence;
    }

    private void _shrinkDocument(Sentence sentence, Document document) {
        Long startOffset = sentence.getAnnotation().getStartNode().getOffset();
        Long endOffset = sentence.getAnnotation().getEndNode().getOffset();

        try {
            //The content from de beginning of the document to the beginning of the sentece must be removed
            document.edit(0L, startOffset, new DocumentContentImpl(""));

            //We delete also the content from the end of the sentence to the end of the document
            //Note that the endOffset has to be updated since we have already deleted content
            document.edit(endOffset - startOffset, document.getAnnotations().lastNode().getOffset(), new DocumentContentImpl(""));
        } catch (InvalidOffsetException e) {
            e.printStackTrace();
        }
    }

}