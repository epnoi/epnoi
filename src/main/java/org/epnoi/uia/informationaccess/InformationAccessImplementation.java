package org.epnoi.uia.informationaccess;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationaccess.wrapper.Wrapper;
import org.epnoi.uia.informationaccess.wrapper.WrapperFactory;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;
import org.epnoi.uia.parameterization.ParametersModel;
import org.epnoi.uia.search.select.SearchSelectResult;
import org.epnoi.uia.search.select.SelectExpression;

import epnoi.model.Context;
import epnoi.model.Resource;

public class InformationAccessImplementation implements InformationAccess {

	private Core core;

	private WrapperFactory wrapperFactory;

	// ---------------------------------------------------------------------------

	public InformationAccessImplementation(Core core) {
		this.core = core;
		this.wrapperFactory = new WrapperFactory(core);

	}

	// ---------------------------------------------------------------------------

	public void put(Resource resource) {
		Wrapper wrapper = this.wrapperFactory.build(resource);
		wrapper.put(resource);

	}

	// ---------------------------------------------------------------------------

	public void put(Resource resource, Context context) {
		Wrapper wrapper = this.wrapperFactory.build(resource);
		wrapper.put(resource, context);

	}

	// ---------------------------------------------------------------------------

	public Resource get(String URI) {
		// TODO Auto-generated method stub
		return null;
	}

	// ---------------------------------------------------------------------------

	public Resource get(String URI, String resourceType) {
		// TODO Auto-generated method stub
		Wrapper wrapper = this.wrapperFactory.build(resourceType);
		return wrapper.get(URI);
	}

	// ---------------------------------------------------------------------------

	public void init(ParametersModel parameters) {
		// TODO Auto-generated method stub

	}

	// ---------------------------------------------------------------------------

	public SearchSelectResult query(SelectExpression queryExpression) {
		return new SearchSelectResult(null);
	}

	// ---------------------------------------------------------------------------

	public void addInformationStore(InformationStore informationStore) {
		// TODO Auto-generated method stub

	}

	// ---------------------------------------------------------------------------

	public void removeInformationStore(String URI) {
		// TODO Auto-generated method stub

	}

}
