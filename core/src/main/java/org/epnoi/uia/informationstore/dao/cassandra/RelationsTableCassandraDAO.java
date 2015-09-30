package org.epnoi.uia.informationstore.dao.cassandra;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.ExternalResource;
import org.epnoi.model.Relation;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.Resource;
import org.epnoi.model.Search;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.Selector;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;

public class RelationsTableCassandraDAO extends CassandraDAO {

	public void remove(String URI) {
		super.deleteRow(URI, RelationsTableCassandraHelper.COLUMN_FAMILY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		
		RelationsTable relationsTable = (RelationsTable) resource;
		super.createRow(relationsTable.getURI(),
				RelationsTableCassandraHelper.COLUMN_FAMILY);

		for (Relation relation : relationsTable.getRelations()) {

			_createRelation(relationsTable.getURI(), relation);
		}

		
		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(relationsTable.getURI(),
						RelationsTableCassandraHelper.COLUMN_FAMILY);


	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {
		return new ExternalResource();
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {

		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI,
						RelationsTableCassandraHelper.COLUMN_FAMILY);

	
		if (columnsIterator.hasNext()) {

			RelationsTable relationsTable = new RelationsTable();
			relationsTable.setURI(URI);
			while (columnsIterator.hasNext()) {

				HColumn<String, String> column = columnsIterator.next();

				if (RelationsTableCassandraHelper.RELATIONS.equals(column
						.getValue())) {
					Relation relation = _readRelation(column.getName());
					relationsTable.addRelation(relation);
				}
			}

			return relationsTable;
		}

		return null;
	}

	// --------------------------------------------------------------------------------

	private Relation _readRelation(String URI) {
		Relation relation = new Relation();
		relation.setURI(URI);

		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI, RelationCassandraHelper.COLUMN_FAMILY);

		while (columnsIterator.hasNext()) {

			HColumn<String, String> column = columnsIterator.next();

			String columnName = column.getName();
			String columnValue = column.getValue();
			switch (columnName) {
			case RelationCassandraHelper.SOURCE:
				relation.setSource(columnValue);
				break;
			case RelationCassandraHelper.TARGET:
				relation.setTarget(columnValue);
				break;
			case RelationCassandraHelper.TYPE:
				relation.setType(columnValue);
				break;
			default:
				if (RelationCassandraHelper.PROVENANCE_SENTENCES
						.equals(columnValue)) {
					int commaOffset = columnName.indexOf(";");

					String probability = columnName.substring(1, commaOffset);
					String provenanceSentence = columnName.subSequence(
							commaOffset + 1, columnName.length()).toString();
					relation.addProvenanceSentence(provenanceSentence,
							Double.parseDouble(probability));
				}
				break;
			}

		}
		return relation;

	}

	// --------------------------------------------------------------------------------

	public void update(Search search) {
		super.updateColumn(search.getURI(), SearchCassandraHelper.DESCRIPTION,
				search.getDescription(), UserCassandraHelper.COLUMN_FAMILY);
	}

	// --------------------------------------------------------------------------------

	private void _createRelation(String relationsTableURI, Relation relation) {

		super.createRow(relation.getURI(),
				RelationCassandraHelper.COLUMN_FAMILY);

		Map<String, String> pairsOfNameValues = new HashMap<String, String>();

		pairsOfNameValues.put(RelationCassandraHelper.SOURCE,
				relation.getSource());

		pairsOfNameValues.put(RelationCassandraHelper.TARGET,
				relation.getTarget());

		pairsOfNameValues.put(RelationCassandraHelper.TYPE, relation.getType());

		for (Entry<String, Double> entry : relation
				.getProvenanceRelationhoodTable().entrySet()) {
			pairsOfNameValues.put(entry.getValue() + ";" + entry.getKey(),
					RelationCassandraHelper.PROVENANCE_SENTENCES);

		}
		
		super.updateColumns(relation.getURI(), pairsOfNameValues,
				RelationCassandraHelper.COLUMN_FAMILY);
		pairsOfNameValues.clear();
		pairsOfNameValues = null;
		super.updateColumn(relationsTableURI, relation.getURI(),
				RelationsTableCassandraHelper.RELATIONS,
				RelationsTableCassandraHelper.COLUMN_FAMILY);

	}

	// --------------------------------------------------------------------------------

	@Override
	public Content<String> getContent(Selector selector) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Content<String> getAnnotatedContent(Selector selector) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContent(Selector selector, Content<String> content) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAnnotatedContent(Selector selector,
			Content<String> annotatedContent) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean exists(Selector selector) {
		// TODO Auto-generated method stub
		return false;
	}

	public static void main(String[] args) {
		RelationsTable relationsTable = new RelationsTable();
		relationsTable.setURI("http://relationsTableForADomain");
		Relation relationA = new Relation();
		relationA.setURI("http://relationA/source/target");
		relationA.setSource("http://relationA/source");
		relationA.setTarget("http://relationA/target");
		relationA.setType(RelationHelper.HYPERNYM);
		relationA.addProvenanceSentence("blablablaA", 0.5);
		relationA.addProvenanceSentence("loqueseaA", 0.75);

		relationsTable.addRelation(relationA);

		Relation relationB = new Relation();
		relationB.setURI("http://relationB/source/target");
		relationB.setSource("http://relationB/source");
		relationB.setTarget("http://relationB/target");
		relationB.setType(RelationHelper.HYPERNYM);
		relationB.addProvenanceSentence("blablablaB", 0.5);
		relationB.addProvenanceSentence("loqueseaB", 0.75);
		relationB.addProvenanceSentence("pozipoziB", 0.75);

		relationsTable.addRelation(relationB);

		Core core = CoreUtility.getUIACore();
		core.getInformationHandler().put(relationsTable,
				Context.getEmptyContext());

		System.out.println("Relations Table "
				+ core.getInformationHandler().get(
						"http://relationsTableForADomain",
						RDFHelper.RELATIONS_TABLE_CLASS));

	}

}
