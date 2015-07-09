package org.epnoi.uia.harvester.filesystem;

import org.epnoi.uia.commons.Parameters;

public class FilesystemHarvesterParameters extends Parameters<Object> {

	public static final String CORPUS_LABEL = "DOMAIN_LABEL_PARAMETER"; //How the harvested items are labeled to be discovered later
	public static final String VERBOSE = "VERBOSE_PARAMETER"; 
	public static final String FILEPATH = "FILEPATH_PARAMETER"; //Filepath were the files to be harvested are deployed
	public static final String OVERWRITE = "OVERWRITE_PARAMETER";
	public static final String CORPUS_URI = "CORPUS_URI_PARAMETER";

	//---------------------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "FilesystemHarvesterParameters [parmaters:"
				+ super.parameters.entrySet() + "]";
	}

}
