package org.epnoi.learner.relations.corpus.parallel;

import org.apache.spark.api.java.function.Function;
import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.model.RelationalSentence;
import org.epnoi.uia.commons.GateUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import gate.Document;

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
		
		ClientConfig config = new DefaultClientConfig();

		Client client = Client.create(config);
		String basePath = "/uia/nlp/process";

		WebResource service = client.resource("http://localhost:8080/epnoi/rest");

		// http://en.wikipedia.org/wiki/Autism/first/object/gate

		String content = service.path(basePath).queryParam("content", sentence)
				.type(javax.ws.rs.core.MediaType.APPLICATION_XML).get(String.class);
//System.out.println("-----> "+content);
		return content;
	}
	// ----------------------------------------------------------------------------------------------------------------------

}
