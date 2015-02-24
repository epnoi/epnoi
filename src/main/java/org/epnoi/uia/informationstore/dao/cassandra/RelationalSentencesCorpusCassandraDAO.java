package org.epnoi.uia.informationstore.dao.cassandra;

import gate.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.model.Resource;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.exceptions.EpnoiInitializationException;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.learner.nlp.TermCandidatesFinder;
import org.epnoi.uia.learner.relations.RelationalSentence;
import org.epnoi.uia.learner.relations.RelationalSentenceHelper;
import org.epnoi.uia.learner.relations.RelationalSentencesCorpus;
import org.epnoi.uia.learner.relations.lexical.LexicalRelationalPatternGenerator;

public class RelationalSentencesCorpusCassandraDAO extends CassandraDAO {
	private static final Pattern pattern = Pattern.compile("\\[[^\\]]*\\]");
	public static final String annotatedSentenceSeparator = "<annotatedContent>";
	private static final int annotatedSentenceSeparatorLength = annotatedSentenceSeparator
			.length();

	// --------------------------------------------------------------------------------

	public void remove(String URI) {
		super.deleteRow(URI,
				RelationalSentencesCorpusCassandraHelper.COLUMN_FAMILLY);

	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {

		RelationalSentencesCorpus relationalSentencesCorpus = (RelationalSentencesCorpus) resource;

		Map<String, String> pairsOfNameValues = new HashMap<String, String>();

		super.createRow(relationalSentencesCorpus.getURI(),
				RelationalSentencesCorpusCassandraHelper.COLUMN_FAMILLY);

		pairsOfNameValues.put(
				RelationalSentencesCorpusCassandraHelper.DESCRIPTION,
				relationalSentencesCorpus.getDescription());

		pairsOfNameValues.put(RelationalSentencesCorpusCassandraHelper.TYPE,
				relationalSentencesCorpus.getType());

		for (RelationalSentence relationalSentence : relationalSentencesCorpus
				.getSentences()) {

			pairsOfNameValues
					.put(_createRelationalSentenceRepresentation(relationalSentence),
							RelationalSentencesCorpusCassandraHelper.SENTENCE);
		}

		super.updateColumns(relationalSentencesCorpus.getURI(),
				pairsOfNameValues,
				RelationalSentencesCorpusCassandraHelper.COLUMN_FAMILLY);
		pairsOfNameValues.clear();
		pairsOfNameValues = null;

	}

	// --------------------------------------------------------------------------------

	public String _createRelationalSentenceRepresentation(
			RelationalSentence relationalSentence) {
		String relationalSentenceRepresentation = "["
				+ relationalSentence.getSource().getStart()
				+ ","
				+ relationalSentence.getSource().getEnd()
				+ "]"
				+ "["
				+ relationalSentence.getTarget().getStart()
				+ ","
				+ relationalSentence.getTarget().getEnd()
				+ "]"
				+ relationalSentence.getSentence()
				+ RelationalSentencesCorpusCassandraDAO.annotatedSentenceSeparator
				+ relationalSentence.getAnnotatedSentence();

		return relationalSentenceRepresentation;
	}

	// --------------------------------------------------------------------------------

	public RelationalSentence _readRelationalSentenceRepresentation(
			String relationalSentenceRepresentation) {

		int commaOffset = relationalSentenceRepresentation.indexOf(",");
		String sourceStart = relationalSentenceRepresentation.substring(1,
				commaOffset);
		int bracketOffset = relationalSentenceRepresentation.indexOf("]");

		String sourceEnd = relationalSentenceRepresentation.substring(
				commaOffset + 1, bracketOffset);

		bracketOffset = relationalSentenceRepresentation.indexOf("[",
				commaOffset);
		int secondCommaOffset = relationalSentenceRepresentation.indexOf(",",
				bracketOffset);
		String targetStart = relationalSentenceRepresentation.substring(
				bracketOffset + 1, secondCommaOffset);
		bracketOffset = relationalSentenceRepresentation.indexOf("]",
				secondCommaOffset);

		String targetEnd = relationalSentenceRepresentation.substring(
				secondCommaOffset + 1, bracketOffset);

		int annotatedContnetIndex = relationalSentenceRepresentation
				.indexOf(RelationalSentencesCorpusCassandraDAO.annotatedSentenceSeparator);
		/*
		 * System.out.println("the index " + annotatedContnetIndex +
		 * " and the length " + relationalSentenceRepresentation);
		 */
		String sentence = relationalSentenceRepresentation.substring(
				bracketOffset + 1, annotatedContnetIndex);

		String annotatedSentence = relationalSentenceRepresentation
				.substring(
						annotatedContnetIndex
								+ RelationalSentencesCorpusCassandraDAO.annotatedSentenceSeparatorLength,
						relationalSentenceRepresentation.length());

		OffsetRangeSelector originSelector = new OffsetRangeSelector(
				Long.parseLong(sourceStart), Long.parseLong(sourceEnd));
		OffsetRangeSelector targetSelector = new OffsetRangeSelector(
				Long.parseLong(targetStart), Long.parseLong(targetEnd));
		return new RelationalSentence(originSelector, targetSelector, sentence,
				annotatedSentence);
	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {

		return null;
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {

		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI,
						RelationalSentencesCorpusCassandraHelper.COLUMN_FAMILLY);

		if (columnsIterator.hasNext()) {
			RelationalSentencesCorpus relationalSentencesCorpus = new RelationalSentencesCorpus();
			relationalSentencesCorpus.setURI(URI);
			while (columnsIterator.hasNext()) {
				HColumn<String, String> column = columnsIterator.next();

				if (RelationalSentencesCorpusCassandraHelper.DESCRIPTION
						.equals(column.getName())) {
					relationalSentencesCorpus.setDescription(column.getValue());

				} else if (RelationalSentencesCorpusCassandraHelper.TYPE
						.equals(column.getName())) {
					relationalSentencesCorpus.setType(column.getValue());

				} else if (RelationalSentencesCorpusCassandraHelper.SENTENCE
						.equals(column.getValue())) {
					// System.out.println("cNAME > " + column.getName());
					RelationalSentence relationalSentenes = _readRelationalSentenceRepresentation(column
							.getName());

					relationalSentencesCorpus.getSentences().add(
							relationalSentenes);

				}
			}

			return relationalSentencesCorpus;
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

		Core core = CoreUtility.getUIACore();
		TermCandidatesFinder termCandidatesFinder = new TermCandidatesFinder();
		termCandidatesFinder.init();

		String relationalSentenceURI = "http://thetestcorpus/drinventor";
		RelationalSentencesCorpus relationalSentencesCorpus = new RelationalSentencesCorpus();
		relationalSentencesCorpus.setDescription("The test corpus");
		relationalSentencesCorpus.setURI(relationalSentenceURI);
		relationalSentencesCorpus.setType(RelationalSentenceHelper.HYPERNYM);

		Document annotatedContent = termCandidatesFinder
				.findTermCandidates("A dog is a canine");
		RelationalSentence relationalSentence = new RelationalSentence(
				new OffsetRangeSelector(0L, 5L), new OffsetRangeSelector(10L,
						15L), "A dog is a canine", annotatedContent.toXml());

		RelationalSentencesCorpusCassandraDAO relationalSentencesCorpusCassandraDAO = new RelationalSentencesCorpusCassandraDAO();
		relationalSentencesCorpusCassandraDAO.init();
		relationalSentencesCorpusCassandraDAO.remove(relationalSentenceURI);

		/*
		 * System.out.println(relationalSentencesCorpusCassandraDAO
		 * ._createRelationalSentenceRepresentation(relationalSentence));
		 */
		annotatedContent = termCandidatesFinder
				.findTermCandidates("A dog, is a canine (and other things!)");
		RelationalSentence rs = relationalSentencesCorpusCassandraDAO
				._readRelationalSentenceRepresentation("[2,5][12,18]A dog, is a canine (and other things!)"
						+ RelationalSentencesCorpusCassandraDAO.annotatedSentenceSeparator
						+ annotatedContent.toXml());

		/*
		 * System.out.println("----> " + rs);
		 * 
		 * 
		 * annotatedContent = termCandidatesFinder
		 * .findTermCandidates("This is another relational sentence");
		 * 
		 * 
		 * 
		 * String representation =
		 * "[4444,555][66,7]This is another relational sentence" +
		 * RelationalSentencesCorpusCassandraDAO.annotatedSentenceSeparator +
		 * "more annotatedcontent";
		 * 
		 * rs = relationalSentencesCorpusCassandraDAO
		 * ._readRelationalSentenceRepresentation(representation);
		 * System.out.println("----> " + rs);
		 * 
		 * System.out.println("Are the same? " +
		 * representation.equals(relationalSentencesCorpusCassandraDAO
		 * ._createRelationalSentenceRepresentation(rs)));
		 */
		relationalSentencesCorpus.getSentences().add(relationalSentence);

		relationalSentencesCorpus.getSentences().add(rs);

		relationalSentencesCorpusCassandraDAO.create(relationalSentencesCorpus,
				Context.getEmptyContext());

		RelationalSentencesCorpus readedCorpus = (RelationalSentencesCorpus) relationalSentencesCorpusCassandraDAO
				.read(relationalSentencesCorpus.getURI());
		/*
		 * System.out.println("The readed relational sentence corpus " +
		 * readedCorpus);
		 */
		/*
		 * System.out.println("lo leido " + GateUtils.deserializeGATEDocument(
		 * readedCorpus.getSentences().get(1) .getAnnotatedSentence()).toXml());
		 */

		LexicalRelationalPatternGenerator lexicalRelationalPatternGenerator = new LexicalRelationalPatternGenerator();
		// try {
		// lexicalRelationalPatternGenerator.init(core);
		System.out.println("generated pattern > "
				+ lexicalRelationalPatternGenerator.generate(readedCorpus
						.getSentences().get(1)));
		// } catch (EpnoiInitializationException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		/*
		 * relationalSentencesCorpusCassandraDAO.init();
		 */
		/*
		 * System.out.println(" --------------------------------------------");
		 * 
		 * WikipediaPage wikipediaPage = new WikipediaPage();
		 * wikipediaPage.setURI("http://externalresourceuri");
		 * wikipediaPage.setTerm("Proof Term");
		 * wikipediaPage.setTermDefinition("Proof Term is whatever bla bla bla"
		 * ); wikipediaPage.setSections(Arrays.asList("first", "middle section",
		 * "references")); wikipediaPage.setSectionsContent(new HashMap<String,
		 * String>()); wikipediaPage.getSectionsContent().put("first",
		 * "This is the content of the first section");
		 * wikipediaPage.getSectionsContent().put("middle section",
		 * "This is the content of the middle section");
		 * wikipediaPage.getSectionsContent().put("references",
		 * "This is the content for the references");
		 * 
		 * wikipediaPageCassandraDAO.create(wikipediaPage,
		 * Context.getEmptyContext());
		 * 
		 * System.out .println(
		 * "Reading the wikipedia page-------------------------------------------"
		 * ); System.out.println(" >> " +
		 * wikipediaPageCassandraDAO.read("http://externalresourceuri"));
		 * 
		 * WikipediaPage page = (WikipediaPage) wikipediaPageCassandraDAO
		 * .read("http://en.wikipedia.org/wiki/Glossary_of_American_football");
		 * 
		 * System.out.println("page> " + page);
		 * 
		 * for (String content : page.getSections()) { System.out .println(
		 * "-----------------------------------------------------------------");
		 * System.out.println("---> " + content); System.out .println("---> " +
		 * page.getSectionsContent().get(content)); }
		 */
	}

	// --------------------------------------------------------------------------------

	@Override
	public boolean exists(Selector selector) {
		String URI = selector.getProperty(SelectorHelper.URI);

		String content = super.readColumn(
				selector.getProperty(SelectorHelper.URI), URI,
				RelationalSentencesCorpusCassandraHelper.COLUMN_FAMILLY);

		return (content != null && content.length() > 5);
	}
}
