package org.epnoi.uia.learner.terms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourcesIndex {

	//Resources are stored per domain, ie: domain_uri
	
	private Map<String, Map<String, AnnotatedWord<ResourceMetadata>>> resources;

	// -------------------------------------------------------------------------------------------------------

	public void init() {
		this.resources = new HashMap<>();
	}

	// -------------------------------------------------------------------------------------------------------

	public AnnotatedWord<ResourceMetadata> lookUp(String domain, String word) {
		return resources.get(domain).get(word);
	}

	// -------------------------------------------------------------------------------------------------------

	public void updateTerm(String domain, String resourceURI,
			AnnotatedWord<TermMetadata> term) {

		Map<String, AnnotatedWord<ResourceMetadata>> domainResources = this.resources
				.get(domain);

		if (domainResources == null) {
			domainResources = new HashMap<>();
			this.resources.put(domain, domainResources);

		}

		AnnotatedWord<ResourceMetadata> resource = domainResources
				.get(resourceURI);
		if (resource == null) {
			resource = new AnnotatedWord<ResourceMetadata>(
					new ResourceMetadata());
			resource.setWord(resourceURI);
			domainResources.put(resourceURI, resource);
		}
		resource.getAnnotation().setNumberOfTerms(
				resource.getAnnotation().getNumberOfTerms() + 1);
		Long ocurrences = resource.getAnnotation().getTermsOcurrences()
				.get(term.getWord());
		if (ocurrences == null) {
			resource.getAnnotation().getTermsOcurrences()
					.put(term.getWord(), 1L);
		} else {
			resource.getAnnotation().getTermsOcurrences()
					.put(term.getWord(), ocurrences + 1);
		}

	}

	// -------------------------------------------------------------------------------------------------------

	public AnnotatedWord<ResourceMetadata> getResource(String domain,
			String resourceURI) {
		return this.resources.get(domain).get(resourceURI);
	}

	// -------------------------------------------------------------------------------------------------------

	public List<AnnotatedWord<ResourceMetadata>> getResources(String domain) {
		return new ArrayList<>(this.resources.get(domain).values());
	}

}
