package org.epnoi.informationhandler.wrappers;

import org.epnoi.informationhandler.wrappers.exception.WrapperNotFoundException;
import org.epnoi.model.*;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.*;

import java.util.HashMap;

public class WrapperFactory {
	private HashMap<String, Wrapper> wrappersByClass;
	private HashMap<String, Wrapper> wrappersByType;
	private Core core;

	// -------------------------------------------------------------------------------------------------------------

	public WrapperFactory(Core core) {
		this.core = core;

		this.wrappersByClass = new HashMap<String, Wrapper>();
		this.wrappersByType = new HashMap<String, Wrapper>();

		this.wrappersByClass.put(InformationSource.class.getName(),
				new InformationSourceWrapper(this.core));
		this.wrappersByClass.put(Feed.class.getName(), new FeedWrapper(
				this.core));
		this.wrappersByClass.put(User.class.getName(), new UserWrapper(
				this.core));
		this.wrappersByClass.put(InformationSourceSubscription.class.getName(),
				new InformationSourceSubscriptionWrapper(this.core));
		this.wrappersByClass.put(Item.class.getName(), new ItemWrapper(
				this.core));
		this.wrappersByClass.put(Annotation.class.getName(),
				new AnnotationWrapper(this.core));

		this.wrappersByClass.put(Paper.class.getName(), new PaperWrapper(
				this.core));
		this.wrappersByClass.put(ResearchObject.class.getName(),
				new ResearchObjectWrapper(this.core));

		this.wrappersByClass.put(WikipediaPage.class.getName(),
				new WikipediaPageWrapper(this.core));

		this.wrappersByClass.put(Term.class.getName(), new TermWrapper(
				this.core));

		this.wrappersByClass.put(RelationalSentencesCorpus.class.getName(),
				new RelationalSentencesCorpusWrapper(this.core));

		this.wrappersByClass.put(Domain.class.getName(), new DomainWrapper(
				this.core));

		this.wrappersByClass.put(RelationsTable.class.getName(),
				new RelationsTableWrapper(this.core));

		this.wrappersByClass.put(WikidataView.class.getName(),
				new WikidataViewWrapper(this.core));

		// ------------------------------------------------------------------------------------------------------------------------------

		this.wrappersByType.put(UserRDFHelper.USER_CLASS, new UserWrapper(
				this.core));
		this.wrappersByType.put(SearchRDFHelper.SEARCH_CLASS,
				new SearchWrapper(this.core));
		this.wrappersByType.put(
				InformationSourceRDFHelper.INFORMATION_SOURCE_CLASS,
				new InformationSourceWrapper(this.core));
		this.wrappersByType
				.put(InformationSourceSubscriptionRDFHelper.INFORMATION_SOURCE_SUBSCRIPTION_CLASS,
						new InformationSourceSubscriptionWrapper(this.core));
		this.wrappersByType.put(FeedRDFHelper.FEED_CLASS, new FeedWrapper(
				this.core));
		this.wrappersByType.put(FeedRDFHelper.ITEM_CLASS, new ItemWrapper(
				this.core));

		this.wrappersByType.put(AnnotationRDFHelper.ANNOTATION_CLASS,
				new AnnotationWrapper(this.core));
		this.wrappersByType.put(RDFHelper.PAPER_CLASS, new PaperWrapper(
				this.core));

		this.wrappersByType.put(RDFHelper.RESEARCH_OBJECT_CLASS,
				new ResearchObjectWrapper(this.core));

		this.wrappersByType.put(RDFHelper.WIKIPEDIA_PAGE_CLASS,
				new WikipediaPageWrapper(this.core));

		this.wrappersByType.put(RDFHelper.TERM_CLASS,
				new TermWrapper(this.core));

		this.wrappersByType.put(RDFHelper.RELATIONAL_SENTECES_CORPUS_CLASS,
				new RelationalSentencesCorpusWrapper(this.core));

		this.wrappersByType.put(RDFHelper.DOMAIN_CLASS, new DomainWrapper(
				this.core));
		this.wrappersByType.put(RDFHelper.RELATIONS_TABLE_CLASS,
				new RelationsTableWrapper(this.core));

		this.wrappersByType.put(RDFHelper.WIKIDATA_VIEW_CLASS,
				new WikidataViewWrapper(this.core));
	}

	// -------------------------------------------------------------------------------------------------------------

	public Wrapper build(Resource resource) throws WrapperNotFoundException {
		Wrapper wrapper = this.wrappersByClass.get(resource.getClass()
				.getName());
		if (wrapper == null) {
			throw new WrapperNotFoundException(
					"There is no wrapper defined for a "
							+ resource.getClass().getName());
		}
		return wrapper;
	}

	// -------------------------------------------------------------------------------------------------------------

	public Wrapper build(String resourceType) throws WrapperNotFoundException {

		Wrapper wrapper = this.wrappersByType.get(resourceType);
		if (wrapper == null) {
			throw new WrapperNotFoundException(
					"There is no wrapper defined for a " + resourceType);
		}
		return wrapper;
	}

}
