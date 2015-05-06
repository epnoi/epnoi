package org.epnoi.uia.learner.relations.knowledgebase.wikidata;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.epnoi.model.RelationHelper;
import org.epnoi.model.Resource;

public class WikidataView implements Resource {
	private String URI;
	private Map<String, Set<String>> labelsDictionary;
	private Map<String, Set<String>> labelsReverseDictionary;
	private Map<String, Map<String, Set<String>>> relations;

	// ------------------------------------------------------------------------------------------------------

	public WikidataView(String URI, Map<String, Set<String>> labelsDictionary,
			Map<String, Set<String>> labelsReverseDictionary,
			Map<String, Map<String, Set<String>>> relations) {
		super();
		this.URI = URI;
		this.labelsDictionary = labelsDictionary;
		this.labelsReverseDictionary = labelsReverseDictionary;
		this.relations = relations;
	}

	// ------------------------------------------------------------------------------------------------------

	public WikidataView() {
		this.labelsDictionary = new HashMap<>();
		this.labelsReverseDictionary = new HashMap<>();
		this.relations = new HashMap<>();
	}

	// ------------------------------------------------------------------------------------------------------

	public String getURI() {
		return URI;
	}

	// ------------------------------------------------------------------------------------------------------

	public void setURI(String URI) {
		this.URI = URI;
	}

	// ------------------------------------------------------------------------------------------------------

	public Map<String, Set<String>> getLabelsDictionary() {
		return labelsDictionary;
	}

	// ------------------------------------------------------------------------------------------------------

	public void setLabelsDictionary(Map<String, Set<String>> labelsDictionary) {
		this.labelsDictionary = labelsDictionary;
	}

	// ------------------------------------------------------------------------------------------------------

	public Map<String, Set<String>> getLabelsReverseDictionary() {
		return labelsReverseDictionary;
	}

	// ------------------------------------------------------------------------------------------------------

	public void setLabelsReverseDictionary(
			Map<String, Set<String>> labelsReverseDictionary) {
		this.labelsReverseDictionary = labelsReverseDictionary;
	}

	// ------------------------------------------------------------------------------------------------------

	public Map<String, Map<String, Set<String>>> getRelations() {
		return relations;
	}

	// ------------------------------------------------------------------------------------------------------

	public void setRelations(Map<String, Map<String, Set<String>>> relations) {
		this.relations = relations;
	}

	// ------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "WikidataView [URI=" + URI + ", labelsDictionary="
				+ labelsDictionary.size() + ", labelsReverseDictionary="
				+ labelsReverseDictionary.size() + ", relations="
				+ relations.get(RelationHelper.HYPERNYM).size() + "]";
	}

	// ------------------------------------------------------------------------------------------------------

}
