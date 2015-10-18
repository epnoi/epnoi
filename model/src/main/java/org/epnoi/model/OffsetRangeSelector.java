package org.epnoi.model;

import java.io.Serializable;

public class OffsetRangeSelector implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1045582730543038449L;
	private Long start;
	private Long end;

	// ------------------------------------------------------------------------------------------

	public OffsetRangeSelector(Long start, Long end) {
		super();
		this.start = start;
		this.end = end;
	}

	// ------------------------------------------------------------------------------------------

	public Long getStart() {
		return start;
	}

	// ------------------------------------------------------------------------------------------

	public void setStart(Long start) {
		this.start = start;
	}

	// ------------------------------------------------------------------------------------------

	public Long getEnd() {
		return end;
	}

	// ------------------------------------------------------------------------------------------

	public void setEnd(Long end) {
		this.end = end;
	}

	// ------------------------------------------------------------------------------------------

	@Override
	public boolean equals(Object obj) {
		return ((obj instanceof OffsetRangeSelector)
				&& ((OffsetRangeSelector) obj).getStart().equals(this.start) && ((OffsetRangeSelector) obj)
				.getEnd().equals(this.end));
	}

	// ------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "OffsetRangeSelector [start=" + start + ", end=" + end + "]";
	}

	// ------------------------------------------------------------------------------------------

}
