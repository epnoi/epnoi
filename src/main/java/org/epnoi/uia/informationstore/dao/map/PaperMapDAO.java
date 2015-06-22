package org.epnoi.uia.informationstore.dao.map;

import org.epnoi.model.Content;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;

import com.rits.cloning.Cloner;

public class PaperMapDAO extends MapDAO {
	private static Cloner cloner = new Cloner();

	// --------------------------------------------------------------------------------

	@Override
	public Content<Object> getAnnotatedContent(Selector selector) {

		Content<Object> annotatedContent = (Content<Object>) map.get(selector
				.getProperty(SelectorHelper.ANNOTATED_CONTENT_URI));

		return annotatedContent;
	}

	// --------------------------------------------------------------------------------
	
	@Override
	public void setAnnotatedContent(Selector selector,
			Content<Object> annotatedContent) {
		Content<Object> clonedAnnotatedContent = cloner
				.deepClone(annotatedContent);
		
		map.put(selector.getProperty(SelectorHelper.ANNOTATED_CONTENT_URI),
				clonedAnnotatedContent);
		database.commit();

	}
}
