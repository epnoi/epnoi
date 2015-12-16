package org.epnoi.knowledgebase.wikidata;

import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.InformationStoreHelper;
import org.epnoi.uia.informationstore.CassandraInformationStore;
import org.epnoi.uia.informationstore.dao.cassandra.WikidataViewCassandraHelper;

import java.util.HashSet;
import java.util.Set;

public class CassandraWikidataView {
	private String URI;
	Core core;
	CassandraInformationStore cassandraInformationStore;
	private final String dictionaryURI;
	private final String reverseDictionaryURI;
	private final String relationsURI;
	
	// ------------------------------------------------------------------------------------------------------

	public CassandraWikidataView(Core core, String URI) {
		this.core=core;
		this.URI = URI;
		
		this.cassandraInformationStore =(CassandraInformationStore) this.core.getInformationHandler()
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
	
	
	public Set<String> getRelated(String sourceLabel, String type) {

		Set<String> targetLabels = new HashSet<String>();

		//Map<String, Set<String>> consideredRelations = this.wikidataView.getRelations().get(type);

		// Firstly we retrieve the IRIs of the source label
		Set<String> sourceIRIs = this.getIRIsOfLabel(sourceLabel);
	//	System.out.println("Inital sourceIRIs obtained from the label" +sourceIRIs);
		if (sourceIRIs != null) {

			for (String sourceIRI : sourceIRIs) {
				//System.out.println("sourceIRI " + sourceIRI);
				Set<String> targetIRIs = this.getIRIRelatedIRIs(type, sourceIRI);
				// System.out.println(" ("+sourceIRI+") targetIRIs " +
				// targetIRIs);
				if (targetIRIs != null) {
					for (String targetIRI : targetIRIs) {
						// System.out.println(" trying > "+ targetIRI);
						// // .getLabelsReverseDictionary().get(
						// targetIRI));
						if (targetIRI != null) {
							if (this.getLabelsOfIRI(targetIRI) != null) {

								for (String destinationTarget : this.getLabelsOfIRI(targetIRI)) {
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


	// ------------------------------------------------------------------------------------------------------

	// ------------------------------------------------------------------------------------------------------
	/* FOR_TEST
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
	*/
	// ------------------------------------------------------------------------------------------------------

	/**
	 * Given a label it returns all the IRIs of items with such label
	 * 
	 * @param label
	 * @return
	 */
	public Set<String> getIRIsOfLabel(String label) {
		System.out.println("_---------------------------------------------------------------________>"+label+"<");
		
		//String labelIRI = this.URI + "/labels#" + label;
		if(label.length()>1) {
			return cassandraInformationStore.getQueryResolver().getValues(this.dictionaryURI, label,
					WikidataViewCassandraHelper.COLUMN_FAMILY);
		}
	return new HashSet<>();
	}

	// ------------------------------------------------------------------------------------------------------

	/**
	 * 
	 * @param type
	 * @param sourceIRI
	 * @return
	 */
	public Set<String> getIRIRelatedIRIs(String type, String sourceIRI) {

		return cassandraInformationStore.getQueryResolver().getValues(this.relationsURI+"/"+type,sourceIRI,
				WikidataViewCassandraHelper.COLUMN_FAMILY);
	}

	// ------------------------------------------------------------------------------------------------------

		/**
		 * 
		 * @param targetIRI
		 * @return
		 */

		public Set<String> getLabelsOfIRI(String IRI) {
			return this.cassandraInformationStore.getQueryResolver().getValues(this.reverseDictionaryURI, IRI,
					WikidataViewCassandraHelper.COLUMN_FAMILY);

		}
}
