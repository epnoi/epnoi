package org.epnoi.model.modules;

import org.epnoi.model.KnowledgeBase;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;

public interface KnowldedgeBaseHandler {

	void init() throws EpnoiInitializationException;

	KnowledgeBase getKnowledgeBase() throws EpnoiInitializationException, EpnoiResourceAccessException;

	boolean isKnowledgeBaseInitialized();

	//KnowledgeBaseParameters getKnowledgeBaseParameters();

}