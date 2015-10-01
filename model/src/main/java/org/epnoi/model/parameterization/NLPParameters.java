package org.epnoi.model.parameterization;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="nlp")
public class NLPParameters {
	private String gatePath;
	int numberOfProcessors;
	
	// ------------------------------------------------------------------------------------------
	
	public String getGatePath() {
		return gatePath;
	}
	
	// ------------------------------------------------------------------------------------------
	
	public void setGatePath(String gatePath) {
		this.gatePath = gatePath;
	}
	
	// ------------------------------------------------------------------------------------------
	
	public int getNumberOfProcessors() {
		return numberOfProcessors;
	}
	// ------------------------------------------------------------------------------------------
	
	public void setNumberOfProcessors(int numberOfProcessors) {
		this.numberOfProcessors = numberOfProcessors;
	}
	
	// ------------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "NLPParameters [gatePath=" + gatePath + ", numberOfProcessors="
				+ numberOfProcessors + "]";
	}

	// ------------------------------------------------------------------------------------------
	

}
