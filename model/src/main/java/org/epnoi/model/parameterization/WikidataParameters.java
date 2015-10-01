package org.epnoi.model.parameterization;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "wordnet")

public class WikidataParameters {

	private boolean considered;
	private boolean inMemory;
	

	private String mode;
	
	private String dumpPath;
	private String uri;
	

	//-----------------------------------------------------------------
	
	public boolean isConsidered() {
		return considered;
	}
	
	//-----------------------------------------------------------------
	
	public void setConsidered(boolean considered) {
		this.considered = considered;
	}
	
	//-----------------------------------------------------------------
	
	public String getMode() {
		return mode;
	}
	
	//-----------------------------------------------------------------
	
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	//-----------------------------------------------------------------
	
	public String getDumpPath() {
		return dumpPath;
	}
	
	//-----------------------------------------------------------------
	
	public void setDumpPath(String dumpPath) {
		this.dumpPath = dumpPath;
	}
	
	//-----------------------------------------------------------------
	
	public String getUri() {
		return uri;
	}
	
	//-----------------------------------------------------------------

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	//-----------------------------------------------------------------
	
	public boolean isInMemory() {
		return inMemory;
	}
	
	//-----------------------------------------------------------------

	public void setInMemory(boolean inMemory) {
		this.inMemory = inMemory;
	}
	
	//-----------------------------------------------------------------

	@Override
	public String toString() {
		return "WikidataParameters [considered=" + considered + ", mode=" + mode + ", dumpPath="
				+ dumpPath + ", uri=" + uri + "]";
	}
	
	
	

}
