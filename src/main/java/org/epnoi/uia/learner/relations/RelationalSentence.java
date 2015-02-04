package org.epnoi.uia.learner.relations;

import org.epnoi.model.OffsetRangeSelector;

public class RelationalSentence {
	OffsetRangeSelector source;
	OffsetRangeSelector target;
	String sentence;
	String annotatedSentence;

	// --------------------------------------------------------------------------------------

	public RelationalSentence(OffsetRangeSelector source,
			OffsetRangeSelector target, String sentence, String annotatedSentence) {
		super();
		this.source = source;
		this.target = target;
		this.sentence = sentence;
		this.annotatedSentence=annotatedSentence;

	}

	// --------------------------------------------------------------------------------------

	public OffsetRangeSelector getSource() {
		return source;
	}

	// --------------------------------------------------------------------------------------

	public void setSource(OffsetRangeSelector source) {
		this.source = source;
	}

	// --------------------------------------------------------------------------------------

	public OffsetRangeSelector getTarget() {
		return target;
	}

	// --------------------------------------------------------------------------------------

	public void setTarget(OffsetRangeSelector target) {
		this.target = target;
	}

	// --------------------------------------------------------------------------------------

	public String getSentence() {
		return sentence;
	}

	// --------------------------------------------------------------------------------------

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	// --------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "RelationalSentence [source=" + source + ", target=" + target
				+ ", sentence=" + sentence + "]";
	}

	// --------------------------------------------------------------------------------------

	public String getAnnotatedSentence() {
		return annotatedSentence;
	}

	// --------------------------------------------------------------------------------------

	public void setAnnotatedSentence(String annotatedSentence) {
		this.annotatedSentence = annotatedSentence;
	}

	// ---------------------------------------------------------------------------------------------------------------------

}
