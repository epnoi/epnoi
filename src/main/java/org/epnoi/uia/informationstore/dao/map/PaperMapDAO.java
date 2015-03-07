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
	public Content<String> getAnnotatedContent(Selector selector) {

		String annotatedContent = map.get(selector
				.getProperty(SelectorHelper.ANNOTATED_CONTENT_URI));
		if (annotatedContent != null) {

			Matcher matcher = pattern.matcher(annotatedContent);

			if (matcher.find()) {
				String type = annotatedContent.subSequence(matcher.start() + 1,
						matcher.end() - 1).toString();

				String content = annotatedContent.subSequence(matcher.end(),
						annotatedContent.length()).toString();
				return new Content<>(content, type);
			}
		}
		return null;
	}

	// --------------------------------------------------------------------------------

	@Override
	public void setAnnotatedContent(Selector selector,
			Content<String> annotatedContent) {

		String annotatedContentSerialized = "[" + annotatedContent.getType()
				+ "]" + annotatedContent.getContent();

		map.put(selector.getProperty(SelectorHelper.ANNOTATED_CONTENT_URI),
				annotatedContentSerialized);
		database.commit();

	}
	// --------------------------------------------------------------------------------

}
