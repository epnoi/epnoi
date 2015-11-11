package org.epnoi.learner.terms;

import org.epnoi.model.AnnotatedWord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomainsIndex {
	
	private Map<String, AnnotatedWord<DomainMetadata>> domains;

	// -------------------------------------------------------------------------------------------------------

	public void init() {
		this.domains = new HashMap<String, AnnotatedWord<DomainMetadata>>();
	}

	// -------------------------------------------------------------------------------------------------------

	public AnnotatedWord<DomainMetadata> lookUp(String word) {
		return domains.get(word);
	}

	// -------------------------------------------------------------------------------------------------------

	public void updateTerm(String domain, String resourceURI) {

		AnnotatedWord<DomainMetadata> retrievedDomain = this.domains
				.get(domain);
		if (retrievedDomain == null) {
			retrievedDomain = new AnnotatedWord<DomainMetadata>(
					new DomainMetadata());
			retrievedDomain.setWord(domain);
			this.domains.put(domain, retrievedDomain);
		}

		retrievedDomain.getAnnotation().getResources().add(resourceURI);

		/*
		 * resource.getAnnotation().setNumberOfTerms(
		 * resource.getAnnotation().getNumberOfTerms() + 1); Long ocurrences =
		 * resource.getAnnotation().getTermsOcurrences() .get(term.getWord());
		 * if (ocurrences == null) {
		 * resource.getAnnotation().getTermsOcurrences() .put(term.getWord(),
		 * new Long(1)); } else { resource.getAnnotation().getTermsOcurrences()
		 * .put(term.getWord(), ocurrences + 1); }
		 */
	}

	// -------------------------------------------------------------------------------------------------------

	public AnnotatedWord<DomainMetadata> getDomain(String domain) {
		return this.domains.get(domain);
	}

	// -------------------------------------------------------------------------------------------------------

	public List<AnnotatedWord<DomainMetadata>> getDomains() {
		return new ArrayList<>(this.domains.values());
	}
}
