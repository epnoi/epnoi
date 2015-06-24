package org.epnoi.uia.informationstore.dao.map;

import gate.Document;

import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;

import org.epnoi.model.Content;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.dao.DAO;
import org.epnoi.uia.parameterization.MapInformationStoreParameters;
import org.mapdb.DB;
import org.mapdb.DBMaker;

public abstract class MapDAO implements DAO {
	protected static DB database;
	private static File databaseFile;
	private static boolean initialized = false;
	private static final String ANNOTATED_CONTENT_COLLECTION = "ANNOTATED_CONTENT_COLLECTION";
	private static final String OTHER_ANNOTATED_CONTENT_COLLECTION = "OTHER_ANNOTATED_CONTENT_COLLECTION";

	protected static ConcurrentNavigableMap<String, Object> map;

	public abstract Content<Object> getAnnotatedContent(Selector selector);

	public abstract void setAnnotatedContent(Selector selector,
			Content<Object> annotatedContent);

	public synchronized void init(MapInformationStoreParameters parameters) {
		if (!initialized) {
			/*
			 * System.out .println(
			 * "Initializing-------------------------------------------------------------"
			 * );
			 */
			databaseFile = new File(parameters.getPath());
			database = DBMaker.newFileDB(databaseFile).transactionDisable()
					.compressionEnable().closeOnJvmShutdown().make();

			map = database.getTreeMap(ANNOTATED_CONTENT_COLLECTION);
			initialized = true;
		}
	}
}
