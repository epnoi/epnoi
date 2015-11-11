package org.epnoi.learner.terms;

import java.util.HashSet;
import java.util.Set;

public class DomainMetadata implements Comparable<DomainMetadata> {

	private Set<String> resources;
	private long numberOfTerms;
	private double minDomainConsensus = 1;
	private double maxDomainConsensus = 0;
	private double minCValue = 100000;
	private double maxCValue = 0;
	// ---------------------------------------------------------------------------------------------------------

	public DomainMetadata() {
		this.numberOfTerms = 0;
		this.resources = new HashSet<String>();

	}

	// ---------------------------------------------------------------------------------------------------------

	@Override
	public int compareTo(DomainMetadata o) {
		// TODO Auto-generated method stub
		return 0;
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

	public Set<String> getResources() {
		return resources;
	}

	// ---------------------------------------------------------------------------------------------------------

	public void setResources(Set<String> resources) {
		this.resources = resources;
	}

	// ---------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "DomainMetadata [resources=" + resources + ", numberOfTerms="
				+ numberOfTerms + "]";
	}

	// ---------------------------------------------------------------------------------------------------------
	
	public double getMinDomainConsensus() {
		return minDomainConsensus;
	}

	// ---------------------------------------------------------------------------------------------------------
	
	public void setMinDomainConsensus(double minDomainConsensus) {
		this.minDomainConsensus = minDomainConsensus;
	}
	
	// ---------------------------------------------------------------------------------------------------------

	public double getMaxDomainConsensus() {
		return maxDomainConsensus;
	}
	
	// ---------------------------------------------------------------------------------------------------------

	public void setMaxDomainConsensus(double maxDomainConsensus) {
		this.maxDomainConsensus = maxDomainConsensus;
	}
	
	// ---------------------------------------------------------------------------------------------------------

	public double getMinCValue() {
		return minCValue;
	}

	// ---------------------------------------------------------------------------------------------------------
	
	public void setMinCValue(double minCValue) {
		this.minCValue = minCValue;
	}

	// ---------------------------------------------------------------------------------------------------------
	
	public double getMaxCValue() {
		return maxCValue;
	}
	
	// ---------------------------------------------------------------------------------------------------------

	public void setMaxCValue(double maxCValue) {
		this.maxCValue = maxCValue;
	}
	
	// ---------------------------------------------------------------------------------------------------------
}
