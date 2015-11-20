package org.epnoi.informationhandler.wrappers;

import org.epnoi.model.*;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.InformationStore;
import org.epnoi.model.modules.InformationStoreHelper;
import org.epnoi.model.rdf.AnnotationRDFHelper;
import org.epnoi.uia.informationstore.SelectorHelper;

public class AnnotationWrapper implements Wrapper {
	Core core;

	// -------------------------------------------------------------------------------------------------------------

	public AnnotationWrapper(Core core) {
		this.core = core;
	}

	// -------------------------------------------------------------------------------------------------------------
	public void put(Resource resource, Context context) {

		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		informationStore.put(resource, context);

	}

	// -------------------------------------------------------------------------------------------------------------

	public Resource get(String URI) {
		Annotation annotation = new Annotation();
		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE,
				AnnotationRDFHelper.ANNOTATION_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		annotation = (Annotation) informationStore.get(selector);

		return annotation;
	}

	// -------------------------------------------------------------------------------------

	public void remove(String URI) {
		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE,
				AnnotationRDFHelper.ANNOTATION_CLASS);
		selector.setProperty(SelectorHelper.URI, URI);
		informationStore.remove(selector);

	}

	// -------------------------------------------------------------------------------------

	@Override
	public void update(Resource resource) {
		// TODO Auto-generated method stub

	}

	// -------------------------------------------------------------------------------------

	@Override
	public boolean exists(String URI) {
		// TODO Auto-generated method stub
		return false;
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
	
	
}
