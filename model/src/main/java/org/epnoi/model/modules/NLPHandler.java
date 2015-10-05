package org.epnoi.model.modules;

import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.model.parameterization.ParametersModel;

import gate.Document;

public interface NLPHandler {

	void init(Core core, ParametersModel parameters);

	Document process(String content) throws EpnoiResourceAccessException;

}