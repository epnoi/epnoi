package org.epnoi.uia.learner.relations;

import org.epnoi.model.OffsetRangeSelector;

public class RelationalSentence {
	OffsetRangeSelector source;
	OffsetRangeSelector target;
	String sentence;
	String type;

	public RelationalSentence(OffsetRangeSelector source,
			OffsetRangeSelector target, String sentence, String type) {
		super();
		this.source = source;
		this.target = target;
		this.sentence = sentence;
		this.type = type;
	}
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public OffsetRangeSelector getSource() {
		return source;
	}


	public void setSource(OffsetRangeSelector source) {
		this.source = source;
	}


	public OffsetRangeSelector getTarget() {
		return target;
	}


	public void setTarget(OffsetRangeSelector target) {
		this.target = target;
	}


	public String getSentence() {
		return sentence;
	}


	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	// ---------------------------------------------------------------------------------------------------------------------

}
