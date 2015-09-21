package org.epnoi.uia.knowledgebase.wikidata;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.epnoi.model.RelationHelper;
import org.epnoi.model.Resource;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.CassandraInformationStore;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.dao.cassandra.WikidataViewCassandraHelper;
import org.epnoi.uia.knowledgebase.wikidata.WikidataHandlerParameters.DumpProcessingMode;

public class CassandraWikidataView {
	private String URI;
	Core core;
	CassandraInformationStore cassandraInformationStore;
	
	

	private Map<String, Set<String>> labelsDictionary;
	private Map<String, Set<String>> labelsReverseDictionary;
	private Map<String, Map<String, Set<String>>> relations;

	// ------------------------------------------------------------------------------------------------------

	public CassandraWikidataView(Core core, String URI, Map<String, Set<String>> labelsDictionary,
			Map<String, Set<String>> labelsReverseDictionary, Map<String, Map<String, Set<String>>> relations) {
		super();
		this.URI = URI;
		this.labelsDictionary = labelsDictionary;
		this.labelsReverseDictionary = labelsReverseDictionary;
		this.relations = relations;
		this.cassandraInformationStore =(CassandraInformationStore) this.core
				.getInformationStoresByType(InformationStoreHelper.CASSANDRA_INFORMATION_STORE).get(0);
	}

	// ------------------------------------------------------------------------------------------------------

	public CassandraWikidataView() {
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

	@Override
	public String toString() {
		return "WikidataView [URI=" + URI + ", labelsDictionary=" + labelsDictionary.size()
				+ ", labelsReverseDictionary=" + labelsReverseDictionary.size() + ", relations="
				+ relations.get(RelationHelper.HYPERNYM).size() + "]";
	}

	// ------------------------------------------------------------------------------------------------------

	public void count() {
		System.out.println(
				"Initially we had " + this.relations.get(RelationHelper.HYPERNYM).size() + " hypernymy relations");
		int relationsCount = 0;
		for (String originURI : this.relations.get(RelationHelper.HYPERNYM).keySet()) {
			for (String destinationURI : this.relations.get(RelationHelper.HYPERNYM).get(originURI)) {
				if (this.labelsReverseDictionary.containsKey(originURI)
						&& this.labelsReverseDictionary.containsKey(destinationURI)) {

					relationsCount++;
				}
			}
		}
		System.out.println("There were " + relationsCount);
	}

	// ------------------------------------------------------------------------------------------------------

	// ------------------------------------------------------------------------------------------------------

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

		CassandraWikidataView wikidataView = wikidataViewCreator.retrieve(WikidataHandlerParameters.DEFAULT_URI);

		System.out.println("It took " + (System.currentTimeMillis() - currentTime) + " to retrieve the wikidata view");

		currentTime = System.currentTimeMillis();
		wikidataView.count();
		System.out.println("It took " + (System.currentTimeMillis() - currentTime) + " to clean the wikidata view");
	}

	// ------------------------------------------------------------------------------------------------------

	/**
	 * Given a label it returns all the IRIs of items with such label
	 * 
	 * @param label
	 * @return
	 */
	public Set<String> getIRIsOfLabel(String label) {
		// TODO Auto-generated method stub

		CassandraInformationStore cassandraInformationStore = (CassandraInformationStore) this.core
				.getInformationStoresByType(InformationStoreHelper.CASSANDRA_INFORMATION_STORE);
		String labelIRI = this.URI + "/labels/" + label;
		return cassandraInformationStore.getQueryResolver().getValues(labelIRI, WikidataViewCassandraHelper.VALUES,
				WikidataViewCassandraHelper.DICTIONARY);

		// cassandraInformationStore.qu
	}

	// ------------------------------------------------------------------------------------------------------

	/**
	 * 
	 * @param type
	 * @param sourceIRI
	 * @return
	 */
	public Set<String> getIRIRelatedIRIs(String type, String sourceIRI) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------------------------------------

	/**
	 * 
	 * @param targetIRI
	 * @return
	 */

	public Set<String> getLabelsOfIRI(String IRI) {
		
		
		return this.cassandraInformationStore.getQueryResolver().getValues(IRI, WikidataViewCassandraHelper.VALUES,
				WikidataViewCassandraHelper.DICTIONARY);

	}
}
