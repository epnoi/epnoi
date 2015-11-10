package org.epnoi.uia.informationstore.dao.cassandra;

import com.google.common.collect.Lists;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;
import org.epnoi.model.*;

import java.util.ArrayList;

public class TermCassandraDAO extends CassandraDAO {

	// --------------------------------------------------------------------------------

	public void remove(String URI) {
		super.deleteRow(URI, TermCassandraHelper.COLUMN_FAMILY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {

		Term term = (Term) resource;

		TermMetadata termMetadata = term.getAnnotatedTerm().getAnnotation();

		// System.out.println("meto> " + term);
		super.createRow(term.getUri(), TermCassandraHelper.COLUMN_FAMILY);

		super.updateColumn(term.getUri(), TermCassandraHelper.WORD, term
				.getAnnotatedTerm().getWord(),
				TermCassandraHelper.COLUMN_FAMILY);

		super.updateColumn(term.getUri(), TermCassandraHelper.LENGTH,
				String.valueOf(termMetadata.getLength()),
				TermCassandraHelper.COLUMN_FAMILY);
		// System.out.println(">>> " + term.getAnnotatedTerm().getAnnotation());

		for (String word : term.getAnnotatedTerm().getAnnotation().getWords()) {

			super.updateColumn(term.getUri(), word, TermCassandraHelper.WORDS,
					TermCassandraHelper.COLUMN_FAMILY);
		}

		super.updateColumn(term.getUri(), TermCassandraHelper.OCURRENCES,
				String.valueOf(termMetadata.getOcurrences()),
				TermCassandraHelper.COLUMN_FAMILY);

		super.updateColumn(term.getUri(),
				TermCassandraHelper.OCURRENCES_AS_SUBTERM,
				String.valueOf(termMetadata.getOcurrencesAsSubterm()),
				TermCassandraHelper.COLUMN_FAMILY);

		super.updateColumn(term.getUri(),
				TermCassandraHelper.NUMBER_OF_SUPERTERMS,
				String.valueOf(termMetadata.getNumberOfSuperterns()),
				TermCassandraHelper.COLUMN_FAMILY);

		super.updateColumn(term.getUri(), TermCassandraHelper.CVALUE,
				String.valueOf(termMetadata.getCValue()),
				TermCassandraHelper.COLUMN_FAMILY);

		super.updateColumn(term.getUri(), TermCassandraHelper.DOMAIN_CONSENSUS,
				String.valueOf(termMetadata.getDomainConsensus()),
				TermCassandraHelper.COLUMN_FAMILY);

		super.updateColumn(term.getUri(),
				TermCassandraHelper.DOMAIN_PERTINENCE,
				String.valueOf(termMetadata.getDomainPertinence()),
				TermCassandraHelper.COLUMN_FAMILY);

		super.updateColumn(term.getUri(), TermCassandraHelper.TERMHOOD,
				String.valueOf(termMetadata.getTermhood()),
				TermCassandraHelper.COLUMN_FAMILY);

		super.updateColumn(term.getUri(), TermCassandraHelper.TERM_PROBABILITY,
				String.valueOf(termMetadata.getTermProbability()),
				TermCassandraHelper.COLUMN_FAMILY);

		
	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {

		return new ExternalResource();
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {


		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI, TermCassandraHelper.COLUMN_FAMILY);
		//System.out.println("------------------------------READING " + URI);
		if (columnsIterator.hasNext()) {
			Term term = new Term();
			TermMetadata termMetadata = term.getAnnotatedTerm().getAnnotation();

			term.setUri(URI);
			ArrayList<String> words = new ArrayList<String>();

			while (columnsIterator.hasNext()) {
				HColumn<String, String> column = columnsIterator.next();

				String columnName = column.getName();
				String columnValue = column.getValue();
				switch (columnName) {
				case TermCassandraHelper.WORD:
					term.getAnnotatedTerm().setWord(columnValue);
					break;

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

				case TermCassandraHelper.TERM_PROBABILITY:
					termMetadata.setTermProbability(Double
							.parseDouble(columnValue));
					break;

				default:
					if (TermCassandraHelper.WORDS.equals(columnValue)) {
						words.add(0, columnName);
					}
					break;
				}
			}
			Lists.reverse(words);
			termMetadata.setWords((String[]) words.toArray(new String[words
					.size()]));
			return term;

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
/*
 
 FOR TEST
	public static void main(String[] args) {
		Core core = CoreUtility.getUIACore();
		Term term = new Term();
		term.setURI("lauri");

		term.getAnnotatedTerm().setAnnotation(new TermMetadata());
		term.getAnnotatedTerm().getAnnotation().setLength(4);
		term.getAnnotatedTerm().getAnnotation()
				.setWords(new String[] { "mi", "mama", "me", "mima" });
		core.getInformationHandler().put(term, Context.getEmptyContext());

		System.out.println("-------> "
				+ core.getInformationHandler().get("lauri"));
	}
*/
	@Override
	public boolean exists(Selector selector) {
		// TODO Auto-generated method stub
		return false;
	}
}
