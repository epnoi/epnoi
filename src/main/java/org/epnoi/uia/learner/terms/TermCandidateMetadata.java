package org.epnoi.uia.learner.terms;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TermCandidateMetadata implements Comparable<TermCandidateMetadata> {
	Map<String, Object> metadata = new HashMap<>();

	public static final String OCURRENCES = "OCURRENCES";
	public static final String OCURRENCES_AS_SUBTERM = "OCURRENCES_OTHER_TERMS";
	public static final String NUMBER_OF_SUPERTERMS = "NUMBER_OF_SUPERTERMS";

	private int length;
	private String[] words;
	private long ocurrences;
	private long ocurrencesAsSubterm;
	private long numberOfSuperterns;
	private double cValue;

	// -------------------------------------------------------------------------------------------------------
	TermCandidateMetadata() {
		this.length = 0;
		this.words = null;
		this.ocurrences = 1L;
		this.ocurrencesAsSubterm = 0;
		this.numberOfSuperterns = 0;
		this.cValue= 0;
	}

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
	public int compareTo(TermCandidateMetadata termMetadata) {

		return termMetadata.length - this.length;
	}

	// -------------------------------------------------------------------------------------------------------

	public Long getOcurrences() {
		return ocurrences;
	}

	// -------------------------------------------------------------------------------------------------------

	public void setOcurrences(Long ocurrences) {
		this.ocurrences = ocurrences;
	}

	// -------------------------------------------------------------------------------------------------------

	public Long getOcurrencesAsSubterm() {
		return ocurrencesAsSubterm;
	}

	// -------------------------------------------------------------------------------------------------------

	public void setOcurrencesAsSubterm(Long ocurrencesAsSubterm) {
		this.ocurrencesAsSubterm = ocurrencesAsSubterm;
	}

	// -------------------------------------------------------------------------------------------------------

	public Long getNumberOfSuperterns() {
		return numberOfSuperterns;
	}

	// -------------------------------------------------------------------------------------------------------

	public void setNumberOfSuperterns(Long numberOfSuperterns) {
		this.numberOfSuperterns = numberOfSuperterns;
	}

	// -------------------------------------------------------------------------------------------------------

	public String[] getWords() {
		return words;
	}

	// -------------------------------------------------------------------------------------------------------

	public void setWords(String[] words) {
		this.words = words;
	}

	// -------------------------------------------------------------------------------------------------------

	public void setCValue(double cValue) {
		this.cValue = cValue;
	}
	
	// -------------------------------------------------------------------------------------------------------
	
	public double getCValue() {
		return this.cValue;
	}
	
	// -------------------------------------------------------------------------------------------------------

	
	@Override
	public String toString() {
		return "TermCandidateMetadata [length=" + length + ", words="
				+ Arrays.toString(words) + ", ocurrences=" + ocurrences
				+ ", ocurrencesAsSubterm=" + ocurrencesAsSubterm
				+ ", numberOfSuperterns=" + numberOfSuperterns + ", cValue="
				+ cValue + "]";
	}
}
