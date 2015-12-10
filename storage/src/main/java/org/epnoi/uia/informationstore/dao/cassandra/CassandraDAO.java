package org.epnoi.uia.informationstore.dao.cassandra;

import me.prettyprint.cassandra.model.BasicColumnDefinition;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.ddl.*;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.SliceQuery;
import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.Selector;

import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;

public abstract class CassandraDAO {
	private static final Logger logger = Logger.getLogger(CassandraDAO.class.getName());
	public static final String CLUSTER = "epnoiCluster";
	public static final String KEYSPACE = "epnoiKeyspace";

	public static final int BATCH_SIZE = 100000;

	protected static Cluster cluster = null;

	protected static KeyspaceDefinition keyspaceDefinition = null;
	protected static Keyspace keyspace = null;

	protected static Map<String, ColumnFamilyTemplate<String, String>> columnFamilyTemplates = null;
	protected static List<ColumnFamilyDefinition> columnFamilyDefinitions = null;
	protected static boolean initialized = false;

	public abstract Resource read(Selector selector);

	public abstract Resource read(String URI);

	public abstract void create(Resource resource, Context context);

	public abstract void remove(String URI);

	public abstract Content<String> getContent(Selector selector);

	public abstract Content<String> getAnnotatedContent(Selector selector);

	public abstract void setContent(Selector selector, Content<String> content);

	public abstract void setAnnotatedContent(Selector selector, Content<String> annotatedContent);

	// ---------------------------------------------------------------------------------------------------------------------------------------------------

	public synchronized void init() {
		if (!initialized) {
			CassandraDAO.cluster = HFactory.getOrCreateCluster(CLUSTER, "localhost:9160");
			// System.out.println("Cluster instantiated");

			List<String> columnFamilyNames = Arrays.asList(ExternalResourceCassandraHelper.COLUMN_FAMILY,
					UserCassandraHelper.COLUMN_FAMILY, SearchCassandraHelper.COLUMN_FAMILY,
					FeedCassandraHelper.COLUMN_FAMILY, ItemCassandraHelper.COLUMN_FAMILY,
					PaperCassandraHelper.COLUMN_FAMILY, WikipediaPageCassandraHelper.COLUMN_FAMILY,
					AnnotatedContentCassandraHelper.COLUMN_FAMILY, ContentCassandraHelper.COLUMN_FAMILY,
					TermCassandraHelper.COLUMN_FAMILY, RelationalSentencesCorpusCassandraHelper.COLUMN_FAMILY,
					DomainCassandraHelper.COLUMN_FAMILY, RelationsTableCassandraHelper.COLUMN_FAMILY,
					RelationCassandraHelper.COLUMN_FAMILY, WikidataViewCassandraHelper.COLUMN_FAMILY);

			if (CassandraDAO.columnFamilyDefinitions == null) {
				logger.info("Intializing columnFamilyDefinitions");
				ColumnFamilyDefinition columnFamilyDefinition = null;

				CassandraDAO.columnFamilyDefinitions = new ArrayList<ColumnFamilyDefinition>();
				for (String columnFamilyName : columnFamilyNames) {
					if (columnFamilyName.equals(UserCassandraHelper.COLUMN_FAMILY)) {

						BasicColumnDefinition columnDefinition = new BasicColumnDefinition();
						columnDefinition.setName(StringSerializer.get().toByteBuffer(UserCassandraHelper.NAME));
						columnDefinition.setIndexName("NAME_INDEX");
						columnDefinition.setIndexType(ColumnIndexType.KEYS);
						columnDefinition.setValidationClass(ComparatorType.UTF8TYPE.getClassName());

						// columnFamilyDefinition.addColumnDefinition(columnDefinition);
						List<ColumnDefinition> columnsDefinition = new ArrayList<ColumnDefinition>();
						columnsDefinition.add(columnDefinition);
						columnFamilyDefinition = HFactory.createColumnFamilyDefinition(KEYSPACE, columnFamilyName,
								ComparatorType.UTF8TYPE, columnsDefinition);
					} else {

						columnFamilyDefinition = HFactory.createColumnFamilyDefinition(KEYSPACE, columnFamilyName,
								ComparatorType.UTF8TYPE);
						/*
						 * System.out.println("Initializing" + columnFamilyName
						 * + " > " + columnFamilyDefinition);
						 */
					}
					CassandraDAO.columnFamilyDefinitions.add(columnFamilyDefinition);

				}
			} else {

				System.out.println("columnFamilyDefinitions was already initialized");

			}

			if (CassandraDAO.keyspaceDefinition == null) {
				CassandraDAO.keyspaceDefinition = cluster.describeKeyspace(KEYSPACE);
			}

			if (CassandraDAO.keyspaceDefinition == null) {
				// if the keyspace doesn't exist, it creates one
				CassandraDAO.keyspaceDefinition = HFactory.createKeyspaceDefinition(KEYSPACE,
						ThriftKsDef.DEF_STRATEGY_CLASS, 1, columnFamilyDefinitions);

				cluster.addKeyspace(CassandraDAO.keyspaceDefinition, true);
				logger.info("Keyspace " + KEYSPACE + " created");
			} else {

				logger.info("The keyspace was already initialized");

				for (ColumnFamilyDefinition cfdef : columnFamilyDefinitions) {

					// logger.info("Adding the definition "
					// + cfdef.getName());
					try {
						cluster.addColumnFamily(cfdef);
					} catch (Exception e) {
						// logger.info("Trying to add the column definition "
						// + cfdef.getName());
					}
				}

			}
			if (CassandraDAO.keyspace == null) {
				CassandraDAO.keyspace = HFactory.createKeyspace(KEYSPACE, CassandraDAO.cluster);
				logger.info("Keyspace " + KEYSPACE + " instantiated");
			}

			// Column family templates
			// initialization------------------------------------------------------------------------------------------

			if (CassandraDAO.columnFamilyTemplates == null) {
				CassandraDAO.columnFamilyTemplates = new HashMap<String, ColumnFamilyTemplate<String, String>>();
				ColumnFamilyTemplate<String, String> columnFamilyTemplate;

				for (String columnFamilyName : columnFamilyNames) {
					// System.out.println("ct " + columnFamilyName);
					columnFamilyTemplate = new ThriftColumnFamilyTemplate<String, String>(CassandraDAO.keyspace,
							columnFamilyName, StringSerializer.get(), StringSerializer.get());
					/*
					 * System.out.println("(" + columnFamilyName + "," +
					 * columnFamilyTemplate + ")");
					 */
					CassandraDAO.columnFamilyTemplates.put(columnFamilyName, columnFamilyTemplate);
				}
			}
			initialized = true;
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * 
	 * @param key
	 *            The URI of the resoruce to be created
	 * @param columnFamilyName
	 *            The columngFamilyName of the resource
	 */
	protected void createRow(String key, String columnFamilyName) {

		ColumnFamilyUpdater<String, String> updater = CassandraDAO.columnFamilyTemplates.get(columnFamilyName)
				.createUpdater(key);

		try {
			CassandraDAO.columnFamilyTemplates.get(columnFamilyName).update(updater);

		} catch (HectorException e) {

			e.printStackTrace();
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------

	protected void updateColumn(String key, String name, String value, String columnFamilyName) {
		int maxTrials = 3;
		int trial = 0;
		boolean success = false;
		while (!success && trial < maxTrials) {

			ColumnFamilyUpdater<String, String> updater = CassandraDAO.columnFamilyTemplates.get(columnFamilyName)
					.createUpdater(key);
			updater.setString(name, value);

			try {
				CassandraDAO.columnFamilyTemplates.get(columnFamilyName).update(updater);
				success = true;
			} catch (HectorException e) {
				trial++;
				System.out.println("updateColumn " + e.getMessage());
				try {
					Thread.sleep(5000 * trial);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			updater = null;
		}

	}

	//
	// -------------------------------------------------------------------------------------------------------------------------------------------------------

	protected void updateManyColumns(String key, Map<String, String> pairsOfNameValues, String columnFamilyName) {
		// System.out.println("Updating many columns (K "+key+ "
		// pv"+pairsOfNameValues+" cfn "+columnFamilyName);
		Set<HColumn<String, String>> colums = new HashSet<HColumn<String, String>>();
		int index = 0;
		Iterator<Entry<String, String>> pairsOfNameValuesIt = pairsOfNameValues.entrySet().iterator();

		while (pairsOfNameValuesIt.hasNext()) {
			Entry<String, String> pair = pairsOfNameValuesIt.next();

			colums.add(HFactory.createStringColumn(pair.getKey(), pair.getValue()));
			index++;

			// When we have collected BATCH_SIZE pairs (name, value), or when we
			// don't have more to add, we create the mutator and perform the
			// update
			if ((index % CassandraDAO.BATCH_SIZE == 0) || !pairsOfNameValuesIt.hasNext()) {
				// System.out.println("Creating mutator and adding "+index);
				Mutator<String> mutator = columnFamilyTemplates.get(columnFamilyName).createMutator();

				for (HColumn<String, String> column : colums) {
					mutator.addInsertion(key, columnFamilyName, column);
				}
				mutator.execute();
				colums.clear();
				mutator = null;

			}
		}

	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------------

	protected void updateColumns(String key, Map<String, String> pairsOfNameValues, String columnFamilyName) {

		Set<HColumn<String, String>> colums = new HashSet<HColumn<String, String>>();
		for (Entry<String, String> pair : pairsOfNameValues.entrySet()) {
			colums.add(HFactory.createStringColumn(pair.getKey(), pair.getValue()));
		}

		Mutator<String> mutator = columnFamilyTemplates.get(columnFamilyName).createMutator();
		// String column_family_name = template.getColumnFamily();

		for (HColumn<String, String> column : colums) {
			mutator.addInsertion(key, columnFamilyName, column);
		}

		colums.clear();
		colums = null;
		mutator.execute();
		mutator = null;
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 * @param key
	 *            The URI of the element that we wish to access
	 * @param name
	 *            The name of the column
	 * @param columnFamilyName
	 *            The name of the Cassandra column family
	 * @return
	 */
	protected String readColumn(String key, String name, String columnFamilyName) {
		try {
			HColumn<String, String> res = CassandraDAO.columnFamilyTemplates.get(columnFamilyName)
					.querySingleColumn(key, name, String.class);
			if (res != null) {
				// System.out.println("---> "+res.getName());
				String value = res.getValue();
				// res = null;
				return value;
			}

		} catch (HectorException e) {
			logger.severe("The source of the exception was the call readColumn("+key+","+name+","+columnFamilyName+")");
			e.printStackTrace();
		}
		return null;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * 
	 * @param key
	 * @param columnFamilyName
	 * @return
	 */
	protected ColumnFamilyResult<String, String> readRow(String key, String columnFamilyName) {
		ColumnFamilyResult<String, String> result = null;
		try {
			result = CassandraDAO.columnFamilyTemplates.get(columnFamilyName).queryColumns(key);

		} catch (HectorException e) {
			System.out.println("Not possible to read the column with key " + key);
		}
		return result;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------

	protected void deleteRow(String key, String columnFamilyName) {
		try {
			CassandraDAO.columnFamilyTemplates.get(columnFamilyName).deleteRow(key);
		} catch (HectorException e) {
			System.out.println("Not possible to delete row with key " + key);
		}

	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * 
	 * @param key
	 *            : The URI of the item
	 * @param columnFamilyKey
	 *            : The column family name of the item
	 * @return
	 */
	protected ColumnSliceIterator<String, String, String> getAllCollumns(String key, String columnFamilyKey) {

		SliceQuery<String, String, String> query = HFactory
				.createSliceQuery(keyspace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get())
				.setKey(key).setColumnFamily(columnFamilyKey);

		ColumnSliceIterator<String, String, String> iterator = new ColumnSliceIterator<String, String, String>(query,
				null, "\uFFFF", false);

		return iterator;

	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		String USER_CF = "USER_CF";

		CassandraDAO cassandraDAO = new UserCassandraDAO();
		cassandraDAO.init();
		cassandraDAO.updateColumn("http://whatever", "pepito", "grillo", USER_CF);
		cassandraDAO.updateColumn("http://whatever", "pepito", "grillo2", USER_CF);

		cassandraDAO.readRow("http://whatever", USER_CF);
		// cassandraDAOProof.delete("http://whatever");
		// cassandraDAOProof.read("http://whatever");
		cassandraDAO.getAllCollumns("http://whatever", USER_CF);

	}

	public abstract boolean exists(Selector selector);

}