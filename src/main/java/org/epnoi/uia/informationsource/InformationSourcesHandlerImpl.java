package org.epnoi.uia.informationsource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.dao.rdf.FeedRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.InformationSourceRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.InformationSourceSubscriptionRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFOAIOREHelper;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

import epnoi.model.InformationSource;
import epnoi.model.InformationSourceNotification;
import epnoi.model.InformationSourceSubscription;
import epnoi.model.Resource;

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

		//System.out.println("ISS> " + informationSourceSubscription);

		InformationSource informationSource = (InformationSource) this.core
				.getInformationAccess().get(
						informationSourceSubscription.getInformationSource(),
						InformationSourceRDFHelper.INFORMATION_SOURCE_CLASS);
		

		

		//System.out.println("IS> " + informationSource);

		String queryExpression = "SELECT ?uri FROM <{GRAPH}> WHERE "
				+ "{ ?feedURI <{URL_PROPERTY}> \"{INFORMATION_SOURCE_URL}\" ."
				+ " ?feedURI a <{FEED_CLASS}> ."
				+ " ?feedURI <{AGGREGATES_PROPERTY}> ?uri . }";

		queryExpression = queryExpression
				.replace("{GRAPH}", parameters.getGraph())
				.replace("{FEED_CLASS}", FeedRDFHelper.FEED_CLASS)
				.replace("{URL_PROPERTY}", RDFHelper.URL_PROPERTY)
				.replace("{AGGREGATES_PROPERTY}",
						RDFOAIOREHelper.AGGREGATES_PROPERTY)
				.replace("{INFORMATION_SOURCE_URL}", informationSource.getURL());

		Date date= new java.util.Date();
		
		for (String informationUnitURI : informationStore.query(queryExpression)) {

			InformationSourceNotification informationSourceNotification = new InformationSourceNotification();
			informationSourceNotification.setURI(informationUnitURI);
			Resource resource = this.core.getInformationAccess().get(informationUnitURI, informationSource.getInformationUnitType());
			informationSourceNotification.setResource(resource);
			informationSourceNotification.setTimestamp(date.toString());
			notifications.add(informationSourceNotification);

		}
		return notifications;
	}
}
