package org.epnoi.model.modules;

import gate.Document;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;

public interface NLPHandler {

	void init()throws EpnoiInitializationException;

	Document process(String content) throws EpnoiResourceAccessException;

}