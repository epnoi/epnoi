package org.epnoi.learner.terms;

import java.util.HashMap;
import java.util.Map;

public class ResourceMetadata implements Comparable<ResourceMetadata> {

	private Map<String, Long> termsOcurrences;
	private long numberOfTerms;

	// ---------------------------------------------------------------------------------------------------------
	
	public ResourceMetadata() {
		this.numberOfTerms = 0;
		this.termsOcurrences = new HashMap<String, Long>();

	}
	
	// ---------------------------------------------------------------------------------------------------------

	@Override
	public int compareTo(ResourceMetadata o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	// ---------------------------------------------------------------------------------------------------------

	public Map<String, Long> getTermsOcurrences() {
		return termsOcurrences;
	}
	
	// ---------------------------------------------------------------------------------------------------------

	public void setTermsOcurrences(Map<String, Long> termsOcurrences) {
		this.termsOcurrences = termsOcurrences;
	}

	// ---------------------------------------------------------------------------------------------------------
	
	public long getNumberOfTerms() {
		return numberOfTerms;
	}

	// ---------------------------------------------------------------------------------------------------------
	
	public void setNumberOfTerms(long numberOfTerms) {
		this.numberOfTerms = numberOfTerms;
	}
	
	// ---------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "ResourceMetadata [termsOcurrences=" + termsOcurrences
				+ ", numberOfTerms=" + numberOfTerms + "]";
	}

}
