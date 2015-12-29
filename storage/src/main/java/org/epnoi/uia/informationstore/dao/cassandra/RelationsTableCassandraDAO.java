package org.epnoi.uia.informationstore.dao.cassandra;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;
import org.epnoi.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class RelationsTableCassandraDAO extends CassandraDAO {

	public void remove(String URI) {
		super.deleteRow(URI, RelationsTableCassandraHelper.COLUMN_FAMILY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		System.out.println("create 00000> "+resource);
		RelationsTable relationsTable = (RelationsTable) resource;
		super.createRow(relationsTable.getUri(),
				RelationsTableCassandraHelper.COLUMN_FAMILY);

		for (Relation relation : relationsTable.getRelations()) {

			_createRelation(relationsTable.getUri(), relation);
		}


	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {
		return new ExternalResource();
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {
		RelationsTable relationsTable = new RelationsTable();
		relationsTable.setUri(URI);
		System.out.println(">>>" + super.readRow(URI,
				RelationsTableCassandraHelper.COLUMN_FAMILY).hasResults());

		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI,
						RelationsTableCassandraHelper.COLUMN_FAMILY);

	



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

	// --------------------------------------------------------------------------------

	private Relation _readRelation(String URI) {
		Relation relation = new Relation();
		relation.setUri(URI);

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
		super.updateColumn(search.getUri(), SearchCassandraHelper.DESCRIPTION,
				search.getDescription(), UserCassandraHelper.COLUMN_FAMILY);
	}

	// --------------------------------------------------------------------------------

	private void _createRelation(String relationsTableURI, Relation relation) {

		super.createRow(relation.getUri(),
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
		
		super.updateColumns(relation.getUri(), pairsOfNameValues,
				RelationCassandraHelper.COLUMN_FAMILY);
		pairsOfNameValues.clear();
		pairsOfNameValues = null;
		super.updateColumn(relationsTableURI, relation.getUri(),
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
	/* 
	 
	 FOR TEST

	public static void main(String[] args) {
		RelationsTable relationsTable = new RelationsTable();
		relationsTable.setURI("http://relationsTableForADomain");
		Relation relationA = new Relation();
		relationA.setURI("http://relationA/source/target");
		relationA.setSource("http://relationA/source");
		relationA.setTarget("http://relationA/target");
		relationA.setType(RelationHelper.HYPERNYMY);
		relationA.addProvenanceSentence("blablablaA", 0.5);
		relationA.addProvenanceSentence("loqueseaA", 0.75);

		relationsTable.addRelation(relationA);

		Relation relationB = new Relation();
		relationB.setURI("http://relationB/source/target");
		relationB.setSource("http://relationB/source");
		relationB.setTarget("http://relationB/target");
		relationB.setType(RelationHelper.HYPERNYMY);
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
*/
}
