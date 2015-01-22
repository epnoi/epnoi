package org.epnoi.model;

public class OffsetRangeSelector {
	private Long start;
	private Long end;

	public OffsetRangeSelector(Long start, Long end) {
		super();
		this.start = start;
		this.end = end;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getEnd() {
		return end;
	}

	public void setEnd(Long end) {
		this.end = end;
	}
}
