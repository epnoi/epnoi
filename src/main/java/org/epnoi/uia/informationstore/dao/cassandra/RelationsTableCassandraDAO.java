package org.epnoi.uia.informationstore.dao.cassandra;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.ExternalResource;
import org.epnoi.model.Feed;
import org.epnoi.model.Item;
import org.epnoi.model.Relation;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.Resource;
import org.epnoi.model.Search;
import org.epnoi.uia.informationstore.Selector;

public class RelationsTableCassandraDAO extends CassandraDAO {

	public void remove(String URI) {
		super.deleteRow(URI, RelationsTableCassandraHelper.COLUMN_FAMILLY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		RelationsTable relationsTable = (RelationsTable) resource;
		super.createRow(relationsTable.getURI(),
				RelationsTableCassandraHelper.COLUMN_FAMILLY);

		for (Relation relation : relationsTable.getRelations()) {

			_createRelation(relationsTable.getURI(), relation);
		}

	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {
		return new ExternalResource();
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {

		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI,
						RelationsTableCassandraHelper.COLUMN_FAMILLY);
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
				.getAllCollumns(URI, RelationCassandraHelper.COLUMN_FAMILLY);

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
				search.getDescription(), UserCassandraHelper.COLUMN_FAMILLY);
	}

	// --------------------------------------------------------------------------------

	private void _createRelation(String relationsTableURI, Relation relation) {

		super.createRow(relation.getURI(),
				RelationCassandraHelper.COLUMN_FAMILLY);

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
				RelationCassandraHelper.COLUMN_FAMILLY);
		pairsOfNameValues.clear();
		pairsOfNameValues = null;
	}

	// --------------------------------------------------------------------------------

	@Override
	public Content<String> getContent(Selector selector) {
		// TODO Auto-generated method stub
		return null;
	}

	

	public static void main(String[] args) {
		RelationsTableCassandraDAO dao = new RelationsTableCassandraDAO();
		dao.init();
		Feed feed = (Feed) dao
				.read("http://www.epnoi.org/informationSources/slashdot");
		System.out.println(">>> " + feed.getItems().get(0).toString());
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

}
