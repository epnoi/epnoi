package org.epnoi.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WikidataView implements Resource {
	private String uri;
	private Map<String, Set<String>> labelsDictionary;
	private Map<String, Set<String>> labelsReverseDictionary;
	private Map<String, Map<String, Set<String>>> relations;

	// ------------------------------------------------------------------------------------------------------

	public WikidataView(String URI, Map<String, Set<String>> labelsDictionary,
			Map<String, Set<String>> labelsReverseDictionary, Map<String, Map<String, Set<String>>> relations) {
		super();
		this.uri = URI;
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

	public String getUri() {
		return uri;
	}

	// ------------------------------------------------------------------------------------------------------

	public void setUri(String URI) {
		this.uri = URI;
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

	public void setLabelsReverseDictionary(Map<String, Set<String>> labelsReverseDictionary) {
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

	// ------------------------------------------------------------------------------------------------------

	public void count() {
		System.out.println(
				"Initially we had " + this.relations.get(RelationHelper.HYPERNYMY).size() + " hypernymy relations");
		int relationsCount = 0;
		for (String originURI : this.relations.get(RelationHelper.HYPERNYMY).keySet()) {
			for (String destinationURI : this.relations.get(RelationHelper.HYPERNYMY).get(originURI)) {
				if (this.labelsReverseDictionary.containsKey(originURI)
						&& this.labelsReverseDictionary.containsKey(destinationURI)) {

					relationsCount++;
				}
			}
		}
		System.out.println("There were " + relationsCount);
	}
	// ------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "WikidataView [URI=" + uri + ", labelsDictionary=" + labelsDictionary + ", labelsReverseDictionary="
				+ labelsReverseDictionary + ", relations=" + relations + "]";
	}

	public Set<String> getRelated(String sourceLabel, String type) {

		Set<String> targetLabels = new HashSet<String>();

		Map<String, Set<String>> consideredRelations = this.relations.get(type);

		// Firstly we retrieve the IRIs
		Set<String> sourceIRIs = this.labelsDictionary.get(sourceLabel);
		//System.out.println("Inital sourceIRIs obtained from the label" + sourceIRIs);
		if (sourceIRIs != null) {

			for (String sourceIRI : sourceIRIs) {
				// System.out.println("sourceIRI " + sourceIRI);
				Set<String> targetIRIs = consideredRelations.get(sourceIRI);
				// System.out.println(" ("+sourceIRI+") targetIRIs " +
				// targetIRIs);
				if (targetIRIs != null) {
					for (String targetIRI : targetIRIs) {
					//	System.out.println(" trying > " + targetIRI);
						// System.out.println("->
						// "+this.getLabelsReverseDictionary().get(targetIRI).size());
						if (targetIRI != null) {
							if (this.labelsReverseDictionary.get(targetIRI) != null) {
							//	System.out.println("reverseDict " + this.labelsReverseDictionary);
								for (String destinationTarget : this.labelsReverseDictionary.get(targetIRI)) {
								//	System.out.println("Destination target " + destinationTarget);
									targetLabels.add(destinationTarget);
								}
							}

						}
					}
				}
			}
		}
		return targetLabels;
	}
/*FOR_TEST
	public static void main(String[] args) {
		Core core = CoreUtility.getUIACore();
		WikidataHandlerParameters parameters = new WikidataHandlerParameters();

		parameters.setParameter(WikidataHandlerParameters.WIKIDATA_VIEW_URI, WikidataHandlerParameters.DEFAULT_URI);
		parameters.setParameter(WikidataHandlerParameters.OFFLINE_MODE, true);
		parameters.setParameter(WikidataHandlerParameters.DUMP_FILE_MODE, DumpProcessingMode.JSON);
		parameters.setParameter(WikidataHandlerParameters.TIMEOUT, 100);
		parameters.setParameter(WikidataHandlerParameters.DUMP_PATH, "/opt/epnoi/epnoideployment/wikidata");

		WikidataViewCreator wikidataViewCreator = new WikidataViewCreator();
		try {
			wikidataViewCreator.init(core, parameters);
		} catch (EpnoiInitializationException e) {

			e.printStackTrace();
		}
		long currentTime = System.currentTimeMillis();

		WikidataView wikidataView = wikidataViewCreator.retrieve(WikidataHandlerParameters.DEFAULT_URI);

		System.out.println("It took " + (System.currentTimeMillis() - currentTime) + " to retrieve the wikidata view");

		currentTime = System.currentTimeMillis();
		wikidataView.count();
		System.out.println("It took " + (System.currentTimeMillis() - currentTime) + " to clean the wikidata view");
	}
	*/
}
