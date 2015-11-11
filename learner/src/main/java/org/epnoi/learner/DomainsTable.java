package org.epnoi.learner;

import org.epnoi.model.Domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomainsTable {
	Map<String, Domain> domains; // Map that stores the domains (their URI is
									// used as the key)
	Map<String, List<String>> domainResources; // Map that stores the list of
												// resources URIs associated
												// with each domain (their URI
												// is used as a key)
	private String targetDomainURI;

	// ------------------------------------------------------------------------------------------

	public DomainsTable() {

		this.domains = new HashMap<>();
		this.domainResources = new HashMap<>();
	}

	// ------------------------------------------------------------------------------------------

	public Map<String, List<String>> getDomainResources() {
		return domainResources;
	}

	// ------------------------------------------------------------------------------------------

	public void setDomainResoruces(Map<String, List<String>> domains) {
		this.domainResources = domains;
	}

	// ------------------------------------------------------------------------------------------

	public List<String> getConsideredDomains() {
		return new ArrayList<String>(this.domainResources.keySet());
	}

	// ------------------------------------------------------------------------------------------

	public Domain getTargetDomain() {
		return this.domains.get(this.targetDomainURI);
	}

	// ------------------------------------------------------------------------------------------

	public void setTargetDomain(String targetDomainURI) {
		this.targetDomainURI = targetDomainURI;
	}

	// ------------------------------------------------------------------------------------------

	public Map<String, Domain> getDomains() {
		return domains;
	}

	// ------------------------------------------------------------------------------------------

	public Domain getDomain(String URI) {
		return this.domains.get(URI);
	}

	// ------------------------------------------------------------------------------------------

	public void addDomain(Domain domain) {
		System.out.println(domain);
		this.domains.put(domain.getUri(), domain);
	}
	
	// ------------------------------------------------------------------------------------------

	public void addDomainResources(String URI, List<String> foundURIs) {
		this.domainResources.put(URI, foundURIs);
		
	}
}
