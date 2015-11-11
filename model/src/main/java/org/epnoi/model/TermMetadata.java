package org.epnoi.model;

import java.util.Arrays;

public class TermMetadata implements Comparable<TermMetadata> {
	// Map<String, Object> metadata = new HashMap<>();
	/*
	 * public static final String OCURRENCES = "OCURRENCES"; public static final
	 * String OCURRENCES_AS_SUBTERM = "OCURRENCES_OTHER_TERMS"; public static
	 * final String NUMBER_OF_SUPERTERMS = "NUMBER_OF_SUPERTERMS";
	 */
	private int length;
	private String[] words;
	private long ocurrences;
	private long ocurrencesAsSubterm;
	private long numberOfSuperterns;
	private double cValue;
	private double domainConsensus;
	private double domainPertinence;
	private double termProbability;
	private double termhood;

	// -------------------------------------------------------------------------------------------------------
	public TermMetadata() {
		this.length = 0;
		this.words = null;
		this.ocurrences = 1L;
		this.ocurrencesAsSubterm = 0;
		this.numberOfSuperterns = 0;
		this.cValue = 0;
		this.domainConsensus = 0;
		// this.termProbability = 0;
		this.termhood = 0;
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
	/*
	 * public Object getMetadataProperty(String property) { return
	 * this.metadata.get(property); }
	 * 
	 * //
	 * ------------------------------------------------------------------------
	 * -------------------------------
	 * 
	 * public void setMetadataProperty(String property, Object value) {
	 * this.metadata.put(property, value); }
	 * 
	 * //
	 * ------------------------------------------------------------------------
	 * -------------------------------
	 */
	@Override
	public int compareTo(TermMetadata termMetadata) {

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

	public double getDomainConsensus() {
		return domainConsensus;
	}

	// -------------------------------------------------------------------------------------------------------

	public void setDomainConsensus(double domainConsensus) {
		this.domainConsensus = domainConsensus;
	}

	// -------------------------------------------------------------------------------------------------------

	public double getTermProbability() {
		return termProbability;
	}

	public void setTermProbability(double termProbability) {
		this.termProbability = termProbability;
	}

	// -------------------------------------------------------------------------------------------------------

	public double getDomainPertinence() {
		return domainPertinence;
	}

	// -------------------------------------------------------------------------------------------------------

	public void setDomainPertinence(double domainPertinence) {
		this.domainPertinence = domainPertinence;
	}

	// -------------------------------------------------------------------------------------------------------

	public double getTermhood() {
		return termhood;
	}

	// -------------------------------------------------------------------------------------------------------

	public void setTermhood(double termhood) {
		this.termhood = termhood;
	}
	
	// -------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "TermMetadata [length=" + length + ", words="
				+ Arrays.toString(words) + ", ocurrences=" + ocurrences
				+ ", ocurrencesAsSubterm=" + ocurrencesAsSubterm
				+ ", numberOfSuperterns=" + numberOfSuperterns + ", cValue="
				+ cValue + ", domainConsensus=" + domainConsensus
				+ ", domainPertinence=" + domainPertinence
				+ ", termProbability=" + termProbability + ", termhood="
				+ termhood + "]";
	}

	// -------------------------------------------------------------------------------------------------------

}
