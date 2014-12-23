package org.epnoi.uia.informationstore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.uia.informationstore.dao.cassandra.AnnotatedContenCassandraDAO;
import org.epnoi.uia.informationstore.dao.cassandra.CassandraDAO;
import org.epnoi.uia.informationstore.dao.cassandra.CassandraDAOFactory;
import org.epnoi.uia.informationstore.dao.cassandra.CassandraQueryResolver;
import org.epnoi.uia.informationstore.dao.cassandra.ContentCassandraDAO;
import org.epnoi.uia.informationstore.dao.cassandra.FeedCassandraHelper;
import org.epnoi.uia.informationstore.dao.cassandra.ItemCassandraHelper;
import org.epnoi.uia.informationstore.dao.cassandra.PaperCassandraHelper;
import org.epnoi.uia.informationstore.dao.cassandra.WikipediaPageCassandraHelper;
import org.epnoi.uia.informationstore.dao.rdf.FeedRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.search.SearchContext;
import org.epnoi.uia.search.select.SearchSelectResult;
import org.epnoi.uia.search.select.SelectExpression;

public class CassandraInformationStore implements InformationStore {
	private InformationStoreParameters parameters;
	private CassandraDAOFactory daoFactory;
	private CassandraQueryResolver queryResolver;
	public static final Map<String, String> typesTable = new HashMap<>();
	static {
		typesTable.put(RDFHelper.PAPER_CLASS,
				PaperCassandraHelper.COLUMN_FAMILLY);
		typesTable.put(FeedRDFHelper.FEED_CLASS,
				FeedCassandraHelper.COLUMN_FAMILLY);
		typesTable.put(FeedRDFHelper.ITEM_CLASS,
				ItemCassandraHelper.COLUMN_FAMILLY);
		typesTable.put(RDFHelper.WIKIPEDIA_PAGE_CLASS,
				WikipediaPageCassandraHelper.COLUMN_FAMILLY);
	}

	// ---------------------------------------------------------------------

	public void close() {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------------

	public void init(InformationStoreParameters parameters) {
		this.parameters = parameters;
		this.daoFactory = new CassandraDAOFactory(parameters);
		this.queryResolver = new CassandraQueryResolver();
		this.queryResolver.init();
	}

	// ------------------------------------------------------------------------

	public boolean test() {
		// TODO Auto-generated method stub
		return false;
	}

	// ------------------------------------------------------------------------

	public Resource get(String URI) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------

	public Resource get(Selector selector) {

		CassandraDAO dao = this.daoFactory.build(selector);

		Resource resource = dao.read(selector.getProperty(SelectorHelper.URI));
		return resource;
	}

	// ------------------------------------------------------------------------

	public void remove(Selector selector) {

		CassandraDAO dao = this.daoFactory.build(selector);

		dao.remove(selector.getProperty(SelectorHelper.URI));

	}

	// ------------------------------------------------------------------------

	public List<String> query(String queryExpression) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------

	public void put(Resource resource, Context context) {
		CassandraDAO dao = this.daoFactory.build(resource);

		dao.create(resource, context);

	}

	// ------------------------------------------------------------------------

	public InformationStoreParameters getParameters() {

		return this.parameters;
	}

	// ------------------------------------------------------------------------

	public void update(Resource resource) {
		// TODO Auto-generated method stub
	}

	// ------------------------------------------------------------------------

	public Content<String> getContent(Selector selector) {
		ContentCassandraDAO dao = new ContentCassandraDAO();
		dao.init();
		return dao.getContent(selector);
	}

	// ------------------------------------------------------------------------

	public Content<String> getAnnotatedContent(Selector selector) {
		AnnotatedContenCassandraDAO dao = new AnnotatedContenCassandraDAO();
		dao.init();
		return dao.getAnnotatedContent(selector);
	}

	// ------------------------------------------------------------------------

	public void setContent(Selector selector, Content<String> content) {
		CassandraDAO dao = this.daoFactory.build(selector);
		dao.setContent(selector, content);
	}

	// ------------------------------------------------------------------------

	public void setAnnotatedContent(Selector selector,
			Content<String> annotatedContent) {

		AnnotatedContenCassandraDAO dao = new AnnotatedContenCassandraDAO();
		dao.init();
		dao.setAnnotatedContent(selector, annotatedContent);

	}

	// ------------------------------------------------------------------------

	@Override
	public boolean exists(Selector selector) {
		System.out.println("llama a exists > " + selector);
		return this.queryResolver.exists(selector);
	}

	// ------------------------------------------------------------------------

	@Override
	public SearchSelectResult query(SelectExpression selectionExpression,
			SearchContext searchContext) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------

}
