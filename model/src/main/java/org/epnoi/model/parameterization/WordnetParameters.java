package org.epnoi.model.parameterization;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "wordnet")
public class WordnetParameters {
	private boolean considered;
	private String dictionaryPath;

	//----------------------------------------------------------------------------------------
	
	public boolean isConsidered() {
		return considered;
	}
	
	//----------------------------------------------------------------------------------------

	public void setConsidered(boolean considered) {
		this.considered = considered;
	}
	
	//----------------------------------------------------------------------------------------

	public String getDictionaryPath() {
		return dictionaryPath;
	}

	//----------------------------------------------------------------------------------------
	
	public void setDictionaryPath(String dictionaryPath) {
		this.dictionaryPath = dictionaryPath;
	}
	
	//----------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "WordnetParameters [considered=" + considered + ", dictionaryPath=" + dictionaryPath + "]";
	}
	
	

}
