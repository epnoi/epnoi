package org.epnoi.api.rest.services.response;

import org.epnoi.model.parameterization.InformationStoreParameters;

import javax.xml.bind.annotation.XmlRootElement;

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
