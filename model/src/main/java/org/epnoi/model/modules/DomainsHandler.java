package org.epnoi.model.modules;

import java.util.List;

import org.epnoi.model.Domain;

public interface DomainsHandler {

	void init(Core core);

	List<String> gather(Domain domain);

}