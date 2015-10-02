package org.epnoi.uia.informationsources;

import org.epnoi.model.InformationSourceSubscription;
import org.epnoi.model.parameterization.InformationStoreParameters;

public interface InformationSourceQueryBuilder {
	public String generateQuery(
			InformationSourceSubscription informationSourceSubscription, InformationStoreParameters parameters);
}
