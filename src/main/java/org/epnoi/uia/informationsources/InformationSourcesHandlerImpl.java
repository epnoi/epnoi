package org.epnoi.uia.informationsources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.epnoi.model.InformationSource;
import org.epnoi.model.InformationSourceNotification;
import org.epnoi.model.InformationSourceSubscription;
import org.epnoi.model.Resource;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.dao.rdf.InformationSourceRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.InformationSourceSubscriptionRDFHelper;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;


public class InformationSourcesHandlerImpl implements InformationSourcesHandler {

	Core core = null;

	public InformationSourcesHandlerImpl(Core core) {
		this.core = core;
	}

	public List<InformationSourceNotification> retrieveNotifications(
			String informationSourceSubscriptionURI) {
		List<InformationSourceNotification> notifications = new ArrayList<InformationSourceNotification>();
		InformationStore informationStore = core.getInformationStoresByType(
				InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
		VirtuosoInformationStoreParameters parameters = (VirtuosoInformationStoreParameters) informationStore
				.getParameters();
		InformationSourceSubscription informationSourceSubscription = (InformationSourceSubscription) this.core
				.getInformationAccess()
				.get(informationSourceSubscriptionURI,
						InformationSourceSubscriptionRDFHelper.INFORMATION_SOURCE_SUBSCRIPTION_CLASS);

		// System.out.println("ISS> " + informationSourceSubscription);

		InformationSource informationSource = (InformationSource) this.core
				.getInformationAccess().get(
						informationSourceSubscription.getInformationSource(),
						InformationSourceRDFHelper.INFORMATION_SOURCE_CLASS);

		InformationSourceQueryBuilder informationSourceQueryGenerator = new RSSInformationSourceQueryBuilder();

		

		Date date = new java.util.Date();
		String queryExpression = informationSourceQueryGenerator.generateQuery(
				informationSourceSubscription, parameters);
		System.out.println("QueryExpression> " + queryExpression);
		for (String informationUnitURI : informationStore
				.query(queryExpression)) {

			InformationSourceNotification informationSourceNotification = new InformationSourceNotification();
			informationSourceNotification.setURI(informationUnitURI);
			Resource resource = this.core.getInformationAccess().get(
					informationUnitURI,
					informationSource.getInformationUnitType());
			informationSourceNotification.setResource(resource);
			informationSourceNotification.setTimestamp(date.toString());
			notifications.add(informationSourceNotification);

		}
		return notifications;
	}
}
