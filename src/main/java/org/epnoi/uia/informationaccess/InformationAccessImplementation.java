package org.epnoi.uia.informationaccess;

import java.util.ArrayList;
import java.util.List;

import org.epnoi.model.Content;
import org.epnoi.model.ContentSummary;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationaccess.events.InformationAccessListener;
import org.epnoi.uia.informationaccess.wrapper.Wrapper;
import org.epnoi.uia.informationaccess.wrapper.WrapperFactory;
import org.epnoi.uia.informationstore.CassandraInformationStore;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.FeedRDFHelper;
import org.epnoi.uia.parameterization.ParametersModel;
import org.epnoi.uia.search.select.SearchSelectResult;
import org.epnoi.uia.search.select.SelectExpression;

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
		// TODO: As it is now it just delivers items/feeds
		Resource resource;
		Wrapper wrapper = this.wrapperFactory.build(FeedRDFHelper.ITEM_CLASS);
		resource = wrapper.get(URI);
		if (resource == null) {
			wrapper = this.wrapperFactory.build(FeedRDFHelper.FEED_CLASS);
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

	// ---------------------------------------------------------------------------

	@Override
	public ContentSummary getContentSummary(String URI) {
		// TODO Auto-generated method stub
		return null;
	}

	// ---------------------------------------------------------------------------

	@Override
	public Content<String> getContent(String URI, String resourceType) {
		CassandraInformationStore informationStore = (CassandraInformationStore) this.core
				.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, resourceType);
		selector.setProperty(SelectorHelper.URI, URI);
		Content<String> content = informationStore.getContent(selector);

		return content;
	}

	// ---------------------------------------------------------------------------

	@Override
	public Content<String> getAnnotatedContent(String URI, String resourceType) {
		CassandraInformationStore informationStore = (CassandraInformationStore) this.core
				.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, resourceType);
		selector.setProperty(SelectorHelper.URI, URI);
		Content<String> content = informationStore
				.getAnnotatedContent(selector);
		return content;
	}

	// ---------------------------------------------------------------------------

	@Override
	public void setContent(String URI, String resourceType,
			Content<String> content) {
		CassandraInformationStore informationStore = (CassandraInformationStore) this.core
				.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, resourceType);
		selector.setProperty(SelectorHelper.URI, URI);
		informationStore
				.setContent(selector, content);

	}

	// ---------------------------------------------------------------------------

	@Override
	public void setAnnotatedContent(String URI, String resourceType,
			Content<String> annotatedContent) {
		CassandraInformationStore informationStore = (CassandraInformationStore) this.core
				.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, resourceType);
		selector.setProperty(SelectorHelper.URI, URI);
		informationStore
				.setAnnotatedContent(selector, annotatedContent);

	}

}
