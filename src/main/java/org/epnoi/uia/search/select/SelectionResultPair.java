package org.epnoi.uia.search.select;

public class SelectionResultPair {
	private String resourceURI;
	private float score;

	// -----------------------------------------------------------------------

	public String getResourceURI() {
		return resourceURI;
	}

	// -----------------------------------------------------------------------

	public void setResourceURI(String resourceURI) {
		this.resourceURI = resourceURI;
	}

	// -----------------------------------------------------------------------

	public float getScore() {
		return score;
	}

	// -----------------------------------------------------------------------

	public void setScore(float score) {
		this.score = score;
	}

	// -----------------------------------------------------------------------

	@Override
	public String toString() {
		return "SelectionResultPair [resourceURI=" + resourceURI + ", score="
				+ score + "]";
	}

}
