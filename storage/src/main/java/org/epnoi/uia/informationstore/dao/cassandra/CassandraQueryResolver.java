package org.epnoi.uia.informationstore.dao.cassandra;

import com.google.common.base.Splitter;
import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CassandraQueryResolver extends CassandraDAO {
	public static final String CLUSTER = "epnoiCluster";
	public static final String KEYSPACE = "epnoiKeyspace";
	private final static String HOST_PORT = "localhost:9160";

	private final static StringSerializer stringSerializer = StringSerializer.get();

	// ------------------------------------------------------------------------------------------

	public static List<Row<String, String, String>> query(String query) {
		List<Row<String, String, String>> list = null;
		Cluster cluster = HFactory.getOrCreateCluster(CLUSTER, HOST_PORT);

		CqlQuery<String, String, String> cqlQuery = new CqlQuery<String, String, String>(
				HFactory.createKeyspace(KEYSPACE, cluster), stringSerializer, stringSerializer, stringSerializer);
		cqlQuery.setQuery(query);
		// cqlQuery.setQuery("select * from User where NAME='Rafita'");
		QueryResult<CqlRows<String, String, String>> result = cqlQuery.execute();
		if (result != null && result.get() != null) {
			list = result.get().getList();
			for (Row row : list) {
				System.out.println("." + row.getKey());
				List columns = row.getColumnSlice().getColumns();
				for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
					HColumn column = (HColumn) iterator.next();
					System.out.print(column.getName() + ":" + column.getValue() + "\t");
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

		System.out.println(" |------> " + super.getAllCollumns(URI, resourceType).hasNext());

		return super.getAllCollumns(URI, resourceType).hasNext();

	}

	// ------------------------------------------------------------------------------------------

	public static void main(String[] args) {

		Cluster c = HFactory.getOrCreateCluster(CLUSTER, HOST_PORT);

		CqlQuery<String, String, String> cqlQuery = new CqlQuery<String, String, String>(
				HFactory.createKeyspace(KEYSPACE, c), stringSerializer, stringSerializer, stringSerializer);
		cqlQuery.setQuery("select key from 'Item'");
		QueryResult<CqlRows<String, String, String>> result = cqlQuery.execute();
		if (result != null && result.get() != null) {
			List<Row<String, String, String>> list = result.get().getList();
			for (Row row : list) {
				System.out.println("." + row.getKey());
				List columns = row.getColumnSlice().getColumns();
				for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
					HColumn column = (HColumn) iterator.next();
					System.out.print(column.getName() + ":" + column.getValue() + "\t");
				}
				System.out.println("");
			}
		} else {
			System.out.println("Seems that that the query didn't return anything");
		}
	}

	// ------------------------------------------------------------------------------------------
	/**
	 * Gets the set of values
	 * 
	 * @param key
	 * @param column
	 * @param columnFamilyName
	 * @return
	 */

	public Set<String> getValues(String key, String column, String columnFamilyName) {
	//	System.out.println("key " + key + " column [" + column + "] columnFamilyName " + columnFamilyName);

		// System.out.println(">>> TIENE RESULTS!" + super.readColumn(key, "
		// Madrid", columnFamilyName));

		String values = super.readColumn(key, column, columnFamilyName);
		if (values != null) {
			Set<String> parsedValues = new HashSet<String>(Splitter.on(';').splitToList(values));
		//	System.out.println("parsedValues >" + parsedValues);
			return parsedValues;
		}
		return new HashSet<String>();
	}
	/*
	 * public void setValues(String key, String column, Set<String> values,
	 * String columnFamilyName) { Joiner joiner = Joiner.on(";").skipNulls();
	 * 
	 * String joinedValues = joiner.join(values); super. }
	 */

	public Set<String> getWith(String key, String columnFamilyName, String m) {
		ColumnSliceIterator<String, String, String> columnsIterator = super.getAllCollumns(key, columnFamilyName);
		Set<String> values = new HashSet<String>();
		if (columnsIterator.hasNext()) {
			while (columnsIterator.hasNext()) {
				HColumn<String, String> column = columnsIterator.next();
				if (column.getName().contains(m)) {
					System.out.println("I found one name[" + column.getName() + "] values [" + column.getValue() + "]");
					values.add(column.getName());

					System.out.println("Con el getValues " + this.getValues(key, column.getName(), columnFamilyName));

					break;
				}
			}
		} else {
			System.out.println("NO RESULT!");
		}
		return values;
	}

	// ------------------------------------------------------------------------------------------

	@Override
	public Resource read(Selector selector) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------------------------

	@Override
	public Resource read(String URI) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------------------------

	@Override
	public void create(Resource resource, Context context) {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------------------------------

	@Override
	public void remove(String URI) {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------------------------------

	@Override
	public Content<String> getContent(Selector selector) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------------------------

	@Override
	public Content<String> getAnnotatedContent(Selector selector) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------------------------

	@Override
	public void setContent(Selector selector, Content<String> content) {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------------------------------

	@Override
	public void setAnnotatedContent(Selector selector, Content<String> annotatedContent) {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------------------------------
}