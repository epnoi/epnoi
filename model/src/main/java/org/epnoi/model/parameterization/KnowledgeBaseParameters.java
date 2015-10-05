package org.epnoi.model.parameterization;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "knowledgeBase")
public class KnowledgeBaseParameters {
	private WordnetParameters wordnet;
	private WikidataParameters wikidata;
	private boolean lazy;

	// ---------------------------------------------------------------------------------
	
	public boolean isLazy() {
		return lazy;
	}
	
	// ---------------------------------------------------------------------------------

	public void setLazy(boolean lazy) {
		this.lazy = lazy;
	}
	
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
/*
	public static void main(String[] args) {
		System.out.println("testing!");
		Core core = CoreUtility.getUIACore();
		System.out.println("------> "+core.getParameters());
		
	}
*/
	// ---------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "KnowledgeBaseParameters [wordnet=" + wordnet + ", wikidata=" + wikidata + ", lazy=" + lazy + "]";
	}

	

}
