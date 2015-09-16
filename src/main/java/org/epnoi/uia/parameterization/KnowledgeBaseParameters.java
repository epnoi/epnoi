package org.epnoi.uia.parameterization;

import javax.xml.bind.annotation.XmlRootElement;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;

@XmlRootElement(name = "knowledgeBase")
public class KnowledgeBaseParameters {
	private WordnetParameters wordnet;
	private WikidataParameters wikidata;

	// ---------------------------------------------------------------------------------
	
	public WikidataParameters getWikidata() {
		return wikidata;
	}
	
	// ---------------------------------------------------------------------------------

	public void setWikidata(WikidataParameters wikidata) {
		this.wikidata = wikidata;
	}
	
	// ---------------------------------------------------------------------------------

	public WordnetParameters getWordnet() {
		return wordnet;
	}
	
	// ---------------------------------------------------------------------------------

	public void setWordnet(WordnetParameters wordnet) {
		this.wordnet = wordnet;
	}

	// ---------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "KnowledgeBaseParameters [wordnet=" + wordnet + ", wikidata=" + wikidata + "]";
	}
	
	// ---------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out.println("testing!");
		Core core = CoreUtility.getUIACore();
		System.out.println("------> "+core.getParameters());
		
	}

	

}
