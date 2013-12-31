package org.epnoi.uia.informationaccess.wrapper;

import java.util.HashMap;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.dao.rdf.SearchRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;

import epnoi.model.Feed;
import epnoi.model.InformationSource;
import epnoi.model.Resource;
import epnoi.model.User;

public class WrapperFactory {
	private HashMap<String, Wrapper> wrappersByClass;
	private HashMap<String, Wrapper> wrappersByType;
	private Core core;

	// -------------------------------------------------------------------------------------------------------------

	public WrapperFactory(Core core) {
		this.core = core;
		this.wrappersByClass = new HashMap<String, Wrapper>();
		this.wrappersByType = new HashMap<String, Wrapper>();
		this.wrappersByClass.put(InformationSource.class.getName(),
				new InformationSourceWrapper(this.core));
		this.wrappersByClass.put(Feed.class.getName(), new FeedWrapper(
				this.core));
		this.wrappersByClass.put(User.class.getName(), new UserWrapper(
				this.core));

		this.wrappersByType.put(UserRDFHelper.USER_CLASS, new UserWrapper(
				this.core));
		this.wrappersByType.put(SearchRDFHelper.SEARCH_CLASS, new SearchWrapper(
				this.core));
	}

	// -------------------------------------------------------------------------------------------------------------

	public Wrapper build(Resource resource) {

		return this.wrappersByClass.get(resource.getClass().getName());
	}

	// -------------------------------------------------------------------------------------------------------------

	public Wrapper build(String resourceType) {

		return this.wrappersByType.get(resourceType);
	}

}
