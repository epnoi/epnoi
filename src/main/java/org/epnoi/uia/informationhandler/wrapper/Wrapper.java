package org.epnoi.uia.informationhandler.wrapper;

import org.epnoi.model.Context;
import org.epnoi.model.Resource;

public interface Wrapper {

	public void put(Resource resource, Context context);

	public void remove(String URI);

	public void update(Resource resource);

	public Resource get(String URI);
	
	public boolean exists(String URI);

}
