package org.epnoi.uia.parameterization;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "solrInformationStore")
public class SOLRInformationStoreParameters extends InformationStoreParameters {


	@Override
	public String toString() {
		return "SOLR Information Store [host:" + super.getHost() + " path: "
				+ super.getPath() + "  port:" + super.getPort()+"]";
	}

}
