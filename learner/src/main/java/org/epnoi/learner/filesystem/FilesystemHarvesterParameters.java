package org.epnoi.learner.filesystem;

import org.epnoi.model.commons.Parameters;

public class FilesystemHarvesterParameters extends Parameters<Object> {

	public static final String CORPUS_LABEL = "DOMAIN_LABEL"; //How the harvested items are labeled to be discovered later
	public static final String VERBOSE = "VERBOSE";
	public static final String FILEPATH = "FILEPATH"; //Filepath were the files to be harvested are deployed
	public static final String OVERWRITE = "OVERWRITE";
	public static final String CORPUS_URI = "CORPUS_URI";
	public static final String DOMAIN_URI = "DOMAIN_URI";
	//---------------------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "FilesystemHarvesterParameters [parmaters:"
				+ super.parameters.entrySet() + "]";
	}

}
