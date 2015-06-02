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
	private String gatePath;

	private ArrayList<VirtuosoInformationStoreParameters> virtuosoInformationStores;
	private ArrayList<SOLRInformationStoreParameters> solrInformationStores;
	private ArrayList<CassandraInformationStoreParameters> cassandraInformationStores;
	private ArrayList<MapInformationStoreParameters> mapInformationStores;

	private RSSHoarderParameters rssHoarder;
	private RSSHarvesterParameters rssHarvester;

	// ---------------------------------------------------------------------------------

	public ParametersModel() {
		this.virtuosoInformationStores = new ArrayList<>();
		this.solrInformationStores = new ArrayList<>();
		this.cassandraInformationStores = new ArrayList<>();
		this.mapInformationStores = new ArrayList<>();
	}

	// ---------------------------------------------------------------------------------

	public ArrayList<VirtuosoInformationStoreParameters> getVirtuosoInformationStore() {
		return virtuosoInformationStores;
	}

	// ---------------------------------------------------------------------------------

	public void setVirtuosoInformationStore(
			ArrayList<VirtuosoInformationStoreParameters> virtuosoInformationStore) {
		this.virtuosoInformationStores = virtuosoInformationStore;
	}

	// ---------------------------------------------------------------------------------

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

	public String getGatePath() {
		return this.gatePath;
	}

	// ---------------------------------------------------------------------------------

	public void setGatePath(String gatePath) {
		this.gatePath=gatePath;
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

	public ArrayList<MapInformationStoreParameters> getMapInformationStore() {
		return this.mapInformationStores;
	}

	// ---------------------------------------------------------------------------------

	public void setMapInformationStore(
			ArrayList<MapInformationStoreParameters> mapInformationStores) {
		this.mapInformationStores = mapInformationStores;
	}

	// ---------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "CoreParametersModel [hostname=" + hostname + ", port=" + port
				+ ", path=" + path + ", gatePath=" + gatePath
				+ ", virtuosoInformationStores=" + virtuosoInformationStores
				+ ", solrInformationStores=" + solrInformationStores
				+ ", cassandraInformationStores=" + cassandraInformationStores
				+ ", mapInformationStores=" + mapInformationStores
				+ ", rssHoarder=" + rssHoarder + ", rssHarvester="
				+ rssHarvester + "]";
	}

	// ---------------------------------------------------------------------------------

}
