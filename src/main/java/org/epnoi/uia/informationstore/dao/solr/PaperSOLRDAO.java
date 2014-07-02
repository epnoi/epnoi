package org.epnoi.uia.informationstore.dao.solr;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.epnoi.model.Context;
import org.epnoi.model.Feed;
import org.epnoi.model.Paper;
import org.epnoi.model.Resource;
import org.epnoi.uia.commons.DateConverter;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;

public class PaperSOLRDAO extends SOLRDAO {

	// ---------------------------------------------------------------------------------------------------

	public void create(Resource resource) {
		create(resource, null);

	}

	// --------------------------------------------------------------------------------

	public void create(Resource resource, Context context) {
		Paper paper = (Paper) resource;

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

		SolrInputDocument document = _indexItem(paper, context);

		try {
			this.server.add(document);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			this.server.commit();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// --------------------------------------------------------------------------------

	private SolrInputDocument _indexItem(Paper paper, Context context) {

		SolrInputDocument newDocument = new SolrInputDocument();

		newDocument.setField(SOLRDAOHelper.URI_PROPERTY, paper.getURI());
		newDocument.setField(SOLRDAOHelper.ID_PROPERTY, paper.getURI());

		/*
		 * 1995-12-31T23:59:59Z
		 * 
		 * 
		 * METER LA FECHA!!!! SimpleDateFormat dateFormat = new
		 * SimpleDateFormat( "yyyy-MM-dd"); Date date =
		 * dateFormat.parse(item.getPubDate());
		 * 
		 * newDocument.addField(SOLRDAOHelper.DESCRIPTION_PROPERTY,
		 * item.getDescription());
		 */

		newDocument.addField(SOLRDAOHelper.DESCRIPTION_PROPERTY,
				paper.getDescription());

		if (context != null) {
			String content = (String) context.getElements().get(paper.getURI());
			newDocument.addField(SOLRDAOHelper.CONTENT_PROPERTY, content);
			
		}
		
		newDocument.addField(SOLRDAOHelper.DATE_PROPERTY,
				DateConverter.convertDateFormat(paper.getPubDate()));
		newDocument.addField(SOLRDAOHelper.INFORMATION_SOURCE_NAME_PROPERTY,
				context.getParameters().get(Context.INFORMATION_SOURCE_NAME));
		
		newDocument.addField(SOLRDAOHelper.TYPE_PROPERTY, RDFHelper.PAPER_CLASS);

		
		
		return newDocument;

	}

	// ---------------------------------------------------------------------------------------------------

	public void update(Resource resource) {
		Feed feed = (Feed) resource;
		// TODO to be done
	}

	// ---------------------------------------------------------------------------------------------------

	public Feed read(String URI) {

		Feed feed = new Feed();
		feed.setURI(URI);

		return feed;
	}

	// ---------------------------------------------------------------------------------------------------

	public Boolean exists(String URI) {
		boolean exists = false;
		/*
		 * Node foo1 = NodeFactory.createURI(URI);
		 * 
		 * return graph.find(new Triple(foo1, Node.ANY, Node.ANY)).hasNext();
		 */
		return exists;
	}

	// ---------------------------------------------------------------------------------------------------

}
