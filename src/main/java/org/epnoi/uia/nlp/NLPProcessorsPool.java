package org.epnoi.uia.nlp;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.parameterization.ParametersModel;

public class NLPProcessorsPool {
	Core core;
	ParametersModel parameters;
	GenericObjectPool<NLPProcessor> pool;

	// -------------------------------------------------------------------------

	public void init(Core core, ParametersModel parameters) {

		this.core = core;
		this.parameters = parameters;
		_initPool();

	}

	private void _initPool() {
		pool = new GenericObjectPool<NLPProcessor>(
				new PooledNLPProcessorFactory(core));
		pool.setBlockWhenExhausted(true);
		pool.setMaxTotal(this.parameters.getNlp().getNumberOfProcessors());
		int i = this.parameters.getNlp().getNumberOfProcessors();
		while (i > 0) {

			try {
				pool.addObject();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			}
			i--;

		}
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
