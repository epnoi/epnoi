package org.epnoi.uia.nlp;

import gate.Document;

import java.util.logging.Logger;

import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
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
		this.pool.init(core, parameters);

	}

	// ----------------------------------------------------------------------------------------------------------

	public Document process(String content) throws EpnoiResourceAccessException {
		NLPProcessor processor = null;
System.out.println("ENTRA!");
		processor = pool.borrowProcessor();
		System.out.println("SALE!");
		Document document = processor.process(content);
		pool.returnProcessor(processor);
		return document;
	}

	// ----------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		Core core = CoreUtility.getUIACore();
		System.out.println(core.getParameters().getNlp());
		try {
			System.out.println(">> "
					+ core.getNLPHandler().process("My house is big")
							.toString());
		} catch (EpnoiResourceAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
