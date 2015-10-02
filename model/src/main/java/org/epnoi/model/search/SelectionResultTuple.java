package org.epnoi.model.search;

public class SelectionResultTuple {
	private String resourceURI;
	private float score;
	private String type;

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

	public String getType() {
		return type;
	}
	
	// -----------------------------------------------------------------------
	
	public void setType(String type) {
		this.type = type;
	}

	// -----------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "SelectionResultTuple [resourceURI=" + resourceURI + ", score="
				+ score + ", type=" + type + "]";
	}

}
