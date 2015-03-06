package org.epnoi.uia.harvester.filesystem;

import org.epnoi.uia.commons.Parameters;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.exceptions.EpnoiInitializationException;

public class FilesystemHarvesterParameters extends Parameters<Object> {

	public static final String CORPUS_LABEL_PARAMETER = "DOMAIN_LABEL_PARAMETER"; //How the harvested items are labeled to be discovered later
	public static final String VERBOSE_PARAMETER = "VERBOSE_PARAMETER"; 
	public static final String FILEPATH_PARAMETER = "FILEPATH_PARAMETER"; //Filepath were the files to be harvested are deployed

	//---------------------------------------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "FilesystemHarvesterParameters [parmaters:"
				+ super.parameters.entrySet() + "]";
	}

}
