package org.epnoi.uia.informationaccess.wrapper;

import java.util.logging.Logger;

import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.User;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;

public class UserWrapper implements Wrapper {
	private static final Logger logger = Logger.getLogger(UserWrapper.class
			.getName());

	Core core;

	// -------------------------------------------------------------------------------------

	public UserWrapper(Core core) {
		this.core = core;
	}

	// -------------------------------------------------------------------------------------

	public void remove(String URI) {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, UserRDFHelper.USER_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		informationStore.remove(selector);
		informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.CASSANDRA_INFORMATION_STORE).get(0);
		informationStore.remove(selector);
	}

	// -------------------------------------------------------------------------------------

	public void put(Resource resource, Context context) {
		logger.info("Putting the user:");
		logger.info("User: " + resource);
		// First we update the information about the user in the cassandra
		// stores
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);

		informationStore.put(resource,context);
		// Finally we store the RDF information associated with the user
		informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
		informationStore.put(resource,context);
	}

	// -------------------------------------------------------------------------------------

	public Resource get(String URI) {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, UserRDFHelper.USER_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		User cassandraUser = (User) informationStore.get(selector);

		informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		User rdfUser = (User) informationStore.get(selector);

		if ((cassandraUser == null) && (rdfUser == null)) {
			return null;
		}
		if ((cassandraUser == null) || (rdfUser == null)) {
			logger.severe("Thre was a data discrepancy when retrieving the user \n casssandraUser: "
					+ cassandraUser + " \n rdfUser: " + rdfUser);

			return null;
		}
		User combinedUser = new User();
		combinedUser.setURI(URI);
		combinedUser.setName(cassandraUser.getName());
		combinedUser.setKnowledgeObjects(rdfUser.getKnowledgeObjects());
		combinedUser.setInformationSourceSubscriptions(rdfUser
				.getInformationSourceSubscriptions());
		return combinedUser;

	}
	
	// -------------------------------------------------------------------------------------

	@Override
	public void update(Resource resource) {
		// TODO Auto-generated method stub
		
	}

	// -------------------------------------------------------------------------------------

	
	
	
}
