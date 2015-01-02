package org.epnoi.uia.informationstore.dao.cassandra;

import java.util.ArrayList;

import gate.Document;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.ExternalResource;
import org.epnoi.model.Paper;
import org.epnoi.model.Resource;
import org.epnoi.model.Term;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.learner.terms.AnnotatedWord;
import org.epnoi.uia.learner.terms.TermMetadata;

public class TermCassandraDAO extends CassandraDAO {

	// --------------------------------------------------------------------------------

	public void remove(String URI) {
		super.deleteRow(URI, TermCassandraHelper.COLUMN_FAMILLY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {

		Term term = (Term) resource;

		TermMetadata termMetadata = term.getAnnotatedTerm().getAnnotation();

		super.createRow(term.getURI(), TermCassandraHelper.COLUMN_FAMILLY);

		super.updateColumn(term.getURI(), TermCassandraHelper.LENGTH,
				String.valueOf(termMetadata.getLength()),
				TermCassandraHelper.COLUMN_FAMILLY);

		for (String word : term.getAnnotatedTerm().getAnnotation().getWords()) {

			super.updateColumn(term.getURI(), word, TermCassandraHelper.WORDS,
					TermCassandraHelper.COLUMN_FAMILLY);
		}

		super.updateColumn(term.getURI(), TermCassandraHelper.OCURRENCES,
				String.valueOf(termMetadata.getOcurrences()),
				TermCassandraHelper.COLUMN_FAMILLY);

		super.updateColumn(term.getURI(),
				TermCassandraHelper.OCURRENCES_AS_SUBTERM,
				String.valueOf(termMetadata.getOcurrencesAsSubterm()),
				TermCassandraHelper.COLUMN_FAMILLY);

		super.updateColumn(term.getURI(),
				TermCassandraHelper.NUMBER_OF_SUPERTERMS,
				String.valueOf(termMetadata.getNumberOfSuperterns()),
				TermCassandraHelper.COLUMN_FAMILLY);

		super.updateColumn(term.getURI(), TermCassandraHelper.CVALUE,
				String.valueOf(termMetadata.getCValue()),
				TermCassandraHelper.COLUMN_FAMILLY);

		super.updateColumn(term.getURI(), TermCassandraHelper.DOMAIN_CONSENSUS,
				String.valueOf(termMetadata.getDomainConsensus()),
				TermCassandraHelper.COLUMN_FAMILLY);

		super.updateColumn(term.getURI(),
				TermCassandraHelper.DOMAIN_PERTINENCE,
				String.valueOf(termMetadata.getDomainPertinence()),
				TermCassandraHelper.COLUMN_FAMILLY);

		super.updateColumn(term.getURI(), TermCassandraHelper.TERMHOOD,
				String.valueOf(termMetadata.getTermhood()),
				TermCassandraHelper.COLUMN_FAMILLY);

	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {

		return new ExternalResource();
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {

		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI, TermCassandraHelper.COLUMN_FAMILLY);

		if (columnsIterator.hasNext()) {
			Term term = new Term();
			TermMetadata termMetadata = term.getAnnotatedTerm().getAnnotation();

			term.setURI(URI);
			ArrayList<String> words = new ArrayList<String>();

			while (columnsIterator.hasNext()) {
				HColumn<String, String> column = columnsIterator.next();

				String columnName = column.getName();
				String columnValue = column.getValue();
				switch (columnName) {
				case TermCassandraHelper.LENGTH:
					termMetadata.setLength(Integer.parseInt(columnValue));
					break;
				case TermCassandraHelper.OCURRENCES:
					termMetadata.setOcurrences(Long.parseLong(columnValue));
					break;

				case TermCassandraHelper.OCURRENCES_AS_SUBTERM:
					termMetadata.setOcurrencesAsSubterm(Long
							.parseLong(columnValue));
					break;

				case TermCassandraHelper.NUMBER_OF_SUPERTERMS:

					termMetadata.setNumberOfSuperterns(Long
							.parseLong(columnValue));
					break;

				case TermCassandraHelper.CVALUE:
					termMetadata.setCValue(Double.parseDouble(columnValue));
					break;
				case TermCassandraHelper.DOMAIN_CONSENSUS:
					termMetadata.setDomainConsensus(Double
							.parseDouble(columnValue));
					break;
				case TermCassandraHelper.DOMAIN_PERTINENCE:
					termMetadata.setDomainPertinence(Double
							.parseDouble(columnValue));
					break;
				case TermCassandraHelper.TERMHOOD:
					termMetadata.setTermhood(Double.parseDouble(columnValue));
					break;

				default:
					if (TermCassandraHelper.WORDS.equals(columnValue)) {
						words.add(columnName);
					}

				}
				termMetadata.setWords((String[]) words.toArray());
				return term;
			}
		}

		return null;

	}

	// --------------------------------------------------------------------------------

	@Override
	public Content<String> getContent(Selector selector) {

		throw (new RuntimeException(
				"The getContent method of the TermCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------

	@Override
	public Content<String> getAnnotatedContent(Selector selector) {

		throw (new RuntimeException(
				"The getAnnotatedContent method of the TermCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------

	@Override
	public void setContent(Selector selector, Content<String> content) {

		throw (new RuntimeException(
				"The setContent method of the TermCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------

	@Override
	public void setAnnotatedContent(Selector selector,
			Content<String> annotatedContent) {

		throw (new RuntimeException(
				"The setAnnotatedContent method of the TermCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------

}
