package org.epnoi.model;

import org.epnoi.model.commons.StringUtils;

public class Term implements Resource {
	private String uri;
	private AnnotatedWord<TermMetadata> annotatedTerm;

	// -----------------------------------------------------------------------------

	public Term() {
		this.annotatedTerm = new AnnotatedWord<TermMetadata>(new TermMetadata());
	}

	// -----------------------------------------------------------------------------


	public String getUri() {
		return uri;
	}

	// -----------------------------------------------------------------------------

	public void setUri(String uri) {
		this.uri = uri;
	}

	// -----------------------------------------------------------------------------

	public AnnotatedWord<TermMetadata> getAnnotatedTerm() {
		return annotatedTerm;
	}

	// -----------------------------------------------------------------------------

	public void setAnnotatedTerm(AnnotatedWord<TermMetadata> annotatedTerm) {
		this.annotatedTerm = annotatedTerm;
	}

	// -----------------------------------------------------------------------------

	public static String buildURI(String term, String domain) {
		String uri = "http://" + domain + "/"
				+ StringUtils.replace(term, "[^a-zA-Z0-9]", "_");
		return uri;

	}

	// -----------------------------------------------------------------------------

	@Override
	public String toString() {
		return "Term [URI=" + uri + ", annotatedTerm=" + annotatedTerm + "]";
	}

	// -----------------------------------------------------------------------------

}
