package org.epnoi.model.parameterization;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cassandraInformationStore")
public class CassandraInformationStoreParameters extends InformationStoreParameters {
	

	// ------------------------------------------------------------------------------------------
		
	@Override
	public String toString() {
		return "Cassandra Information Store [host:" + super.getHost() + " path: "
				+ super.getPath() + "  port:" + super.getPort() + "]";
	}

}
