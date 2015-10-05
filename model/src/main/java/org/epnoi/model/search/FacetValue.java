package org.epnoi.model.search;

public class FacetValue {
	
	private String value;
	private long count;

	//------------------------------------------------------------------------

	public String getValue() {
		return value;
	}
	
	//------------------------------------------------------------------------

	public void setValue(String value) {
		this.value = value;
	}
	
	//------------------------------------------------------------------------

	public long getCount() {
		return count;
	}
	
	//------------------------------------------------------------------------

	public void setCount(long count) {
		this.count = count;
	}
	
	//------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "FacetValue [value=" + value
				+ ", count=" + count + "]";
	}

	//------------------------------------------------------------------------
}
