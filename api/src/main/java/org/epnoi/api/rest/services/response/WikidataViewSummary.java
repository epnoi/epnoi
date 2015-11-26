package org.epnoi.api.rest.services.response;

public class WikidataViewSummary {
	private Long labelsDictionarySize;
	private Long reverseLabelDictionarySize;
	private Long numberOfHypernymyRelations;
	private Long numberOfUsefulRelations;

	// -------------------------------------------------------------------------------------------
	
	public Long getLabelsDictionarySize() {
		return labelsDictionarySize;
	}
	
	// -------------------------------------------------------------------------------------------

	public void setLabelsDictionarySize(Long labelsDictionarySize) {
		this.labelsDictionarySize = labelsDictionarySize;
	}
	
	// -------------------------------------------------------------------------------------------

	public Long getReverseLabelDictionarySize() {
		return reverseLabelDictionarySize;
	}
	
	// -------------------------------------------------------------------------------------------

	public void setReverseLabelDictionarySize(Long reverseLabelDictionarySize) {
		this.reverseLabelDictionarySize = reverseLabelDictionarySize;
	}

	// -------------------------------------------------------------------------------------------
	
	public Long getNumberOfHypernymyRelations() {
		return numberOfHypernymyRelations;
	}
	
	// -------------------------------------------------------------------------------------------

	public void setNumberOfHypernymyRelations(Long numberOfHypernymyRelations) {
		this.numberOfHypernymyRelations = numberOfHypernymyRelations;
	}
	
	// -------------------------------------------------------------------------------------------

	public Long getNumberOfUsefulRelations() {
		return numberOfUsefulRelations;
	}
	
	// -------------------------------------------------------------------------------------------

	public void setNumberOfUsefulRelations(Long numberOfUsefulRelations) {
		this.numberOfUsefulRelations = numberOfUsefulRelations;
	}

	// -------------------------------------------------------------------------------------------

}
