package org.epnoi.uia.informationstore.dao.cassandra;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;
import org.epnoi.model.*;
import org.epnoi.uia.informationstore.SelectorHelper;

import java.util.*;
import java.util.Map.Entry;

public class WikidataViewCassandraDAO extends CassandraDAO {
	final Joiner joiner;
	private String dictionaryURI;
	private String reverseDictionaryURI;
	private String relationsURI;

	public WikidataViewCassandraDAO() {
		this.joiner = Joiner.on(";").skipNulls();

	}

	// --------------------------------------------------------------------------------

	public void remove(String URI) {
		
		_defineURIs(URI);
		super.deleteRow(URI, WikidataViewCassandraHelper.COLUMN_FAMILY);
		super.deleteRow(this.dictionaryURI,
				WikidataViewCassandraHelper.COLUMN_FAMILY);
		super.deleteRow(this.reverseDictionaryURI,
				WikidataViewCassandraHelper.COLUMN_FAMILY);
		super.deleteRow(this.relationsURI + "/" + RelationHelper.HYPERNYMY,
				WikidataViewCassandraHelper.COLUMN_FAMILY);

	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
	
		WikidataView wikidataView = (WikidataView) resource;

		_defineURIs(wikidataView.getUri());

		_defineRows(wikidataView.getUri());

		_createDictionary(wikidataView);
		_createReverseDictionary(wikidataView);
		_createRelations(wikidataView);

	}

	private void _defineRows(String URI) {
		super.createRow(URI, WikidataViewCassandraHelper.COLUMN_FAMILY);

		super.createRow(this.dictionaryURI,
				WikidataViewCassandraHelper.COLUMN_FAMILY);

		super.createRow(this.reverseDictionaryURI,
				WikidataViewCassandraHelper.COLUMN_FAMILY);
		super.createRow(this.relationsURI + "/" + RelationHelper.HYPERNYMY,
				WikidataViewCassandraHelper.COLUMN_FAMILY);
	}

	// --------------------------------------------------------------------------------

	private void _defineURIs(String URI) {
		this.dictionaryURI = URI + "/dictionary";
		this.reverseDictionaryURI = URI + "/reverseDictionary";
		this.relationsURI = URI + "/relations";
	}

	// --------------------------------------------------------------------------------

	private void _createRelations(WikidataView wikidataView) {
		// Relations mapping
		Map<String, String> pairsOfNameValues = new HashMap<String, String>();
		for (Entry<String, Map<String, Set<String>>> relationsEntry : wikidataView
				.getRelations().entrySet()) {

			String relationType = relationsEntry.getKey();
			for (Entry<String, Set<String>> relationEntry : relationsEntry
					.getValue().entrySet()) {

				String sourceIRI = relationEntry.getKey();
				String targetIRIs = joiner.join(relationEntry.getValue());
				pairsOfNameValues.put(sourceIRI, targetIRIs);

			}

			super.updateManyColumns(this.relationsURI +"/" +relationType,
					pairsOfNameValues,
					WikidataViewCassandraHelper.COLUMN_FAMILY);
		}
		pairsOfNameValues.clear();
		pairsOfNameValues = null;
	}

	// --------------------------------------------------------------------------------

	private void _createReverseDictionary(WikidataView wikidataView) {
		Map<String, String> pairsOfNameValues = new HashMap<String, String>();

		for (Entry<String, Set<String>> reverseDictionaryEntry : wikidataView
				.getLabelsReverseDictionary().entrySet()) {

			String labels = joiner.join(reverseDictionaryEntry.getValue());

			pairsOfNameValues.put(reverseDictionaryEntry.getKey(), labels);
		}

		super.updateManyColumns(this.reverseDictionaryURI, pairsOfNameValues,
				WikidataViewCassandraHelper.COLUMN_FAMILY);

		pairsOfNameValues.clear();
		pairsOfNameValues = null;

	}

	// --------------------------------------------------------------------------------

	private void _createDictionary(WikidataView wikidataView) {
		Map<String, String> pairsOfNameValues = new HashMap<String, String>();

		for (Entry<String, Set<String>> labelDictionaryEntry : wikidataView
				.getLabelsDictionary().entrySet()) {

			String labelAsociatedIRIs = joiner.join(labelDictionaryEntry
					.getValue());
		/*
			String labelIRI = wikidataView.getURI() + "/labels/"
					+ labelDictionaryEntry.getKey();
*/
			pairsOfNameValues.put(labelDictionaryEntry.getKey(), labelAsociatedIRIs);
		}

		super.updateManyColumns(this.dictionaryURI, pairsOfNameValues,
				WikidataViewCassandraHelper.COLUMN_FAMILY);

		pairsOfNameValues.clear();
		pairsOfNameValues = null;

	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {
		//System.out.println("Reading selector >> " + selector);
		return null;
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {
		//System.out.println("Reading URI >> " + URI);
		WikidataView wikidataView = null;
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.URI, URI);
		if (exists(selector)) {
			_defineURIs(URI);
			Map<String, Set<String>> labelsDictionary = _retrieveMap(this.dictionaryURI);

			Map<String, Set<String>> labelsReverseDictionary = _retrieveMap(this.reverseDictionaryURI);

			Map<String, Map<String, Set<String>>> relations = new HashMap<>();
			Map<String, Set<String>> hypernymRelations = _retrieveMap(this.relationsURI+"/"+RelationHelper.HYPERNYMY);
			
			relations.put(RelationHelper.HYPERNYMY, hypernymRelations);

			wikidataView = new WikidataView(URI, labelsDictionary,
					labelsReverseDictionary, relations);

			return wikidataView;
		}
			
		
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

		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI, WikidataViewCassandraHelper.COLUMN_FAMILY);
		Map<String, Set<String>> dictionary = new HashMap<>();

		while (columnsIterator.hasNext()) {
			HColumn<String, String> column = columnsIterator.next();

			String IRI = column.getName();
			String IRIasssociatedValues = column.getValue();
			Set<String> splittedValues = new HashSet<String>(Splitter.on(';')
					.splitToList(IRIasssociatedValues));
			dictionary.put(IRI, splittedValues);

		}
	//	System.out.println("["+URI+"] Dictionary "+dictionary);
		return dictionary;

	}

	// --------------------------------------------------------------------------------

	@Override
	public boolean exists(Selector selector) {
		String URI = selector.getProperties().get(SelectorHelper.URI);
		_defineURIs(URI);
		
	//	System.out.println(URI+"----> "+super.readRow(URI+"", WikidataViewCassandraHelper.COLUMN_FAMILY).hasResults());
		return (super.readRow(this.dictionaryURI, WikidataViewCassandraHelper.COLUMN_FAMILY).hasResults());
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
	/*
	public static void main(String[] args) {
	
		System.out
				.println("Starting WikidataView Cassandra Test--------------");

		WikidataView wikidataView = _generateWikidataview();

		Core core = CoreUtility.getUIACore();

		System.out.println("Initial wikidataView> " + wikidataView);

		core.getInformationHandler().put(wikidataView,
				Context.getEmptyContext());

		WikidataView readedWikidataview = (WikidataView)core.getInformationHandler().get(
				WikidataHandlerParameters.DEFAULT_URI,
				RDFHelper.WIKIDATA_VIEW_CLASS);
		System.out.println("The readed Wikidataview>  "
				+readedWikidataview );

		System.out.println("test label assoicated uris"
				+ readedWikidataview
						.getRelated("source label", RelationHelper.HYPERNYMY));
		
		System.out.println("There are "+readedWikidataview
						.getRelated("source label", RelationHelper.HYPERNYMY).size() );
		
		System.out.println("test label 3 assoicated uris"
				+ readedWikidataview.getRelated("test label 3",
						RelationHelper.HYPERNYMY));

		System.out.println("Let's delete it !");
		core.getInformationHandler().remove(wikidataView);
		System.out.println("Wikidataview  "
				+ core.getInformationHandler().get(
						WikidataHandlerParameters.DEFAULT_URI,
						RDFHelper.WIKIDATA_VIEW_CLASS));

		System.out.println("WikidataView Cassandra Test--------------");
	
	}
*/
	// --------------------------------------------------------------------------------

	private static WikidataView _generateWikidataview(String URI) {

		Map<String, Set<String>> labelsDictionary = new HashMap<>();
		Map<String, Set<String>> labelsReverseDictionary = new HashMap<>();
		
		
		Map<String, Map<String, Set<String>>> relations = new HashMap<>();
		Map<String, Set<String>> hypernymRelations = new HashMap<>();
		Set<String> destionationSet = new HashSet<String>();
		destionationSet.add("http://testTargetA");
		destionationSet.add("http://testTargetB");
		hypernymRelations.put("http://testSource", destionationSet);
		relations.put(RelationHelper.HYPERNYMY, hypernymRelations);

		
		
		Set<String> labelDictionary = new HashSet<String>();
		labelDictionary.add("http://testTargetA");
	
		labelsDictionary.put("target label", labelDictionary);
		labelsDictionary.put("source label", new HashSet<String>(Arrays.asList("http://testSource")));
		
		
		labelsReverseDictionary.put("http://testSource", new HashSet<String>(Arrays.asList("source label")));
		
		labelsReverseDictionary.put("http://testTargetA", new HashSet<String>(Arrays.asList("target label")));
		
	

		WikidataView wikidataView = new WikidataView(
				URI, labelsDictionary,
				labelsReverseDictionary, relations);
		return wikidataView;
	}

}