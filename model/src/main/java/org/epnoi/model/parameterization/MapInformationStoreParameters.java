package org.epnoi.model.parameterization;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "mapInformationStore")
public class MapInformationStoreParameters extends InformationStoreParameters {

	@Override
	public String toString() {
		return "MapInformationStoreParameters [getPort()=" + getPort()
				+ ", getPath()=" + getPath() + ", getHost()=" + getHost()
				+ ", getURI()=" + getURI() + "]";
	}

}
