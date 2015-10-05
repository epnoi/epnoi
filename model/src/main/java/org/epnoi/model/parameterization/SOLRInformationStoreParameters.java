package org.epnoi.model.parameterization;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "solrInformationStore")
public class SOLRInformationStoreParameters extends InformationStoreParameters {
	private String core;

	// ------------------------------------------------------------------------------------------
	
	public String getCore() {
		return core;
	}

	// ------------------------------------------------------------------------------------------
	
	public void setCore(String core) {
		this.core = core;
	}

	// ------------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "SOLR Information Store [host:" + super.getHost() + " path: "
				+ super.getPath() + "  port:" + super.getPort() + ", core: "+ this.core+"]";
	}

}
