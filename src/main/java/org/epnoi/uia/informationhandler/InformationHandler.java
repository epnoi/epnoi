package org.epnoi.uia.informationhandler;

import org.epnoi.model.Content;
import org.epnoi.model.ContentSummary;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.uia.informationhandler.events.InformationAccessListener;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.parameterization.ParametersModel;



public interface InformationHandler {

	public void put(Resource resource, Context context);

	public Resource get(String URI);

	public Resource get(String URI, String resourceType);

	public void remove(Resource resource);

	public void remove(String URI, String resourceType);

	public void init(ParametersModel parameters);

	public void addInformationStore(InformationStore informationStore);

	public void removeInformationStore(String URI);

	public void update(Resource resource);

	public void publish(String eventType, Resource source);
	
	public boolean contains(String URI, String resourceType);

	public void subscribe(InformationAccessListener listener,
			String subscriptionExpression);
	
	public Content<String> getContent(Selector selector);
	
	public Content<String> getAnnotatedContent(Selector selector);

	public void  setContent(Selector selector, Content<String> content);
	
	public void  setAnnotatedContent(Selector selector, Content<String> annotatedContent);

	
}
