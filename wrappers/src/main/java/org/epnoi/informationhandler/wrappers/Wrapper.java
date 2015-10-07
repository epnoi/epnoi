package org.epnoi.informationhandler.wrappers;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.Selector;

public interface Wrapper {

	public void put(Resource resource, Context context);

	public void remove(String URI);

	public void update(Resource resource);

	public Resource get(String URI);

	public boolean exists(String URI);

	public Content<String> getContent(Selector selector);

	public void setContent(Selector selector, Content<String> content);

	public Content<Object> getAnnotatedContent(Selector selector);

	public void setAnnotatedContent(Selector selector,
			Content<Object> annotatedContent);

}
