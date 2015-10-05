package org.epnoi.uia.informationstore.dao.map;

import org.epnoi.model.Content;
import org.epnoi.model.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;

public class WikipediaPageMapDAO extends MapDAO {
	
	// --------------------------------------------------------------------------------

	@Override
	public Content<Object> getAnnotatedContent(Selector selector) {

		Content<Object> annotatedContent = (Content<Object>)map.get(selector
				.getProperty(SelectorHelper.ANNOTATED_CONTENT_URI));

		return annotatedContent;
	}

	// --------------------------------------------------------------------------------
	
	@Override
	public void setAnnotatedContent(Selector selector,
			Content<Object> annotatedContent) {

		
	//System.out.println(">> "+clonedAnnotatedContent);
		map.put(selector.getProperty(SelectorHelper.ANNOTATED_CONTENT_URI),
				annotatedContent);
		// System.out.println(">> "+map.keySet());
		database.commit();

	}
}
