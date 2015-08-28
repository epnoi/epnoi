package org.epnoi.uia.nlp;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;

public class PooledNLPProcessorFactory implements PooledObjectFactory<NLPProcessor> {

	//----------------------------------------------------------------------------
	
	@Override
	public void activateObject(PooledObject<NLPProcessor> object)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	//----------------------------------------------------------------------------

	@Override
	public void destroyObject(PooledObject<NLPProcessor> object) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	//----------------------------------------------------------------------------

	@Override
	public PooledObject<NLPProcessor> makeObject() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	//----------------------------------------------------------------------------
	
	@Override
	public void passivateObject(PooledObject<NLPProcessor> object)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	//----------------------------------------------------------------------------

	@Override
	public boolean validateObject(PooledObject<NLPProcessor> object) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//----------------------------------------------------------------------------

}
