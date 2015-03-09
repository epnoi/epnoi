package org.epnoi.uia.learner.relations;

import java.util.ArrayList;
import java.util.List;

import org.epnoi.model.Resource;

public class RelationalSentencesCorpus implements Resource {

	private String URI;
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

	public String getURI() {
		return URI;
	}

	// --------------------------------------------------------------------------------------------

	public void setURI(String uRI) {
		URI = uRI;
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
		return "RelationalSentencesCorpus [URI=" + URI + ", description="
				+ description + ", type=" + type + ", #sentences=" + sentences.size()
				+ "]";
	}

	// --------------------------------------------------------------------------------------------

}
