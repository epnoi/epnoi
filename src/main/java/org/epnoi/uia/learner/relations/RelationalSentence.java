package org.epnoi.uia.learner.relations;

import org.epnoi.model.OffsetRangeSelector;

public class RelationalSentence {
	OffsetRangeSelector source;
	OffsetRangeSelector target;
	String sentence;

	// --------------------------------------------------------------------------------------

	public RelationalSentence(OffsetRangeSelector source,
			OffsetRangeSelector target, String sentence) {
		super();
		this.source = source;
		this.target = target;
		this.sentence = sentence;

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

	// ---------------------------------------------------------------------------------------------------------------------

}
