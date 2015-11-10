package org.epnoi.model.modules;

import org.epnoi.model.Domain;
import org.epnoi.model.exceptions.EpnoiInitializationException;

import java.util.List;

public interface DomainsHandler {

	void init() throws EpnoiInitializationException;

	List<String> gather(Domain domain);

}