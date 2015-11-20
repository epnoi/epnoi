package org.epnoi.informationhandler.wrappers;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.Selector;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.InformationStore;
import org.epnoi.model.modules.InformationStoreHelper;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.informationstore.SelectorHelper;

public class ResearchObjectWrapper implements Wrapper {
	Core core;

	// -------------------------------------------------------------------------------------------------------------

	public ResearchObjectWrapper(Core core) {
		this.core = core;
	}

	// -------------------------------------------------------------------------------------------------------------
	public void put(Resource resource, Context context) {

		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
		// System.out.println("--------------------------------------------->  "+informationStore);
		informationStore.put(resource, context);
		/*
		informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.SOLR_INFORMATION_STORE).get(0);

		informationStore.put(resource, context);
*/
	}

	// ------------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------------------------------------

	public Resource get(String URI) {
		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE,
				RDFHelper.RESEARCH_OBJECT_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		return informationStore.get(selector);
	}

	// -------------------------------------------------------------------------------------

	public void remove(String URI) {

		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE,
				RDFHelper.RESEARCH_OBJECT_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		informationStore.remove(selector);
/*
		informationStore = this.core.getInformationStoresByType(
				InformationStoreHelper.SOLR_INFORMATION_STORE).get(0);
		informationStore.remove(selector);
*/
	}

	// -------------------------------------------------------------------------------------

	@Override
	public void update(Resource resource) {
		this.remove(resource.getUri());
		this.put(resource, Context.getEmptyContext());

	}

	// -------------------------------------------------------------------------------------

	@Override
	public boolean exists(String URI) {
		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE,
				RDFHelper.RESEARCH_OBJECT_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		return informationStore.exists(selector);

	}

	// -------------------------------------------------------------------------------------

	@Override
	public Content<String> getContent(Selector selector) {
		// TODO Auto-generated method stub
		return null;
	}

	// -------------------------------------------------------------------------------------

	@Override
	public void setContent(Selector selector, Content<String> content) {
		// TODO Auto-generated method stub

	}

	// -------------------------------------------------------------------------------------

	@Override
	public Content<Object> getAnnotatedContent(Selector selector) {
		// TODO Auto-generated method stub
		return null;
	}

	// -------------------------------------------------------------------------------------

	@Override
	public void setAnnotatedContent(Selector selector,
			Content<Object> annotatedContent) {
		// TODO Auto-generated method stub

	}

	// -------------------------------------------------------------------------------------
	public static void main(String[] args) {
/*
 
 
		ResearchObject researchObject = new ResearchObject();
		researchObject.setURI("http://testResearchObject");
		researchObject.getAggregatedResources().add("http://resourceA");
		researchObject.getAggregatedResources().add("http://resourceB");
		researchObject.getDcProperties().addPropertyValue(
				DublinCoreRDFHelper.TITLE_PROPERTY,
				"First RO, loquetienesquebuscar");
		researchObject.getDcProperties().addPropertyValue(
				DublinCoreRDFHelper.DESCRIPTION_PROPERTY,
				"Description of the first RO");
		researchObject.getDcProperties().addPropertyValue(
				DublinCoreRDFHelper.DATE_PROPERTY, "2005-02-28T00:00:00Z");

		Context context = new Context();
		context.getParameters().put(Context.INFORMATION_SOURCE_NAME,
				"coreMainTest");
		Core core = CoreUtility.getUIACore();

		core.getInformationHandler().put(researchObject, context);

		Resource readedRO = core.getInformationHandler().get(
				"http://testResearchObject", RDFHelper.RESEARCH_OBJECT_CLASS);
		System.out.println("Readed RO >" + readedRO);

		
		if (core.getInformationHandler().contains(researchObject.getURI(),
				RDFHelper.RESEARCH_OBJECT_CLASS)) {
			System.out.println("The research object exists!");
		} else {
			System.out.println("It doesn't exist, something went wrong :(");
		}

		core.getInformationHandler().remove(researchObject);

		
		
		if (core.getInformationHandler().contains(researchObject.getURI(),
				RDFHelper.RESEARCH_OBJECT_CLASS)) {
			System.out.println("It exists, something went wrong :(");
		} else {
			System.out.println("The domain doesn't exist!");
			
		}
*/
	}
	

}
