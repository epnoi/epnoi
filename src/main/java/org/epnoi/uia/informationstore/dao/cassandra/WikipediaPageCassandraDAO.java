package org.epnoi.uia.informationstore.dao.cassandra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.WikipediaPage;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;

public class WikipediaPageCassandraDAO extends CassandraDAO {
	private static final Pattern pattern = Pattern.compile("\\[[^\\]]*\\]");

	public void remove(String URI) {
		super.deleteRow(URI, WikipediaPageCassandraHelper.COLUMN_FAMILLY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {

		WikipediaPage wikipediaPage = (WikipediaPage) resource;

		Map<String, String> pairsOfNameValues = new HashMap<String, String>();

		super.createRow(wikipediaPage.getURI(),
				WikipediaPageCassandraHelper.COLUMN_FAMILLY);

		pairsOfNameValues.put(WikipediaPageCassandraHelper.TERM,
				wikipediaPage.getTerm());
		/*
		 * super.updateColumn(wikipediaPage.getURI(),
		 * WikipediaPageCassandraHelper.TERM, wikipediaPage.getTerm(),
		 * WikipediaPageCassandraHelper.COLUMN_FAMILLY);
		 */

		String termDefinition = (wikipediaPage.getTermDefinition() == null) ? ""
				: wikipediaPage.getTermDefinition();
		// System.out.println("----------------------------->" +
		// termDefinition);

		pairsOfNameValues.put(WikipediaPageCassandraHelper.TERM_DEFINITION,
				termDefinition);
		/*
		 * super.updateColumn(wikipediaPage.getURI(),
		 * WikipediaPageCassandraHelper.TERM_DEFINITION, termDefinition,
		 * WikipediaPageCassandraHelper.COLUMN_FAMILLY);
		 */

		/*
		 * REPLICADO for (String section : wikipediaPage.getSections()) {
		 * 
		 * super.updateColumn(wikipediaPage.getURI(), section,
		 * WikipediaPageCassandraHelper.SECTION,
		 * WikipediaPageCassandraHelper.COLUMN_FAMILLY);
		 * 
		 * }
		 */

		String sectionContent;

		for (String section : wikipediaPage.getSections()) {

			sectionContent = wikipediaPage.getSectionsContent().get(section);
			pairsOfNameValues.put("[" + section + "]" + sectionContent,
					WikipediaPageCassandraHelper.SECTION_CONTENT);

			/*
			 * super.updateColumn(wikipediaPage.getURI(), "[" + section + "]" +
			 * sectionContent, WikipediaPageCassandraHelper.SECTION_CONTENT,
			 * WikipediaPageCassandraHelper.COLUMN_FAMILLY);
			 */
		}
		sectionContent = null;

		for (Entry<String, Object> contextElement : context.getElements()
				.entrySet()) {
			String annotatedContent = (contextElement.getValue() == null) ? ""
					: "[" + AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE
							+ "]" + contextElement.getValue();
			// System.out.println("------- > "+annotatedContent);
			pairsOfNameValues.put(contextElement.getKey(), annotatedContent);

			/*
			 * super.updateColumn(wikipediaPage.getURI(),
			 * contextElement.getKey(), "[" +
			 * AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE + "]" +
			 * ((Document) contextElement.getValue()).toXml() .toString(),
			 * WikipediaPageCassandraHelper.COLUMN_FAMILLY);
			 */
		}

		super.updateColumns(wikipediaPage.getURI(), pairsOfNameValues,
				WikipediaPageCassandraHelper.COLUMN_FAMILLY);
		pairsOfNameValues.clear();
		pairsOfNameValues = null;

	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {

		return null;
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {

		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI,
						WikipediaPageCassandraHelper.COLUMN_FAMILLY);
		List<String> sectionsContent = new ArrayList<String>();
		if (columnsIterator.hasNext()) {
			WikipediaPage paper = new WikipediaPage();
			paper.setURI(URI);
			while (columnsIterator.hasNext()) {
				HColumn<String, String> column = columnsIterator.next();

				/*
				 * System.out.println("(" + column.getName() + "|" +
				 * column.getValue() + ")");
				 */
				if (WikipediaPageCassandraHelper.TERM.equals(column.getName())) {
					paper.setTerm(column.getValue());

				} else if (WikipediaPageCassandraHelper.TERM_DEFINITION
						.equals(column.getName())) {
					paper.setTermDefinition(column.getValue());

				} else if (WikipediaPageCassandraHelper.SECTION.equals(column
						.getValue())) {
					paper.getSections().add(column.getName());
				} else if (WikipediaPageCassandraHelper.SECTION_CONTENT
						.equals(column.getValue())) {

					String columnName = column.getName();
					// System.out.println("column name > " + columnName);
					Matcher matcher = pattern.matcher(column.getName());

					if (matcher.find()) {
						String section = columnName.subSequence(
								matcher.start() + 1, matcher.end() - 1)
								.toString();

						String content = columnName.subSequence(matcher.end(),
								columnName.length()).toString();

						// System.out.println("(" +section + ")= " + content);
						paper.getSections().add(section);
						paper.getSectionsContent().put(section, content);
					}

				}
			}

			return paper;
		}

		return null;
	}

	// --------------------------------------------------------------------------------

	@Override
	public Content<String> getContent(Selector selector) {

		throw (new RuntimeException(
				"The getContent method of the WikipediaPageCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------

	@Override
	public Content<String> getAnnotatedContent(Selector selector) {
		// System.out.println("annotatedContent > " + selector);
		String annotatedContent = super.readColumn(
				selector.getProperty(SelectorHelper.URI),
				selector.getProperty(SelectorHelper.ANNOTATED_CONTENT_URI),
				WikipediaPageCassandraHelper.COLUMN_FAMILLY);

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
	public void setContent(Selector selector, Content<String> content) {

		throw (new RuntimeException(
				"The setContent method of the WikipediaPageCassandraDAO should not be invoked"));
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
				AnnotatedContentCassandraHelper.COLUMN_FAMILLY);

	}

	// --------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out.println("WikipediaPage Cassandra Test--------------");
		System.out
				.println("Initialization --------------------------------------------");
		WikipediaPageCassandraDAO wikipediaPageCassandraDAO = new WikipediaPageCassandraDAO();

		wikipediaPageCassandraDAO.init();

		System.out.println(" --------------------------------------------");

		WikipediaPage wikipediaPage = new WikipediaPage();
		wikipediaPage.setURI("http://externalresourceuri");
		wikipediaPage.setTerm("Proof Term");
		wikipediaPage.setTermDefinition("Proof Term is whatever bla bla bla");
		wikipediaPage.setSections(Arrays.asList("first", "middle section",
				"references"));
		wikipediaPage.setSectionsContent(new HashMap<String, String>());
		wikipediaPage.getSectionsContent().put("first",
				"This is the content of the first section");
		wikipediaPage.getSectionsContent().put("middle section",
				"This is the content of the middle section");
		wikipediaPage.getSectionsContent().put("references",
				"This is the content for the references");

		wikipediaPageCassandraDAO.create(wikipediaPage,
				Context.getEmptyContext());

		System.out
				.println("Reading the wikipedia page-------------------------------------------");
		System.out.println(" >> "
				+ wikipediaPageCassandraDAO.read("http://externalresourceuri"));

		WikipediaPage page = (WikipediaPage) wikipediaPageCassandraDAO
				.read("http://en.wikipedia.org/wiki/Glossary_of_American_football");

		System.out.println("page> " + page);

		for (String content : page.getSections()) {
			System.out
					.println("-----------------------------------------------------------------");
			System.out.println("---> " + content);
			System.out
					.println("---> " + page.getSectionsContent().get(content));
		}

	}

	// --------------------------------------------------------------------------------

	@Override
	public boolean exists(Selector selector) {
		String annotationURI = selector.getProperty(SelectorHelper.URI)
				+ "/first/" + AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE;
		
		String annotatedContent = super.readColumn(
				selector.getProperty(SelectorHelper.URI), annotationURI,
				WikipediaPageCassandraHelper.COLUMN_FAMILLY);

		return (annotatedContent != null && annotatedContent.length() > 5);
	}
}
