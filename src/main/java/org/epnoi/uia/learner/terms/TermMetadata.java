package org.epnoi.uia.learner.terms;

import java.util.HashMap;
import java.util.Map;

public class TermMetadata implements Comparable<TermMetadata> {
	Map<String, Object> metadata = new HashMap<>();

	public static final String OCURRENCES = "OCURRENCES";
	public static final String OCURRENCES_AS_SUBTERM = "OCURRENCES_OTHER_TERMS";
	public static final String NUMBER_OF_SUPERTERMS = "NUMBER_OF_SUPERTERMS";

	private int length;

	// -------------------------------------------------------------------------------------------------------

	public int getLength() {
		return length;
	}

	// -------------------------------------------------------------------------------------------------------

	public void setLength(int length) {
		this.length = length;
	}

	// -------------------------------------------------------------------------------------------------------

	public Object getMetadataProperty(String property) {
		return this.metadata.get(property);
	}

	// -------------------------------------------------------------------------------------------------------

	public void setMetadataProperty(String property, Object value) {
		this.metadata.put(property, value);
	}

	// -------------------------------------------------------------------------------------------------------

	@Override
	public int compareTo(TermMetadata termMetadata) {

		return termMetadata.length - this.length;
	}

	@Override
	public String toString() {
		return "TermMetadata [metadata=" + metadata + ", length=" + length
				+ "]";
	}

}
