package org.epnoi.uia.learner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomainsTable {
	Map<String, List<String>> domains;
	private String targetDomain;

	// ------------------------------------------------------------------------------------------

	public DomainsTable() {

		this.domains = new HashMap<String, List<String>>();
	}

	// ------------------------------------------------------------------------------------------

	public Map<String, List<String>> getDomains() {
		return domains;
	}

	// ------------------------------------------------------------------------------------------

	public void setDomains(Map<String, List<String>> domains) {
		this.domains = domains;
	}

	// ------------------------------------------------------------------------------------------

	public List<String> getConsideredDomains() {
		return new ArrayList<String>(this.domains.keySet());
	}

	// ------------------------------------------------------------------------------------------

	public String getTargetDomain() {
		return targetDomain;
	}

	// ------------------------------------------------------------------------------------------

	public void setTargetDomain(String targetDomain) {
		this.targetDomain = targetDomain;
	}

	// ------------------------------------------------------------------------------------------

}
