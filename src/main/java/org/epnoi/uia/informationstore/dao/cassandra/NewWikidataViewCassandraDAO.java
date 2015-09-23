package org.epnoi.uia.informationstore.dao.cassandra;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;

public class NewWikidataViewCassandraDAO extends CassandraDAO {
	final Joiner joiner;
	private String dictionaryURI;
	private String reverseDictionaryURI;
	private String relationsURI;

	public NewWikidataViewCassandraDAO() {
		this.joiner = Joiner.on(";").skipNulls();

	}

	// --------------------------------------------------------------------------------

	public void remove(String URI) {
		super.deleteRow(URI, WikidataViewCassandraHelper.COLUMN_FAMILY);

	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {

		WikidataView wikidataView = (WikidataView) resource;
		_defineURIs(wikidataView);
		_createDictionary(wikidataView);
		_createReverseDictionary(wikidataView);
		_createRelations(wikidataView);

		Map<String, String> pairsOfNameValues = new HashMap<String, String>();

		super.createRow(wikidataView.getURI(), WikidataViewCassandraHelper.COLUMN_FAMILY);

		Joiner joiner = Joiner.on(";").skipNulls();

		for (Entry<String, Set<String>> labelDictionaryEntry : wikidataView.getLabelsDictionary().entrySet()) {
			Set<String> labelIRIsSet = labelDictionaryEntry.getValue();
			String labelIRIs = joiner.join(labelIRIsSet);
			String serializedLabelDictionaryEntry = labelDictionaryEntry.getKey() + ";" + labelIRIs;
			pairsOfNameValues.put(serializedLabelDictionaryEntry, WikidataViewCassandraHelper.DICTIONARY);
		}

		super.updateManyColumns(wikidataView.getURI(), pairsOfNameValues, WikidataViewCassandraHelper.COLUMN_FAMILY);

		pairsOfNameValues.clear();
		pairsOfNameValues = null;

	}

	private void _defineURIs(WikidataView wikidataView) {
		this.dictionaryURI = wikidataView.getURI() + "/dictionary";
		this.reverseDictionaryURI = wikidataView.getURI() + "/reverseDictionary";
		this.relationsURI = wikidataView.getURI() + "/relations";
	}

	// --------------------------------------------------------------------------------

	private void _createRelations(WikidataView wikidataView) {
		// Relations mapping
		Map<String, String> pairsOfNameValues = new HashMap<String, String>();
		for (Entry<String, Map<String, Set<String>>> relationsEntry : wikidataView.getRelations().entrySet()) {

			String relationType = relationsEntry.getKey();
			for (Entry<String, Set<String>> relationEntry : relationsEntry.getValue().entrySet()) {

				String sourceIRI = relationEntry.getKey();
				String targetIRIs = joiner.join(relationEntry.getValue());
				pairsOfNameValues.put(sourceIRI, targetIRIs);

			}

			super.updateManyColumns(this.relationsURI + relationType, pairsOfNameValues,
					WikidataViewCassandraHelper.COLUMN_FAMILY);
		}
		pairsOfNameValues.clear();
		pairsOfNameValues = null;
	}

	// --------------------------------------------------------------------------------

	private void _createReverseDictionary(WikidataView wikidataView) {
		Map<String, String> pairsOfNameValues = new HashMap<String, String>();

		for (Entry<String, Set<String>> reverseDictionaryEntry : wikidataView.getLabelsReverseDictionary().entrySet()) {

			String labels = joiner.join(reverseDictionaryEntry.getValue());

			pairsOfNameValues.put(reverseDictionaryEntry.getKey(), labels);
		}

		super.updateManyColumns(this.reverseDictionaryURI, pairsOfNameValues,
				WikidataViewCassandraHelper.WIKIDATA_COLUMN_FAMILY);

		pairsOfNameValues.clear();
		pairsOfNameValues = null;

	}

	// --------------------------------------------------------------------------------

	private void _createDictionary(WikidataView wikidataView) {
		Map<String, String> pairsOfNameValues = new HashMap<String, String>();

		for (Entry<String, Set<String>> labelDictionaryEntry : wikidataView.getLabelsDictionary().entrySet()) {

			String labelAsociatedIRIs = joiner.join(labelDictionaryEntry.getValue());
			String labelIRI = wikidataView.getURI() + "/labels/" + labelDictionaryEntry.getKey();

			pairsOfNameValues.put(labelIRI, labelAsociatedIRIs);
		}

		super.updateManyColumns(this.dictionaryURI, pairsOfNameValues, WikidataViewCassandraHelper.COLUMN_FAMILY);

		pairsOfNameValues.clear();
		pairsOfNameValues = null;

	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {

		return null;
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {

		Map<String, Set<String>> labelsDictionary = _retrieveMap(this.dictionaryURI);

		Map<String, Set<String>> labelsReverseDictionary = _retrieveMap(this.reverseDictionaryURI);

		Map<String, Map<String, Set<String>>> relations = new HashMap<>();
		Map<String, Set<String>> hypernymRelations = _retrieveMap(this.reverseDictionaryURI);
		relations.put(RelationHelper.HYPERNYM, hypernymRelations);

		WikidataView wikidataView = new WikidataView(URI, labelsDictionary, labelsReverseDictionary, relations);

		return wikidataView;

	}

	// --------------------------------------------------------------------------------

	/**
	 * Function that retrieves a map from cassandra
	 * 
	 * @param URI
	 *            THe URI of the dictionary to be retrieved ()
	 * @return
	 */

	private Map<String, Set<String>> _retrieveMap(String URI) {

		ColumnSliceIterator<String, String, String> columnsIterator = super.getAllCollumns(URI,
				WikidataViewCassandraHelper.COLUMN_FAMILY);
		Map<String, Set<String>> dictionary = new HashMap<>();

		while (columnsIterator.hasNext()) {
			HColumn<String, String> column = columnsIterator.next();

			String IRI = column.getName();
			String IRIasssociatedValues = column.getValue();
			Set<String> splittedValues = new HashSet<String>(Splitter.on(';').splitToList(IRIasssociatedValues));
			dictionary.put(IRI, splittedValues);

		}
		return dictionary;

	}

	// --------------------------------------------------------------------------------

	@Override
	public boolean exists(Selector selector) {
		String URI = selector.getProperty(SelectorHelper.URI);

		String content = super.readColumn(selector.getProperty(SelectorHelper.URI), URI,
				RelationalSentencesCorpusCassandraHelper.COLUMN_FAMILY);

		return (content != null && content.length() > 5);
	}

	// --------------------------------------------------------------------------------

	@Override
	public Content<String> getContent(Selector selector) {

		throw (new RuntimeException("The getContent method of the WikipediaPageCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------

	@Override
	public Content<String> getAnnotatedContent(Selector selector) {
		throw (new RuntimeException("The setContent method of the WikipediaPageCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------

	@Override
	public void setContent(Selector selector, Content<String> content) {

		throw (new RuntimeException("The setContent method of the WikipediaPageCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------

	@Override
	public void setAnnotatedContent(Selector selector, Content<String> annotatedContent) {
		throw (new RuntimeException("The setContent method of the WikipediaPageCassandraDAO should not be invoked"));
	}

	// --------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out.println("Starting WikidataView Cassandra Test--------------");

		WikidataView wikidataView = _generateWikidataview();

		Core core = CoreUtility.getUIACore();

		System.out.println("Initial wikidataView> " + wikidataView);

		core.getInformationHandler().put(wikidataView, Context.getEmptyContext());

		System.out.println("Wikidataview  " + core.getInformationHandler().get(WikidataHandlerParameters.DEFAULT_URI,
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

		WikidataView wikidataView = new WikidataView(WikidataHandlerParameters.DEFAULT_URI, labelsDictionary,
				labelsReverseDictionary, relations);
		return wikidataView;
	}

}