package org.epnoi.uia.informationaccess.wrapper;

import epnoi.model.Context;
import epnoi.model.Resource;

public interface Wrapper {
	public void put(Resource resource);

	public void put(Resource resource, Context context);

	public void remove(String URI);

	public void update(Resource resource);

	public Resource get(String URI);

}
