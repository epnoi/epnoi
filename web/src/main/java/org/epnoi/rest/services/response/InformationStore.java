package org.epnoi.rest.services.response;

import javax.xml.bind.annotation.XmlRootElement;

import org.epnoi.uia.parameterization.InformationStoreParameters;

@XmlRootElement
public class InformationStore {
	private InformationStoreParameters informationStoreParameters;
	private Boolean status;

	public InformationStoreParameters getInformationStoreParameters() {
		return informationStoreParameters;
	}

	public void setInformationStoreParameters(
			InformationStoreParameters informationStoreParameters) {
		this.informationStoreParameters = informationStoreParameters;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}
