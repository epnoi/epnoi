package org.epnoi.uia.informationstore.dao.cassandra;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;
import org.epnoi.model.*;
import org.epnoi.uia.informationstore.SelectorHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaperCassandraDAO extends CassandraDAO {
	private static final Pattern pattern = Pattern.compile("\\[[^\\]]*\\]");

	public void remove(String URI) {
		super.deleteRow(URI, PaperCassandraHelper.COLUMN_FAMILY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		Paper paper = (Paper) resource;
		super.createRow(paper.getUri(), PaperCassandraHelper.COLUMN_FAMILY);
		Map<String, String> pairsOfNameValues = new HashMap<String, String>();

		if (paper.getDescription() != null) {

			pairsOfNameValues.put(PaperCassandraHelper.DESCRIPTION,
					paper.getDescription());
		
		}
		if (paper.getTitle() != null) {
			pairsOfNameValues.put(PaperCassandraHelper.TITLE, paper.getTitle());

		}

		for (String author : paper.getAuthors()) {
			pairsOfNameValues.put(author, PaperCassandraHelper.AUTHORS);
		}
		
		for (Entry<String, Object> contextElement : context.getElements()
				.entrySet()) {
			String annotatedContent = (contextElement.getValue() == null) ? ""
					: "[" + AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE
							+ "]" + contextElement.getValue();
			// System.out.println("------- > "+annotatedContent);
			pairsOfNameValues.put(contextElement.getKey(), annotatedContent);

		}

		String content = paper.getTitle() + "." + paper.getDescription();
		// System.out.println("content:> " + content);

		pairsOfNameValues.put(PaperCassandraHelper.CONTENT,
				paper.getDescription());

		super.updateColumns(paper.getUri(), pairsOfNameValues,
				PaperCassandraHelper.COLUMN_FAMILY);
		
	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {

		return new ExternalResource();
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {
		System.out.println("----> > " + URI);
		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI, PaperCassandraHelper.COLUMN_FAMILY);
		if (columnsIterator.hasNext()) {
			Paper paper = new Paper();
			paper.setUri(URI);
			while (columnsIterator.hasNext()) {
				HColumn<String, String> column = columnsIterator.next();
				if (PaperCassandraHelper.DESCRIPTION.equals(column.getName())) {
					paper.setDescription(column.getValue());

				} else if (PaperCassandraHelper.TITLE.equals(column.getName())) {
					paper.setTitle(column.getValue());

				} else if (PaperCassandraHelper.AUTHORS.equals(column
						.getValue())) {
					paper.getAuthors().add(column.getName());
				}
			}

			return paper;
		}

		return null;
	}

	// --------------------------------------------------------------------------------

	@Override
	public Content<String> getContent(Selector selector) {

		// System.out.println("annotatedContent > " + selector);
		String annotatedContent = super.readColumn(
				selector.getProperty(SelectorHelper.URI),
				PaperCassandraHelper.CONTENT,
				PaperCassandraHelper.COLUMN_FAMILY);

		if (annotatedContent == null) {// http://en.wikipedia.org/wiki/Glossary_of_American_football
										// bug
			return null;
		}
		Matcher matcher = pattern.matcher(annotatedContent);

		if (matcher.find()) {
			String type = annotatedContent.subSequence(matcher.start() + 1,
					matcher.end() - 1).toString();

			String content = annotatedContent.subSequence(matcher.end(),
					annotatedContent.length()).toString();
			return new Content<>(content, type);

		}
		return null;
	}

	// --------------------------------------------------------------------------------

	@Override
	public Content<String> getAnnotatedContent(Selector selector) {

		String annotatedContent = super.readColumn(
				selector.getProperty(SelectorHelper.URI),
				selector.getProperty(SelectorHelper.ANNOTATED_CONTENT_URI),
				PaperCassandraHelper.COLUMN_FAMILY);
		//System.out.println("annotatedContent > " + selector + "  --> "+ annotatedContent);
		if (annotatedContent == null) {// http://en.wikipedia.org/wiki/Glossary_of_American_football
										// bug
			return new Content<String>(null, null);
		}
		Matcher matcher = pattern.matcher(annotatedContent);

		if (matcher.find()) {
			String type = annotatedContent.subSequence(matcher.start() + 1,
					matcher.end() - 1).toString();

			String content = annotatedContent.subSequence(matcher.end(),
					annotatedContent.length()).toString();
			return new Content<>(content, type);

		}
		return null;
	}

	// --------------------------------------------------------------------------------

	@Override
	public void setContent(Selector selector, Content<String> content) {

		throw (new RuntimeException(
				"The setContent method of the PaperCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------

	@Override
	public void setAnnotatedContent(Selector selector,
			Content<String> annotatedContent) {

		// System.out.println("selector> " + selector);

		super.updateColumn(
				selector.getProperty(SelectorHelper.URI),
				selector.getProperty(SelectorHelper.ANNOTATED_CONTENT_URI),
				"[" + annotatedContent.getType() + "]"
						+ annotatedContent.getContent(),
				PaperCassandraHelper.COLUMN_FAMILY);

	}

	// --------------------------------------------------------------------------------

	@Override
	public boolean exists(Selector selector) {
		// TODO Auto-generated method stub
		return false;
	}

	// --------------------------------------------------------------------------------

}
