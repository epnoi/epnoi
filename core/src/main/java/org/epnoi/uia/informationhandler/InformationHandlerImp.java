package org.epnoi.uia.informationhandler;

import java.util.ArrayList;
import java.util.List;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.Selector;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.InformationAccessListener;
import org.epnoi.model.modules.InformationHandler;
import org.epnoi.model.modules.InformationStore;
import org.epnoi.model.parameterization.ParametersModel;
import org.epnoi.model.parameterization.VirtuosoInformationStoreParameters;
import org.epnoi.uia.informationhandler.wrapper.Wrapper;
import org.epnoi.uia.informationhandler.wrapper.WrapperFactory;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.VirtuosoInformationStore;

public class InformationHandlerImp implements InformationHandler {

	private Core core;

	private WrapperFactory wrapperFactory;

	private List<InformationAccessListener> listeners;

	// ---------------------------------------------------------------------------

	public InformationHandlerImp(Core core) {
		this.core = core;
		this.wrapperFactory = new WrapperFactory(core);
		this.listeners = new ArrayList<InformationAccessListener>();

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
		resource = null;
		context.clear();

	}

	// ---------------------------------------------------------------------------

	public Resource get(String URI) {
		// TODO: As it is now it just delivers items/feeds
		Resource resource = null;

		String resourceType = this.getType(URI);
		if (resourceType != null) {

			Wrapper wrapper = this.wrapperFactory.build(resourceType);
			resource = wrapper.get(URI);

		}
		return resource;
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

	// ---------------------------------------------------------------------------

	@Override
	public Content<String> getContent(Selector selector) {
		Wrapper wrapper = this.wrapperFactory.build(selector
				.getProperty(SelectorHelper.TYPE));
		Content<String> content = wrapper.getContent(selector);

		return content;
	}

	// ---------------------------------------------------------------------------

	@Override
	public Content<Object> getAnnotatedContent(Selector selector) {
		Wrapper wrapper = this.wrapperFactory.build(selector
				.getProperty(SelectorHelper.TYPE));

		Content<Object> content = wrapper.getAnnotatedContent(selector);
		return content;
	}

	// ---------------------------------------------------------------------------

	@Override
	public void setContent(Selector selector, Content<String> content) {
		Wrapper wrapper = this.wrapperFactory.build(selector
				.getProperty(SelectorHelper.TYPE));
		wrapper.setContent(selector, content);

	}

	// ---------------------------------------------------------------------------

	@Override
	public void setAnnotatedContent(Selector selector,
			Content<Object> annotatedContent) {
		Wrapper wrapper = this.wrapperFactory.build(selector
				.getProperty(SelectorHelper.TYPE));

		wrapper.setAnnotatedContent(selector, annotatedContent);

	}

	// ---------------------------------------------------------------------------

	@Override
	public boolean contains(String URI, String resourceType) {

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, resourceType);
		selector.setProperty(SelectorHelper.URI, URI);
		Wrapper wrapper = this.wrapperFactory.build(resourceType);
		return wrapper.exists(URI);

	}

	// ---------------------------------------------------------------------------

	@Override
	public List<String> getAll(String resourceType) {
		// ------------------------------------------------------------------------------

		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String queryExpression = "SELECT DISTINCT ?uri FROM <{GRAPH}>"
				+ "{ ?uri a <" + resourceType + "> ." + "}";

		queryExpression = queryExpression
				.replace(
						"{GRAPH}",
						((VirtuosoInformationStoreParameters) informationStore
								.getParameters()).getGraph());

		 System.out.println("QUERY EXPRESSION ----------> " +queryExpression);
		List<String> queryResults = informationStore.query(queryExpression);

		return queryResults;
	}

	// ---------------------------------------------------------------------------

	public String getType(String URI) {
		VirtuosoInformationStore informationStore = (VirtuosoInformationStore) this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		return informationStore.getType(URI);
	}

}
