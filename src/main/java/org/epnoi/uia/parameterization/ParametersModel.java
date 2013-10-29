package org.epnoi.uia.parameterization;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

//import javax.xml.bind.annotation.
@XmlRootElement(name = "parametersModel")
public class ParametersModel {

	//private String modelPath;
	// private String indexPath;
	// private String graphPath;

	// Server related properties
	private String hostname;
	private String port;
	private String path;
	// private String scope;

	private ArrayList<VirtuosoInformationStoreParameters> virtuosoInformationStores;
	private RSSHoarderParameters rssHoarder;
	
	
	
	public ParametersModel() {
		this.virtuosoInformationStores = new ArrayList<VirtuosoInformationStoreParameters>();
	}	

	public ArrayList<VirtuosoInformationStoreParameters> getVirtuosoInformationStore() {
		return virtuosoInformationStores;
	}

	public void setVirtuosoInformationStore(
			ArrayList<VirtuosoInformationStoreParameters> virtuosoInformationStore) {
		this.virtuosoInformationStores = virtuosoInformationStore;
	}

	

	public void resolveToAbsolutePaths(Class<? extends Object> referenceClass) {
/*TRANSLATES LOCAL PATHS TO ABSOLUTE PATHS IF NECESSARY
		String completeModelPath = this.modelPath;
		if (this.modelPath.charAt(0) != '/') {
			completeModelPath = referenceClass.getResource(this.modelPath)
					.getPath();
		}
		this.setModelPath(completeModelPath);
		for (KeywordRecommenderParameters keywordRecommender : this.keywordBasedRecommender) {

			if (keywordRecommender.getIndexPath().charAt(0) != '/') {
				keywordRecommender.setIndexPath(referenceClass.getResource(
						keywordRecommender.getIndexPath()).getPath());
			}

		}
*/
	}

	public RSSHoarderParameters getRssHoarder() {
		return rssHoarder;
	}

	public void setRssHoarder(RSSHoarderParameters rssHoarder) {
		this.rssHoarder = rssHoarder;
	}

}
