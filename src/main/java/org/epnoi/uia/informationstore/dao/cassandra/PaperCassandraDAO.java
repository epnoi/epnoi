package org.epnoi.uia.informationstore.dao.cassandra;

import gate.Document;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;

import org.epnoi.model.Content;
import org.epnoi.model.ContentHelper;
import org.epnoi.model.Context;
import org.epnoi.model.ExternalResource;
import org.epnoi.model.Paper;
import org.epnoi.model.Resource;
import org.epnoi.model.Search;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;

public class PaperCassandraDAO extends CassandraDAO {

	public void remove(String URI) {
		super.deleteRow(URI, PaperCassandraHelper.COLUMN_FAMILLY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		Paper paper = (Paper) resource;
		super.createRow(paper.getURI(), PaperCassandraHelper.COLUMN_FAMILLY);

		if (paper.getDescription() != null) {

			super.updateColumn(paper.getURI(),
					PaperCassandraHelper.DESCRIPTION, paper.getDescription(),
					PaperCassandraHelper.COLUMN_FAMILLY);

		}
		if (paper.getDescription() != null) {

			super.updateColumn(paper.getURI(), PaperCassandraHelper.TITLE,
					paper.getTitle(), PaperCassandraHelper.COLUMN_FAMILLY);

		}

		for (String author : paper.getAuthors()) {

			super.updateColumn(paper.getURI(), author,
					PaperCassandraHelper.AUTHORS,
					PaperCassandraHelper.COLUMN_FAMILLY);
		}

		if (context.getElements().get(Context.ANNOTATED_CONTENT) != null) {

			// gate.corpora.DocumentStaxUtils.readGateXmlDocument(xsr, doc);
			Document annotatedContent = (Document) context.getElements().get(
					Context.ANNOTATED_CONTENT);

			super.updateColumn(paper.getURI(),
					AnnotatedContentCassandraHelper.CONTENT,
					annotatedContent.toXml(),
					AnnotatedContentCassandraHelper.COLUMN_FAMILLY);
		}

		String content = paper.getTitle() + "." + paper.getDescription();
		// System.out.println("content:> " + content);
		super.updateColumn(paper.getURI(), ContentCassandraHelper.CONTENT,
				content, ContentCassandraHelper.COLUMN_FAMILLY);

	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {

		return new ExternalResource();
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {
		System.out.println("----> > " + URI);
		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI, PaperCassandraHelper.COLUMN_FAMILLY);
		if (columnsIterator.hasNext()) {
			Paper paper = new Paper();
			paper.setURI(URI);
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

		throw (new RuntimeException(
				"The getContent method of the PaperCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------

	@Override
	public Content<String> getAnnotatedContent(Selector selector) {

		throw (new RuntimeException(
				"The getAnnotatedContent method of the PaperCassandraDAO should not be invoked"));
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

		throw (new RuntimeException(
				"The setAnnotatedContent method of the PaperCassandraDAO should not be invoked"));
	}

	@Override
	public boolean exists(Selector selector) {
		// TODO Auto-generated method stub
		return false;
	}

	// --------------------------------------------------------------------------------

}
