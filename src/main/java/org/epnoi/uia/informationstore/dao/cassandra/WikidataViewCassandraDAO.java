package org.epnoi.uia.informationstore.dao.cassandra;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.Resource;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.relations.knowledgebase.wikidata.WikidataView;

public class WikidataViewCassandraDAO extends CassandraDAO {
	private static final Pattern pattern = Pattern.compile("\\[[^\\]]*\\]");
	public static final String annotatedSentenceSeparator = "<annotatedContent>";
	private static final int annotatedSentenceSeparatorLength = annotatedSentenceSeparator
			.length();

	// --------------------------------------------------------------------------------

	public void remove(String URI) {
		super.deleteRow(URI, WikidataViewCassandraHelper.COLUMN_FAMILLY);

	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {

		WikidataView wikidataView = (WikidataView) resource;

		Map<String, String> pairsOfNameValues = new HashMap<String, String>();

		super.createRow(wikidataView.getURI(),
				WikidataViewCassandraHelper.COLUMN_FAMILLY);
		// Relations mapping
		for (Entry<String, Map<String, Set<String>>> relationsEntry : wikidataView
				.getRelations().entrySet()) {

			String relationType = relationsEntry.getKey();
			for (Entry<String, Set<String>> relationEntry : relationsEntry
					.getValue().entrySet()) {

				String sourceIRI = relationEntry.getKey();
				for (String targetIRI : relationEntry.getValue()) {

					pairsOfNameValues.put(

					_serializeRelation(relationType, sourceIRI, targetIRI),
							WikidataViewCassandraHelper.RELATIONS);

				}

			}
		}
		System.out.println("------------------------------> "
				+ pairsOfNameValues);

		super.updateColumns(wikidataView.getURI(), pairsOfNameValues,
				WikidataViewCassandraHelper.COLUMN_FAMILLY);
		pairsOfNameValues.clear();
		pairsOfNameValues = null;

	}

	// --------------------------------------------------------------------------------

	private String _serializeRelation(String relationType, String sourceIRI,
			String targetIRI) {
		return sourceIRI + ">" + targetIRI + ">" + relationType;
	}

	// --------------------------------------------------------------------------------

	private Relation _deserializeRelation(String relationExpression) {
		System.out.println("Expression >" + relationExpression);
		int offset = relationExpression.indexOf(">");
		String sourceIRI = relationExpression.substring(0, offset);
		System.out.println(">>>>" + sourceIRI);
		int secondOffset = relationExpression.indexOf(">", offset + 1);
		String targetIRI = relationExpression.substring(offset + 1,
				secondOffset);
		String relationType = relationExpression.substring(secondOffset + 1,
				relationExpression.length());
		return new Relation(relationType, sourceIRI, targetIRI);
	}

	// --------------------------------------------------------------------------------

	private class Relation {
		private String relationType;
		private String source;
		private String target;

		public Relation(String relationType, String source, String target) {
			super();
			this.relationType = relationType;
			this.source = source;
			this.target = target;
		}

		// ------------------------------------------------------------------------------

		public String getRelationType() {
			return relationType;
		}

		// ------------------------------------------------------------------------------

		public String getSource() {
			return source;
		}

		// ------------------------------------------------------------------------------

		public String getTarget() {
			return target;
		}

		// ------------------------------------------------------------------------------

		@Override
		public String toString() {
			return "Relation [relationType=" + relationType + ", source="
					+ source + ", target=" + target + "]";
		}

	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {

		return null;
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {

		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI, WikidataViewCassandraHelper.COLUMN_FAMILLY);

		if (columnsIterator.hasNext()) {
			WikidataView wikidataView = new WikidataView();
			wikidataView.setURI(URI);
			while (columnsIterator.hasNext()) {
				HColumn<String, String> column = columnsIterator.next();

				String columnName = column.getName();
				String columnValue = column.getValue();
				if (WikidataViewCassandraHelper.RELATIONS.equals(columnValue)) {
					System.out.println("The readed relation "
							+ _deserializeRelation(columnName));
					Relation relation = _deserializeRelation(columnName);
					_addRelation(wikidataView, relation);
				}
				/*
				 * switch (columnName) { case RelationCassandraHelper.SOURCE:
				 * relation.setSource(columnValue); break; case
				 * RelationCassandraHelper.TARGET:
				 * relation.setTarget(columnValue); break; case
				 * RelationCassandraHelper.TYPE: relation.setType(columnValue);
				 * break; default: if
				 * (RelationCassandraHelper.PROVENANCE_SENTENCES
				 * .equals(columnValue)) { int commaOffset =
				 * columnName.indexOf(";");
				 * 
				 * String probability = columnName.substring(1, commaOffset);
				 * String provenanceSentence = columnName.subSequence(
				 * commaOffset + 1, columnName.length()).toString();
				 * relation.addProvenanceSentence(provenanceSentence,
				 * Double.parseDouble(probability)); } break; }
				 */
			}
			return wikidataView;
		}

		return null;
	}

	// --------------------------------------------------------------------------------

	private void _addRelation(WikidataView wikidataView, Relation relation) {
		Map<String, Set<String>> relations = wikidataView.getRelations().get(
				relation.getRelationType());

		if (relations == null) {
			relations = new HashMap<>();
			wikidataView.getRelations().put(relation.getRelationType(), relations);
	
		}
		
		Set<String> targets = relations.get(relation.getSource());
		if (targets == null) {
			targets = new HashSet<String>();
			relations.put(relation.getSource(), targets);
		}
		targets.add(relation.getTarget());

	}

	@Override
	public Content<String> getContent(Selector selector) {

		throw (new RuntimeException(
				"The getContent method of the WikipediaPageCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------

	@Override
	public Content<String> getAnnotatedContent(Selector selector) {
		throw (new RuntimeException(
				"The setContent method of the WikipediaPageCassandraDAO should not be invoked"));
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
				"The setContent method of the WikipediaPageCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out
				.println("Starting WikidataView Cassandra Test--------------");

		WikidataView wikidataView = _generateWikidataview();

		Core core = CoreUtility.getUIACore();

		System.out.println("Initial wikidataView> " + wikidataView);

		core.getInformationHandler().put(wikidataView,
				Context.getEmptyContext());

		System.out.println("Wikidataview  "
				+ core.getInformationHandler().get("http://wikidataView",
						RDFHelper.WIKIDATA_VIEW_CLASS));

		System.out.println("WikidataView Cassandra Test--------------");
	}

	// --------------------------------------------------------------------------------

	private static WikidataView _generateWikidataview() {

		Map<String, Set<String>> labelsDictionary = new HashMap<>();
		Map<String, Set<String>> labelsReverseDictionary = new HashMap<>();
		Map<String, Map<String, Set<String>>> relations = new HashMap<>();

		Map<String, Set<String>> hypernymRelations = new HashMap<>();
		Set<String> destionationSet = new HashSet<String>();
		destionationSet.add("http://testTargetA");
		destionationSet.add("http://testTargetB");
		hypernymRelations.put("http://testSource", destionationSet);
		relations.put(RelationHelper.HYPERNYM, hypernymRelations);

		WikidataView wikidataView = new WikidataView("http://wikidataView",
				labelsDictionary, labelsReverseDictionary, relations);
		return wikidataView;
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
