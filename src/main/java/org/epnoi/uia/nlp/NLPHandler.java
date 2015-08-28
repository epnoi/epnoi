package org.epnoi.uia.nlp;

import java.util.logging.Logger;

import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.nlp.gate.GATEInitializer;
import org.epnoi.uia.parameterization.ParametersModel;

public class NLPHandler {
	private Core core;
	private ParametersModel parameters;
	private NLPProcessorsPool pool;

	private static final Logger logger = Logger.getLogger(NLPHandler.class
			.getName());

	// ----------------------------------------------------------------------------------------------------------

	public void init(Core core, ParametersModel parameters) {
		logger.info("Initializing the NLPHandler");
		this.core = core;
		this.parameters = parameters;
		GATEInitializer gateInitializer = new GATEInitializer();
		gateInitializer.init(parameters);
		this.pool = new NLPProcessorsPool();

	}
	
	// ----------------------------------------------------------------------------------------------------------

	
	public void process(String content) throws EpnoiResourceAccessException{
		NLPProcessor processor=null;
		
			processor = pool.borrowProcessor();
		
		processor.process(content);
		pool.returnProcessor(processor);
	}

	// ----------------------------------------------------------------------------------------------------------
	
	
}
