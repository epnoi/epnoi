package org.epnoi.uia.informationaccess;

import java.util.ArrayList;
import java.util.List;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationaccess.events.InformationAccessListener;
import org.epnoi.uia.informationaccess.wrapper.Wrapper;
import org.epnoi.uia.informationaccess.wrapper.WrapperFactory;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.parameterization.ParametersModel;
import org.epnoi.uia.search.select.SearchSelectResult;
import org.epnoi.uia.search.select.SelectExpression;

import epnoi.model.Context;
import epnoi.model.Resource;

public class InformationAccessImplementation implements InformationAccess {

	private Core core;

	private WrapperFactory wrapperFactory;

	private List<InformationAccessListener> listeners;

	// ---------------------------------------------------------------------------

	public InformationAccessImplementation(Core core) {
		this.core = core;
		this.wrapperFactory = new WrapperFactory(core);
		this.listeners = new ArrayList<InformationAccessListener>();

	}

	// ---------------------------------------------------------------------------

	public void put(Resource resource) {
		Wrapper wrapper = this.wrapperFactory.build(resource);
		wrapper.put(resource);

	}

	// ---------------------------------------------------------------------------

	public void update(Resource resource) {
		Wrapper wrapper = this.wrapperFactory.build(resource);
		wrapper.update(resource);

	}

	// ---------------------------------------------------------------------------

	public void put(Resource resource, Context context) {
		Wrapper wrapper = this.wrapperFactory.build(resource);
		wrapper.put(resource, context);

	}

	// ---------------------------------------------------------------------------

	public Resource get(String URI) {
		// TODO Auto-generated method stub
		return null;
	}

	// ---------------------------------------------------------------------------

	public Resource get(String URI, String resourceType) {
		Wrapper wrapper = this.wrapperFactory.build(resourceType);
		return wrapper.get(URI);
	}

	// ---------------------------------------------------------------------------

	public void remove(String URI, String resourceType) {
		Wrapper wrapper = this.wrapperFactory.build(resourceType);
		wrapper.remove(URI);
	}

	// ---------------------------------------------------------------------------

	public void remove(Resource resource) {
		Wrapper wrapper = this.wrapperFactory.build(resource);
		wrapper.remove(resource.getURI());

	}

	// ---------------------------------------------------------------------------

	public void init(ParametersModel parameters) {
		// TODO Auto-generated method stub

	}

	// ---------------------------------------------------------------------------

	public SearchSelectResult query(SelectExpression queryExpression) {
		return new SearchSelectResult(null);
	}

	// ---------------------------------------------------------------------------

	public void addInformationStore(InformationStore informationStore) {
		// TODO Auto-generated method stub

	}

	// ---------------------------------------------------------------------------

	public void removeInformationStore(String URI) {
		// TODO Auto-generated method stub

	}

	// ---------------------------------------------------------------------------

	public synchronized void publish(String eventType, Resource source) {
		for (InformationAccessListener listener : this.listeners) {
			listener.notify(eventType, source);
		}
	}

	// ---------------------------------------------------------------------------

	public synchronized void subscribe(InformationAccessListener listener,
			String subscriptionExpression) {
		this.listeners.add(listener);
	}

}
