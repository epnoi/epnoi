package org.epnoi.model;

import java.util.ArrayList;
import java.util.List;

public class RelationalSentencesCorpus implements Resource {

	private String uri;
	private String description;
	private String type;
	private List<RelationalSentence> sentences = new ArrayList<RelationalSentence>();

	// --------------------------------------------------------------------------------------------

	public List<RelationalSentence> getSentences() {
		return sentences;
	}

	// --------------------------------------------------------------------------------------------

	public void setSentences(List<RelationalSentence> sentences) {
		this.sentences = sentences;
	}

	// --------------------------------------------------------------------------------------------

	public String getUri() {
		return uri;
	}

	// --------------------------------------------------------------------------------------------

	public void setUri(String uRI) {
		uri = uRI;
	}

	// --------------------------------------------------------------------------------------------

	public String getDescription() {
		return description;
	}

	// --------------------------------------------------------------------------------------------

	public void setDescription(String description) {
		this.description = description;
	}

	// --------------------------------------------------------------------------------------------

	public String getType() {
		return type;
	}

	// --------------------------------------------------------------------------------------------

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "RelationalSentencesCorpus [URI=" + uri + ", description="
				+ description + ", type=" + type + ", #sentences=" + sentences.size()
				+ "]";
	}

	// --------------------------------------------------------------------------------------------

}
