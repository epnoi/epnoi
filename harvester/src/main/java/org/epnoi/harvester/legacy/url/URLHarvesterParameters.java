package org.epnoi.harvester.legacy.url;

import org.epnoi.model.commons.Parameters;

public class URLHarvesterParameters extends Parameters<Object> {

	public static final String VERBOSE_PARAMETER = "VERBOSE_PARAMETER";
	public static final String OVERWRITE_PARAMETER = "OVERWRITE_PARAMETER";

	// ---------------------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "URLHarvesterParameters [parmaters:"
				+ super.parameters.entrySet() + "]";
	}

}
