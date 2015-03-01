package org.epnoi.uia.informationstore.dao.map;

import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;

import org.epnoi.model.Content;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.dao.DAO;
import org.epnoi.uia.parameterization.MapInformationStoreParameters;
import org.mapdb.DB;
import org.mapdb.DBMaker;

public abstract class MapDAO implements DAO {
	private static DB database;
	private static File databaseFile;
	private static boolean initialized = false;
	private static final String ANNOTATED_CONTENT_COLLECTION = "ANNOTATED_CONTENT_COLLECTION";

	protected static ConcurrentNavigableMap<String, String> map;

	public abstract Content<String> getAnnotatedContent(Selector selector);

	public abstract void setAnnotatedContent(Selector selector,
			Content<String> annotatedContent);
	
	public synchronized void init(MapInformationStoreParameters parameters) {
		if (!initialized) {
			databaseFile = new File(parameters.getPath());
			database = DBMaker.newFileDB(databaseFile).closeOnJvmShutdown()
					.make();
			map = database.getTreeMap(ANNOTATED_CONTENT_COLLECTION);
			initialized = true;
		}
	}
}
