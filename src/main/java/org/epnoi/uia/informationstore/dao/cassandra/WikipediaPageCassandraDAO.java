package org.epnoi.uia.informationstore.dao.cassandra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;

import org.apache.commons.lang.ArrayUtils;
import org.epnoi.model.Content;
import org.epnoi.model.ContentHelper;
import org.epnoi.model.Context;
import org.epnoi.model.ExternalResource;
import org.epnoi.model.Resource;
import org.epnoi.model.User;
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
		super.createRow(wikipediaPage.getURI(),
				WikipediaPageCassandraHelper.COLUMN_FAMILLY);

		super.updateColumn(wikipediaPage.getURI(),
				WikipediaPageCassandraHelper.TERM, wikipediaPage.getTerm(),
				WikipediaPageCassandraHelper.COLUMN_FAMILLY);

		String termDefinition = (wikipediaPage.getTermDefinition() == null) ? ""
				: wikipediaPage.getTermDefinition();
		System.out.println("----------------------------->" + termDefinition);
		super.updateColumn(wikipediaPage.getURI(),
				WikipediaPageCassandraHelper.TERM_DEFINITION, termDefinition,
				WikipediaPageCassandraHelper.COLUMN_FAMILLY);

		/*
		 * if (paper.getDescription() != null) {
		 * 
		 * super.updateColumn(paper.getURI(), PaperCassandraHelper.TITLE,
		 * paper.getTitle(), PaperCassandraHelper.COLUMN_FAMILLY);
		 * 
		 * }
		 */

		for (String section : wikipediaPage.getSections()) {

			super.updateColumn(wikipediaPage.getURI(), section,
					WikipediaPageCassandraHelper.SECTION,
					WikipediaPageCassandraHelper.COLUMN_FAMILLY);

		}

		for (String section : wikipediaPage.getSections()) {

			String sectionContent = wikipediaPage.getSectionsContent().get(
					section);
			super.updateColumn(wikipediaPage.getURI(), "[" + section + "]"
					+ sectionContent,
					WikipediaPageCassandraHelper.SECTION_CONTENT,
					WikipediaPageCassandraHelper.COLUMN_FAMILLY);

		}
	}

	/*
	 * if (context.getElements().get(Context.ANNOTATED_CONTENT) != null) {
	 * 
	 * // gate.corpora.DocumentStaxUtils.readGateXmlDocument(xsr, doc); Document
	 * annotatedContent = (Document) context.getElements().get(
	 * Context.ANNOTATED_CONTENT);
	 * 
	 * super.updateColumn(paper.getURI(),
	 * PaperCassandraHelper.ANNOTATED_CONTENT, annotatedContent.toXml(),
	 * PaperCassandraHelper.COLUMN_FAMILLY); }
	 */

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
					/*
					 * String section=column.getName()
					 * 
					 * 
					 * sectionsContent.add(par);
					 */

					String columnName = column.getName();

					Matcher matcher = pattern.matcher(column.getName());

					if (matcher.find()) {
						String section = columnName.subSequence(
								matcher.start() + 1, matcher.end() - 1)
								.toString();

						String content = columnName.subSequence(matcher.end(),
								columnName.length()).toString();

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

		throw (new RuntimeException(
				"The getAnnotatedContent method of the WikipediaPageCassandraDAO should not be invoked"));
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

		throw (new RuntimeException(
				"The setAnnotatedContent method of the WikipediaPageCassandraDAO should not be invoked"));
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

	}
}
