package org.epnoi.model.search;

import org.epnoi.model.Resource;

public class OrganizationResultPair {
	private Resource resource;
	private double score;

	// -----------------------------------------------------------------------

	public Resource getResource() {
		return resource;
	}

	// -----------------------------------------------------------------------

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	// -----------------------------------------------------------------------

	public double getScore() {
		return score;
	}

	// -----------------------------------------------------------------------

	public void setScore(double score) {
		this.score = score;
	}

	// -----------------------------------------------------------------------

	@Override
	public String toString() {
		return "SelectionResultPair [resource=" + resource + ", score=" + score
				+ "]";
	}

}
