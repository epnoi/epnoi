package org.epnoi.uia.nlp;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.model.parameterization.ParametersModel;

public class NLPProcessorsPool {
	ParametersModel parameters;
	GenericObjectPool<NLPProcessor> pool;
	
	// -------------------------------------------------------------------------
	
	public void init(ParametersModel parameters){
		this.parameters = parameters;
		_initPool();
	
	}

	private void _initPool() {
		pool = new GenericObjectPool<NLPProcessor>(new PooledNLPProcessorFactory());
		pool.setBlockWhenExhausted(true);
		pool.setMaxTotal(1);
		NLPProcessor nlpProcessor = new NLPProcessor();
		
	}

	// -------------------------------------------------------------------------

	public NLPProcessor borrowProcessor() throws EpnoiResourceAccessException {
		try {
			NLPProcessor processor = this.pool.borrowObject();
			return processor;
		} catch (Exception e) {
			throw new EpnoiResourceAccessException(
					"There was a problem accessing the NLPProcessors pool");

		}
	}

	// -------------------------------------------------------------------------

	public void returnProcessor(NLPProcessor processor) {
		this.pool.returnObject(processor);
	}
	
	// -------------------------------------------------------------------------
}


