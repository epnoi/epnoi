package org.epnoi.uia.informationaccess.wrapper;

import epnoi.model.Resource;

public interface Wrapper {
	public void put(Resource resource);

	public Resource get(String URI);

}
