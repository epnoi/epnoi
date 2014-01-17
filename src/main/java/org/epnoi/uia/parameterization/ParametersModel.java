package org.epnoi.uia.parameterization;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

//import javax.xml.bind.annotation.
@XmlRootElement(name = "parametersModel")
public class ParametersModel {

	// Server related properties
	private String hostname;
	private String port;
	private String path;
	// private String scope;

	private ArrayList<VirtuosoInformationStoreParameters> virtuosoInformationStores;
	private ArrayList<SOLRInformationStoreParameters> solrInformationStores;
	private ArrayList<CassandraInformationStoreParameters> cassandraInformationStores;

	private RSSHoarderParameters rssHoarder;
	private RSSHarvesterParameters rssHarvester;

	public ParametersModel() {
		this.virtuosoInformationStores = new ArrayList<VirtuosoInformationStoreParameters>();
		this.solrInformationStores = new ArrayList<SOLRInformationStoreParameters>();
		this.cassandraInformationStores = new ArrayList<CassandraInformationStoreParameters>();
	}

	public ArrayList<VirtuosoInformationStoreParameters> getVirtuosoInformationStore() {
		return virtuosoInformationStores;
	}

	public void setVirtuosoInformationStore(
			ArrayList<VirtuosoInformationStoreParameters> virtuosoInformationStore) {
		this.virtuosoInformationStores = virtuosoInformationStore;
	}

	public void resolveToAbsolutePaths(Class<? extends Object> referenceClass) {
		/*
		 * TRANSLATES LOCAL PATHS TO ABSOLUTE PATHS IF NECESSARY String
		 * completeModelPath = this.modelPath; if (this.modelPath.charAt(0) !=
		 * '/') { completeModelPath = referenceClass.getResource(this.modelPath)
		 * .getPath(); } this.setModelPath(completeModelPath); for
		 * (KeywordRecommenderParameters keywordRecommender :
		 * this.keywordBasedRecommender) {
		 * 
		 * if (keywordRecommender.getIndexPath().charAt(0) != '/') {
		 * keywordRecommender.setIndexPath(referenceClass.getResource(
		 * keywordRecommender.getIndexPath()).getPath()); }
		 * 
		 * }
		 */
	}

	// ---------------------------------------------------------------------------------

	public RSSHoarderParameters getRssHoarder() {
		return rssHoarder;
	}

	// ---------------------------------------------------------------------------------

	public void setRssHoarder(RSSHoarderParameters rssHoarder) {
		this.rssHoarder = rssHoarder;
	}

	// ---------------------------------------------------------------------------------

	public RSSHarvesterParameters getRssHarvester() {
		return rssHarvester;
	}

	// ---------------------------------------------------------------------------------

	public void setRssHarvester(RSSHarvesterParameters rssHarvester) {
		this.rssHarvester = rssHarvester;
	}

	// ---------------------------------------------------------------------------------

	public ArrayList<SOLRInformationStoreParameters> getSolrInformationStore() {
		return solrInformationStores;
	}

	// ---------------------------------------------------------------------------------

	public void setSolrInformationStore(
			ArrayList<SOLRInformationStoreParameters> solrInformationStores) {
		this.solrInformationStores = solrInformationStores;
	}

	// ---------------------------------------------------------------------------------

	public ArrayList<CassandraInformationStoreParameters> getCassandraInformationStore() {
		return cassandraInformationStores;
	}

	// ---------------------------------------------------------------------------------

	public void setCassandraInformationStore(
			ArrayList<CassandraInformationStoreParameters> cassandraInformationStores) {
		this.cassandraInformationStores = cassandraInformationStores;
	}

	// ---------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "ParametersModel [hostname=" + hostname + ", port=" + port
				+ ", path=" + path + ", virtuosoInformationStores="
				+ virtuosoInformationStores + ", solrInformationStores="
				+ solrInformationStores + ", rssHoarder=" + rssHoarder
				+ ", rssHarvester=" + rssHarvester + "]";
	}

}
