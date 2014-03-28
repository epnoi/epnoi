package org.epnoi.uia.informationsources;

import org.epnoi.uia.parameterization.InformationStoreParameters;

import epnoi.model.InformationSourceSubscription;

public interface InformationSourceQueryGenerator {
	public String generateQuery(
			InformationSourceSubscription informationSourceSubscription, InformationStoreParameters parameters);
}
