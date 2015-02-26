package org.epnoi.model;

import javax.xml.bind.annotation.XmlElement;

import org.epnoi.uia.commons.StringUtils;
import org.epnoi.uia.learner.terms.AnnotatedWord;
import org.epnoi.uia.learner.terms.TermMetadata;

public class Term implements Resource {
	private String URI;
	private AnnotatedWord<TermMetadata> annotatedTerm;

	// -----------------------------------------------------------------------------

	public Term() {
		this.annotatedTerm = new AnnotatedWord<TermMetadata>(new TermMetadata());
	}

	// -----------------------------------------------------------------------------

	@XmlElement(name = "URI")
	public String getURI() {
		return URI;
	}

	// -----------------------------------------------------------------------------

	public void setURI(String uri) {
		this.URI = uri;
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
		return "Term [URI=" + URI + ", annotatedTerm=" + annotatedTerm + "]";
	}

	// -----------------------------------------------------------------------------

}
