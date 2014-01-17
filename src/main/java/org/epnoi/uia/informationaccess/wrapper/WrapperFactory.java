package org.epnoi.uia.informationaccess.wrapper;

import java.util.HashMap;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationaccess.wrapper.exception.WrapperNotFoundException;
import org.epnoi.uia.informationstore.dao.rdf.FeedRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.InformationSourceRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.InformationSourceSubscriptionRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.SearchRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;

import epnoi.model.Feed;
import epnoi.model.InformationSource;
import epnoi.model.InformationSourceSubscription;
import epnoi.model.Item;
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
		this.wrappersByClass.put(InformationSourceSubscription.class.getName(),
				new InformationSourceSubscriptionWrapper(this.core));
		this.wrappersByClass.put(Item.class.getName(),
				new ItemWrapper(this.core));

		
		
		this.wrappersByType.put(UserRDFHelper.USER_CLASS, new UserWrapper(
				this.core));
		this.wrappersByType.put(SearchRDFHelper.SEARCH_CLASS,
				new SearchWrapper(this.core));
		this.wrappersByType.put(
				InformationSourceRDFHelper.INFORMATION_SOURCE_CLASS,
				new InformationSourceWrapper(this.core));
		this.wrappersByType.put(
				InformationSourceSubscriptionRDFHelper.INFORMATION_SOURCE_SUBSCRIPTION_CLASS,
				new InformationSourceSubscriptionWrapper(this.core));
		this.wrappersByType.put(
				FeedRDFHelper.FEED_CLASS,
				new FeedWrapper(this.core));
		this.wrappersByType.put(
				FeedRDFHelper.ITEM_CLASS,
				new ItemWrapper(this.core));
	}

	// -------------------------------------------------------------------------------------------------------------

	public Wrapper build(Resource resource) throws WrapperNotFoundException {
		Wrapper wrapper = this.wrappersByClass.get(resource.getClass()
				.getName());
		if (wrapper == null) {
			throw new WrapperNotFoundException(
					"There is no wrapper defined for a "
							+ resource.getClass().getName());
		}
		return wrapper;
	}

	// -------------------------------------------------------------------------------------------------------------

	public Wrapper build(String resourceType) throws WrapperNotFoundException {

		Wrapper wrapper = this.wrappersByType.get(resourceType);
		if (wrapper == null) {
			throw new WrapperNotFoundException(
					"There is no wrapper defined for a " + resourceType);
		}
		return wrapper;
	}

}
