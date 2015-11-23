package org.epnoi.model.modules;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.Selector;
import org.epnoi.model.exceptions.EpnoiInitializationException;

import java.util.Collection;
import java.util.List;



public interface InformationHandler {

	public void put(Resource resource, Context context);

	public Resource get(String URI);

	public Resource get(String URI, String resourceType);

	public void remove(Resource resource);

	public void remove(String URI, String resourceType);

	public void init() throws EpnoiInitializationException;

	public void update(Resource resource);

	public void publish(String eventType, Resource source);
	
	public boolean contains(String URI, String resourceType);

	public void subscribe(InformationAccessListener listener,
			String subscriptionExpression);
	
	public Content<String> getContent(Selector selector);
	
	public Content<Object> getAnnotatedContent(Selector selector);

	public void  setContent(Selector selector, Content<String> content);
	
	public void  setAnnotatedContent(Selector selector, Content<Object> annotatedContent);

	public List<String> getAll(String resourceClass);
	Collection<InformationStore> getInformationStores();

	List<InformationStore> getInformationStoresByType(String type);

	boolean checkStatus(String informationStoreURI);
	
}
