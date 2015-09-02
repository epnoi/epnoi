package org.epnoi.uia.nlp;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.epnoi.uia.core.Core;

public class PooledNLPProcessorFactory implements PooledObjectFactory<NLPProcessor> {
	Core core;
	
	public PooledNLPProcessorFactory(Core core) {
		this.core=core;
		}

	//----------------------------------------------------------------------------
	
	@Override
	public void activateObject(PooledObject<NLPProcessor> object)
			throws Exception {
		//System.out.println("POOL:> ACTIVATING AN OBJECT! "+object);
		
	}
	
	//----------------------------------------------------------------------------

	@Override
	public void destroyObject(PooledObject<NLPProcessor> object) throws Exception {
	//	System.out.println("DESTROYING AN OBJECT!");
		//System.out.println("DESTROYING AN OBJECT!");
	}
	
	//----------------------------------------------------------------------------

	@Override
	public PooledObject<NLPProcessor> makeObject() throws Exception {
		
		//System.out.println("POOL:> MAKING AN OBJECT! "+this.core);
		NLPProcessor newNLPProcessor = new NLPProcessor();
		newNLPProcessor.init(core);
		return new DefaultPooledObject<NLPProcessor>(newNLPProcessor);
	}

	//----------------------------------------------------------------------------
	
	@Override
	public void passivateObject(PooledObject<NLPProcessor> object)
			throws Exception {
		//System.out.println("POOL:> PASSIVATING AN OBJECT! "+object);
		
	}
	
	//----------------------------------------------------------------------------

	@Override
	public boolean validateObject(PooledObject<NLPProcessor> object) {
		//System.out.println("POOL:> VATING AN OBJECT! "+object);
				return true;
	}
	
	//----------------------------------------------------------------------------

}
