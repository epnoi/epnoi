package org.epnoi.uia.informationhandler.wrapper;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.Resource;
import org.epnoi.model.Term;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.exceptions.EpnoiInitializationException;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.cassandra.RelationalSentencesCorpusCassandraDAO;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.relations.RelationalSentence;
import org.epnoi.uia.learner.relations.RelationalSentencesCorpus;
import org.epnoi.uia.learner.relations.lexical.LexicalRelationalPatternGenerator;

public class RelationalSentencesCorpusWrapper implements Wrapper {
	private Core core;

	RelationalSentencesCorpusWrapper(Core core) {
		this.core = core;
	}

	// ------------------------------------------------------------------------

	@Override
	public void put(Resource resource, Context context) {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);
		informationStore.put(resource, context);

		informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
		informationStore.put(resource, context);
	}

	// ------------------------------------------------------------------------

	@Override
	public void remove(String URI) {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE,
				RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		informationStore.remove(selector);

		informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.CASSANDRA_INFORMATION_STORE).get(0);

		informationStore.remove(selector);
	}

	// ------------------------------------------------------------------------

	@Override
	public void update(Resource resource) {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------------

	@Override
	public Resource get(String URI) {
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE,
				RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);

		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.CASSANDRA_INFORMATION_STORE)
				.get(0);

		Resource cassandraItem = informationStore.get(selector);

		return cassandraItem;
	}

	// ------------------------------------------------------------------------

	@Override
	public boolean exists(String URI) {
		// TODO Auto-generated method stub
		return false;
	}

	// ------------------------------------------------------------------------

	@Override
	public Content<String> getContent(Selector selector) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------

	@Override
	public void setContent(Selector selector, Content<String> content) {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------------

	@Override
	public Content<Object> getAnnotatedContent(Selector selector) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------

	@Override
	public void setAnnotatedContent(Selector selector,
			Content<Object> annotatedContent) {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------------

	public static void main(String[] args) {
		Core core = CoreUtility.getUIACore();

		RelationalSentencesCorpus relationalSentencesCorpus = new RelationalSentencesCorpus();
		relationalSentencesCorpus.setDescription("The test corpus");
		relationalSentencesCorpus.setURI("http://thetestcorpus/drinventor");
		relationalSentencesCorpus.setType(RelationHelper.HYPERNYM);
		RelationalSentence relationalSentence = new RelationalSentence(
				new OffsetRangeSelector(0L, 5L), new OffsetRangeSelector(10L,
						15L), "Bla bla bla this is a relational sentence",
				"ANOTATED BLA BLA BLA");
		core.getInformationHandler().remove("http://thetestcorpus/drinventor",
				RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
		RelationalSentencesCorpusCassandraDAO relationalSentencesCorpusCassandraDAO = new RelationalSentencesCorpusCassandraDAO();
		relationalSentencesCorpusCassandraDAO.init();
		System.out.println(relationalSentencesCorpusCassandraDAO
				._createRelationalSentenceRepresentation(relationalSentence));

		RelationalSentence rs = relationalSentencesCorpusCassandraDAO
				._readRelationalSentenceRepresentation("[4,55][666,7777][Bla bla bla this is a relational sentence][ANOTATED BLA BLA BLA]");
		System.out.println("----> " + rs);

		String representation = "[4444,555][66,7]Bla bla bla this is another relational sentence";
		rs = relationalSentencesCorpusCassandraDAO
				._readRelationalSentenceRepresentation(representation);
		System.out.println("----> " + rs);

		/*
		 * System.out.println("Are the same? " +
		 * representation.equals(relationalSentencesCorpusCassandraDAO
		 * ._createRelationalSentenceRepresentation(rs)));
		 */

		relationalSentencesCorpus.getSentences().add(relationalSentence);

		relationalSentencesCorpus.getSentences().add(rs);

		core.getInformationHandler().put(relationalSentencesCorpus,
				Context.getEmptyContext());

		RelationalSentencesCorpus readedCorpus = (RelationalSentencesCorpus) core
				.getInformationHandler().get(
						relationalSentencesCorpus.getURI(),
						RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS);
		System.out.println("The readed relational sentence corpus "
				+ readedCorpus);

	}

}
