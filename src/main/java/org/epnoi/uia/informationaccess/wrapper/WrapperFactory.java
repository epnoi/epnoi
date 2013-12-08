package org.epnoi.uia.informationaccess.wrapper;

import java.util.HashMap;

import org.epnoi.uia.core.Core;

import epnoi.model.Feed;
import epnoi.model.InformationSource;
import epnoi.model.Resource;

public class WrapperFactory {
	private HashMap<String, Wrapper> wrappers;
	private Core core;

	//-------------------------------------------------------------------------------------------------------------
	
	public WrapperFactory(Core core) {
		this.core = core;
		this.wrappers = new HashMap<String, Wrapper>();
		this.wrappers.put(InformationSource.class.getName(),
				new InformationSourceWrapper(this.core));
		this.wrappers.put(Feed.class.getName(),
				new FeedWrapper(this.core));

	}

	//-------------------------------------------------------------------------------------------------------------
	
	public Wrapper build(Resource resource) {

		return this.wrappers.get(resource.getClass().getName());
	}

}
