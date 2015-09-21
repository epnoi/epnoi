package org.epnoi.uia.informationstore.dao.cassandra;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import org.epnoi.uia.knowledgebase.wikidata.WikidataHandlerParameters;
import org.epnoi.uia.knowledgebase.wikidata.WikidataView;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class NewWikidataViewCassandraDAO extends CassandraDAO {

	// --------------------------------------------------------------------------------

	public void remove(String URI) {
		super.deleteRow(URI, WikidataViewCassandraHelper.COLUMN_FAMILLY);

	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {

		WikidataView wikidataView = (WikidataView) resource;

		_createDictionary(wikidataView);
		_createReverseDictionary(wikidataView);
		_createRelations(wikidataView);

		Map<String, String> pairsOfNameValues = new HashMap<String, String>();

		super.createRow(wikidataView.getURI(),
				WikidataViewCassandraHelper.COLUMN_FAMILLY);
	

		Joiner joiner = Joiner.on(";").skipNulls();

		for (Entry<String, Set<String>> labelDictionaryEntry : wikidataView
				.getLabelsDictionary().entrySet()) {
			Set<String> labelIRIsSet = labelDictionaryEntry.getValue();
			String labelIRIs = joiner.join(labelIRIsSet);
			String serializedLabelDictionaryEntry = labelDictionaryEntry
					.getKey() + ";" + labelIRIs;
			pairsOfNameValues.put(serializedLabelDictionaryEntry,
					WikidataViewCassandraHelper.DICTIONARY);
		}

		System.out.println("------------------------------> "
				+ pairsOfNameValues.size());

		super.updateManyColumns(wikidataView.getURI(), pairsOfNameValues,
				WikidataViewCassandraHelper.COLUMN_FAMILLY);
		System.out.println("Clear!!!");
		pairsOfNameValues.clear();
		pairsOfNameValues = null;

	}

	// --------------------------------------------------------------------------------

	private void _createRelations(WikidataView wikidataView) {
		// Relations mapping
		for (Entry<String, Map<String, Set<String>>> relationsEntry : wikidataView
				.getRelations().entrySet()) {

			String relationType = relationsEntry.getKey();
			for (Entry<String, Set<String>> relationEntry : relationsEntry
					.getValue().entrySet()) {

				String sourceIRI = relationEntry.getKey();
				for (String targetIRI : relationEntry.getValue()) {
/*
					pairsOfNameValues.put(

					_serializeRelation(relationType, sourceIRI, targetIRI),
							WikidataViewCassandraHelper.RELATIONS);
*/
				}

			}
		}

	}

	private void _createReverseDictionary(WikidataView wikidataView) {
		// TODO Auto-generated method stub

	}

	private void _createDictionary(WikidataView wikidataView) {
		Map<String, String> pairsOfNameValues = new HashMap<String, String>();
		Joiner joiner = Joiner.on(";").skipNulls();

		for (Entry<String, Set<String>> labelDictionaryEntry : wikidataView
				.getLabelsDictionary().entrySet()) {

			String labelAsociatedIRIs = joiner.join(labelDictionaryEntry
					.getValue());
			String labelIRI = wikidataView.getURI() + "/labels/"
					+ labelDictionaryEntry.getKey();

			pairsOfNameValues.put(labelIRI, labelAsociatedIRIs);
		}

		System.out.println("------------------------------> "
				+ pairsOfNameValues.size());

		super.updateManyColumns(wikidataView.getURI() + "/dictionary",
				pairsOfNameValues,
				WikidataViewCassandraHelper.WIKIDATA_COLUMN_FAMILY);
		System.out.println("Dicitionary Clear!!!");
		pairsOfNameValues.clear();
		pairsOfNameValues = null;

	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {

		return null;
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {
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
				+ core.getInformationHandler().get(
						WikidataHandlerParameters.DEFAULT_URI,
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

		Set<String> labelDictionary = new HashSet<String>();
		labelDictionary.add("http://testTargetA");
		labelDictionary.add("http://testTargetB");
		labelsDictionary.put("test label", labelDictionary);

		WikidataView wikidataView = new WikidataView(
				WikidataHandlerParameters.DEFAULT_URI, labelsDictionary,
				labelsReverseDictionary, relations);
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
