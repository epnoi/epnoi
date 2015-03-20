package org.epnoi.uia.informationstore.dao.map;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.epnoi.model.Content;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;

public class PaperMapDAO extends MapDAO {

	private static final Pattern pattern = Pattern.compile("\\[[^\\]]*\\]");

	// --------------------------------------------------------------------------------

	@Override
	public Content<Object> getAnnotatedContent(Selector selector) {

		Content annotatedContent = (Content)map.get(selector
				.getProperty(SelectorHelper.ANNOTATED_CONTENT_URI));

		// System.out.println(">> "+map.keySet());
		// System.out.println("_---> "+map.get("file:///epnoi/epnoideployment/firstReviewResources/CGCorpus/A01_S01_A_Powell_Optimization_Approach__for_Example-Based_Skinning__CORPUS__v3.xml/text/xml/gate"));

		if (annotatedContent != null) {

		//	Matcher matcher = pattern.matcher(annotatedContent);

			//if (matcher.find()) {
				//String type = annotatedContent.subSequence(matcher.start() + 1,
					//	matcher.end() - 1).toString();
			String type = annotatedContent.getType();
			/*
				String content = annotatedContent.subSequence(matcher.end(),
						annotatedContent.length()).toString();
				*/
			Object content = annotatedContent.getContent();
				return new Content<Object>(content, type);
			}
		//}
		return null;
	}

	// --------------------------------------------------------------------------------

	@Override
	public void setAnnotatedContent(Selector selector,
			Content<Object> annotatedContent) {
/*
		String annotatedContentSerialized = "[" + annotatedContent.getType()
				+ "]" + annotatedContent.getContent();
		// System.out.println("p>"+selector);
*/
		map.put(selector.getProperty(SelectorHelper.ANNOTATED_CONTENT_URI),
				annotatedContent.getContent());
		// System.out.println(">> "+map.keySet());
		database.commit();

	}
	// --------------------------------------------------------------------------------

}
