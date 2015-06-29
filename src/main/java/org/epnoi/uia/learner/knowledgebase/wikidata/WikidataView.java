package org.epnoi.uia.learner.knowledgebase.wikidata;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.cassandra.thrift.Cassandra.system_add_column_family_args;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.Resource;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.learner.knowledgebase.wikidata.WikidataHandlerParameters.DumpProcessingMode;

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

	public void clean() {
		System.out.println("Initially we had "
				+ this.relations.get(RelationHelper.HYPERNYM).size()
				+ " hypernymy relations");
		int relationsCount = 0;
		for (String originURI : this.relations.get(RelationHelper.HYPERNYM)
				.keySet()) {
			for (String destinationURI : this.relations.get(
					RelationHelper.HYPERNYM).get(originURI)) {
				if (this.labelsReverseDictionary.containsKey(originURI)
						&& this.labelsReverseDictionary
								.containsKey(destinationURI)) {
					relationsCount++;
					/*
					if(relationsCount%1000==0){
						System.out.println(originURI+"---->"+destinationURI);
					}
					*/
				}
			}
		}
		System.out.println("There were " + relationsCount);
	}

	// ------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		Core core = CoreUtility.getUIACore();
		WikidataHandlerParameters parameters = new WikidataHandlerParameters();

		parameters.setParameter(WikidataHandlerParameters.WIKIDATA_VIEW_URI,
				WikidataHandlerParameters.DEFAULT_URI);
		parameters.setParameter(WikidataHandlerParameters.OFFLINE_MODE, true);
		parameters.setParameter(WikidataHandlerParameters.DUMP_FILE_MODE,
				DumpProcessingMode.JSON);
		parameters.setParameter(WikidataViewCreatorParameters.TIMEOUT, 100);
		parameters.setParameter(WikidataViewCreatorParameters.DUMP_PATH,
				"/opt/epnoi/epnoideployment/wikidata");

		WikidataViewCreator wikidataViewCreator = new WikidataViewCreator();
		try {
			wikidataViewCreator.init(core, parameters);
		} catch (EpnoiInitializationException e) {

			e.printStackTrace();
		}
		long currentTime=System.currentTimeMillis();
	
		WikidataView wikidataView = wikidataViewCreator
				.retrieve(WikidataHandlerParameters.DEFAULT_URI);
	
		System.out.println("It took "+(System.currentTimeMillis()-currentTime)+" to retrieve the wikidata view");
		
		currentTime=System.currentTimeMillis();
		wikidataView.clean();
		System.out.println("It took "+(System.currentTimeMillis()-currentTime)+" to clean the wikidata view");
	}
}
