package org.epnoi.uia.informationaccess.wrapper;

import org.epnoi.model.Annotation;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.AnnotationRDFHelper;

public class AnnotationWrapper implements Wrapper {
	Core core;

	// -------------------------------------------------------------------------------------------------------------

	public AnnotationWrapper(Core core) {
		this.core = core;
	}

	// -------------------------------------------------------------------------------------------------------------
	public void put(Resource resource, Context context) {

		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		informationStore.put(resource, context);

	}

	// -------------------------------------------------------------------------------------------------------------

	public Resource get(String URI) {
		Annotation annotation = new Annotation();
		InformationStore informationStore = this.core
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
		InformationStore informationStore = this.core
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
}
