package org.epnoi.sources;

import org.epnoi.model.InformationSource;
import org.epnoi.model.InformationSourceNotification;
import org.epnoi.model.InformationSourceSubscription;
import org.epnoi.model.Resource;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.InformationSourcesHandler;
import org.epnoi.model.modules.InformationStore;
import org.epnoi.model.modules.InformationStoreHelper;
import org.epnoi.model.parameterization.VirtuosoInformationStoreParameters;
import org.epnoi.model.rdf.InformationSourceRDFHelper;
import org.epnoi.model.rdf.InformationSourceSubscriptionRDFHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class InformationSourcesHandlerImpl implements InformationSourcesHandler {
    @Autowired
    Core core;

    public InformationSourcesHandlerImpl() {

    }

    @PostConstruct
    @Override
    public void init() throws EpnoiInitializationException {

    }

    public List<InformationSourceNotification> retrieveNotifications(
            String informationSourceSubscriptionURI) {
        List<InformationSourceNotification> notifications = new ArrayList<InformationSourceNotification>();
        InformationStore informationStore = core.getInformationStoresByType(
                InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
        VirtuosoInformationStoreParameters parameters = (VirtuosoInformationStoreParameters) informationStore
                .getParameters();
        InformationSourceSubscription informationSourceSubscription = (InformationSourceSubscription) this.core
                .getInformationHandler()
                .get(informationSourceSubscriptionURI,
                        InformationSourceSubscriptionRDFHelper.INFORMATION_SOURCE_SUBSCRIPTION_CLASS);

        // System.out.println("ISS> " + informationSourceSubscription);

        InformationSource informationSource = (InformationSource) this.core
                .getInformationHandler().get(
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
            informationSourceNotification.setUri(informationUnitURI);
            Resource resource = this.core.getInformationHandler().get(
                    informationUnitURI,
                    informationSource.getInformationUnitType());
            informationSourceNotification.setResource(resource);
            informationSourceNotification.setTimestamp(date.toString());
            notifications.add(informationSourceNotification);

        }
        return notifications;
    }
}
