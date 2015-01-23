package org.epnoi.uia.informationstore.dao.cassandra;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.ExternalResource;
import org.epnoi.model.Resource;
import org.epnoi.model.Search;
import org.epnoi.uia.informationstore.Selector;


public class SearchCassandraDAO extends CassandraDAO {

	public void remove(String URI) {
		super.deleteRow(URI, SearchCassandraHelper.COLUMN_FAMILLY);
	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		Search search = (Search) resource;
		super.createRow(search.getURI(), SearchCassandraHelper.COLUMN_FAMILLY);

		if (search.getTitle() != null) {

			super.updateColumn(search.getURI(), SearchCassandraHelper.TITLE,
					search.getTitle(), SearchCassandraHelper.COLUMN_FAMILLY);

		}

		if (search.getDescription() != null) {
			super.updateColumn(search.getURI(),
					SearchCassandraHelper.DESCRIPTION, search.getDescription(),
					SearchCassandraHelper.COLUMN_FAMILLY);

		}

		for (String expression : search.getExpressions()) {
			super.updateColumn(search.getURI(), expression,
					SearchCassandraHelper.EXPRESSIONS,
					SearchCassandraHelper.COLUMN_FAMILLY);
		}

	}

	// --------------------------------------------------------------------------------

	public Resource read(Selector selector) {
		return new ExternalResource();
	}

	// --------------------------------------------------------------------------------

	public Resource read(String URI) {
		/*
		 * System.out.println(" --> " + URI); ColumnSliceIterator<String,
		 * String, String> columnsIteratorProof = super .getAllCollumns(URI,
		 * ExternalResourceCassandraHelper.COLUMN_FAMILLY);
		 * 
		 * while (columnsIteratorProof.hasNext()) { HColumn<String, String>
		 * column = columnsIteratorProof.next(); System.out.println("Column   "
		 * + column); }
		 */
		ColumnSliceIterator<String, String, String> columnsIterator = super
				.getAllCollumns(URI, SearchCassandraHelper.COLUMN_FAMILLY);
		if (columnsIterator.hasNext()) {
			Search search = new Search();
			search.setURI(URI);
			while (columnsIterator.hasNext()) {

				HColumn<String, String> column = columnsIterator.next();
				//System.out.println("--column " + column);
				if (SearchCassandraHelper.TITLE.equals(column.getName())) {
					search.setTitle(column.getValue());

				} else {
					if (SearchCassandraHelper.DESCRIPTION.equals(column
							.getName())) {
						search.setDescription(column.getValue());
					} else {
						if (SearchCassandraHelper.EXPRESSIONS.equals(column
								.getValue())) {
							search.addExpression(column.getName());
						}
					}

				}
			}

			return search;
		}

		return null;
	}

	// --------------------------------------------------------------------------------

	public void update(Search search) {
		super.updateColumn(search.getURI(), SearchCassandraHelper.DESCRIPTION,
				search.getDescription(), UserCassandraHelper.COLUMN_FAMILLY);
	}

	// --------------------------------------------------------------------------------
	/*
	 * public boolean existsUserWithName(String name) { List result =
	 * (CassandraCQLClient .query("select * from User where NAME='" + name +
	 * "'")); return ((result != null) && (result.size() > 0)); }
	 * 
	 * //
	 * ------------------------------------------------------------------------
	 * -------- public User getUserWithName(String name) { List result =
	 * (CassandraCQLClient .query("select * from User where NAME='" + name +
	 * "'")); if ((result != null) && (result.size() > 0)) { Row row = (Row)
	 * result.get(0); User user = this.read((String) row.getKey()); return user;
	 * } return null; }
	 */

	// --------------------------------------------------------------------------------

	public List<Search> getSearchs() {
		List<Search> searchs = new ArrayList<Search>();
		List<Row<String, String, String>> result = (CassandraQueryResolver
				.query("select * from " + SearchCassandraHelper.COLUMN_FAMILLY));
		if (result != null) {
			for (Row<String, String, String> row : result) {

				Search search = (Search) this.read((String) row.getKey());
				searchs.add(search);

			}
		}
		return searchs;
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

		
		
		
}
