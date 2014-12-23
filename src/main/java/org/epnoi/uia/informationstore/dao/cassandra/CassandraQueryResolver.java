package org.epnoi.uia.informationstore.dao.cassandra;

import java.util.Iterator;
import java.util.List;

import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;

public class CassandraQueryResolver extends CassandraDAO {
	public static final String CLUSTER = "epnoiCluster";
	public static final String KEYSPACE = "epnoiKeyspace";
	private final static String HOST_PORT = "localhost:9160";

	private final static StringSerializer se = StringSerializer.get();

	// ------------------------------------------------------------------------------------------

	public static List<Row<String, String, String>> query(String query) {
		List<Row<String, String, String>> list = null;
		Cluster c = HFactory.getOrCreateCluster(CLUSTER, HOST_PORT);

		CqlQuery<String, String, String> cqlQuery = new CqlQuery<String, String, String>(
				HFactory.createKeyspace(KEYSPACE, c), se, se, se);
		cqlQuery.setQuery(query);
		// cqlQuery.setQuery("select * from User where NAME='Rafita'");
		QueryResult<CqlRows<String, String, String>> result = cqlQuery
				.execute();
		if (result != null && result.get() != null) {
			list = result.get().getList();
			for (Row row : list) {
				System.out.println("." + row.getKey());
				List columns = row.getColumnSlice().getColumns();
				for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
					HColumn column = (HColumn) iterator.next();
					System.out.print(column.getName() + ":" + column.getValue()
							+ "\t");
				}
				System.out.println("");
			}
		}
		return list;
	}

	// ------------------------------------------------------------------------------------------

	public boolean exists(Selector selector) {

		
		String URI = selector.getProperty(SelectorHelper.URI);

		String resourceType = selector.getProperty(SelectorHelper.TYPE);
		return super.getAllCollumns(URI, resourceType)!=null;

		

	}

	// ------------------------------------------------------------------------------------------

	public static void main(String[] args) {
	
		Cluster c = HFactory.getOrCreateCluster(CLUSTER, HOST_PORT);

		CqlQuery<String, String, String> cqlQuery = new CqlQuery<String, String, String>(
				HFactory.createKeyspace(KEYSPACE, c), se, se, se);
		cqlQuery.setQuery("select key from 'Item'");
		QueryResult<CqlRows<String, String, String>> result = cqlQuery
				.execute();
		if (result != null && result.get() != null) {
			List<Row<String, String, String>> list = result.get().getList();
			for (Row row : list) {
				System.out.println("." + row.getKey());
				List columns = row.getColumnSlice().getColumns();
				for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
					HColumn column = (HColumn) iterator.next();
					System.out.print(column.getName() + ":" + column.getValue()
							+ "\t");
				}
				System.out.println("");
			}
		} else {
			System.out
					.println("Seems that that the query didn't return anything");
		}
	}

	@Override
	public Resource read(Selector selector) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource read(String URI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(Resource resource, Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(String URI) {
		// TODO Auto-generated method stub

	}

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

	// ------------------------------------------------------------------------------------------
}