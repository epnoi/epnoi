package org.epnoi.uia.informationstore;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.Selector;
import org.epnoi.model.modules.InformationStore;
import org.epnoi.model.parameterization.InformationStoreParameters;
import org.epnoi.model.rdf.FeedRDFHelper;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.model.search.SearchContext;
import org.epnoi.model.search.SearchSelectResult;
import org.epnoi.model.search.SelectExpression;
import org.epnoi.uia.informationstore.dao.cassandra.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CassandraInformationStore implements InformationStore {
	private InformationStoreParameters parameters;
	private CassandraDAOFactory daoFactory;
	private CassandraQueryResolver queryResolver;
	

	public static final Map<String, String> typesTable = new HashMap<>();
	static {
		typesTable.put(RDFHelper.PAPER_CLASS,
				PaperCassandraHelper.COLUMN_FAMILY);
		typesTable.put(FeedRDFHelper.FEED_CLASS,
				FeedCassandraHelper.COLUMN_FAMILY);
		typesTable.put(FeedRDFHelper.ITEM_CLASS,
				ItemCassandraHelper.COLUMN_FAMILY);
		typesTable.put(RDFHelper.WIKIPEDIA_PAGE_CLASS,
				WikipediaPageCassandraHelper.COLUMN_FAMILY);
		typesTable.put(RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS,
				RelationalSentencesCorpusCassandraHelper.COLUMN_FAMILY);
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
		dao = null;
		return resource;
	}

	// ------------------------------------------------------------------------

	public void remove(Selector selector) {

		CassandraDAO dao = this.daoFactory.build(selector);

		dao.remove(selector.getProperty(SelectorHelper.URI));
		dao = null;
		
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
		dao = null;

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
		Content<String> content = dao.getContent(selector);
		dao = null;
		return content;
	}

	// ------------------------------------------------------------------------

	public Content<String> getAnnotatedContent(Selector selector) {
		CassandraDAO dao = this.daoFactory.build(selector);
		dao.init();
		Content<String> content = dao.getAnnotatedContent(selector);
		dao = null;
		return content;
	}

	// ------------------------------------------------------------------------

	public void setContent(Selector selector, Content<String> content) {
		CassandraDAO dao = this.daoFactory.build(selector);
		dao.setContent(selector, content);
		dao = null;
	}

	// ------------------------------------------------------------------------

	public void setAnnotatedContent(Selector selector,
			Content<String> annotatedContent) {

		CassandraDAO dao = this.daoFactory.build(selector);
		dao.init();
		dao.setAnnotatedContent(selector, annotatedContent);
		dao = null;

	}

	// ------------------------------------------------------------------------

	@Override
	public boolean exists(Selector selector) {
		CassandraDAO dao = this.daoFactory.build(selector);
		dao.init();
		return dao.exists(selector);

	}

	// ------------------------------------------------------------------------

	@Override
	public SearchSelectResult query(SelectExpression selectionExpression,
			SearchContext searchContext) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public CassandraQueryResolver getQueryResolver() {
		return queryResolver;
	}

	public void setQueryResolver(CassandraQueryResolver queryResolver) {
		this.queryResolver = queryResolver;
	}
	// ------------------------------------------------------------------------

}
