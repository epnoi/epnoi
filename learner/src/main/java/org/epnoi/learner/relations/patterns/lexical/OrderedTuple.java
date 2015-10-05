package org.epnoi.learner.relations.patterns.lexical;

public class OrderedTuple {
	private String first;
	private String second;

	public OrderedTuple(String first, String second) {
		super();
		this.first = first;
		this.second = second;
	}

	// ---------------------------------------------------------------

	@Override
	public int hashCode() {
		return (first + second).hashCode();
	}

	// ---------------------------------------------------------------

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof OrderedTuple)
				&& this.first.equals(((OrderedTuple) obj).getFirst())
				&& this.second.equals(((OrderedTuple) obj).getSecond());
	}

	// ---------------------------------------------------------------

	public String getFirst() {
		return first;
	}

	// ---------------------------------------------------------------

	public void setFirst(String first) {
		this.first = first;
	}

	// ---------------------------------------------------------------

	public String getSecond() {
		return second;
	}

	// ---------------------------------------------------------------

	public void setSecond(String second) {
		this.second = second;
	}

	// ---------------------------------------------------------------

}
