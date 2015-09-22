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
	private final String dictionaryURI;
	private final String reverseDictionaryURI;
	private final String relationsURI;
	
	// ------------------------------------------------------------------------------------------------------

	public CassandraWikidataView(Core core, String URI) {
		
		this.URI = URI;
		this.cassandraInformationStore =(CassandraInformationStore) this.core
				.getInformationStoresByType(InformationStoreHelper.CASSANDRA_INFORMATION_STORE).get(0);
	this.dictionaryURI=this.URI+"/dictionary";
	this.reverseDictionaryURI=this.URI+"/reverseDictionary";
	this.relationsURI=this.URI+"/relations";
	
	}

	// ------------------------------------------------------------------------------------------------------

	public String getURI() {
		return URI;
	}
	
	// ------------------------------------------------------------------------------------------------------
/*
	@Override
	public String toString() {
		return "WikidataView [URI=" + URI + ", labelsDictionary=" + labelsDictionary.size()
				+ ", labelsReverseDictionary=" + labelsReverseDictionary.size() + ", relations="
				+ relations.get(RelationHelper.HYPERNYM).size() + "]";
	}
*/
	// ------------------------------------------------------------------------------------------------------
/*
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
*/
	// ------------------------------------------------------------------------------------------------------

	// ------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		Core core = CoreUtility.getUIACore();
		WikidataHandlerParameters parameters = new WikidataHandlerParameters();

		/*
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
	*/
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
		String labelIRI = this.URI + "/labels#" + label;
		return cassandraInformationStore.getQueryResolver().getValues(this.dictionaryURI,labelIRI,
				WikidataViewCassandraHelper.WIKIDATA_COLUMN_FAMILY);

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
		
		return cassandraInformationStore.getQueryResolver().getValues(this.relationsURI+type,sourceIRI,
				WikidataViewCassandraHelper.WIKIDATA_COLUMN_FAMILY);
	}

	// ------------------------------------------------------------------------------------------------------

		/**
		 * 
		 * @param targetIRI
		 * @return
		 */

		public Set<String> getLabelsOfIRI(String IRI) {
			return this.cassandraInformationStore.getQueryResolver().getValues(this.reverseDictionaryURI, IRI,
					WikidataViewCassandraHelper.WIKIDATA_COLUMN_FAMILY);

		}
}
