package org.epnoi.model.modules;

import org.epnoi.model.commons.Parameters;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="knowledgeBaseParameters")
public class KnowledgeBaseParameters extends Parameters<Object> {
	public static String RETRIEVE_WIKIDATA_VIEW = "RETRIEVE_WIKIDATA_VIEW";
	public static String WIKIDATA_PARAMETERS = "WIKIDATA_PARAMETERS";
	public static String WORDNET_PARAMETERS = "WORDNET_PARAMETERS";
	public static String CONSIDER_WORDNET= "CONSIDER_WORDNET";
	public static String CONSIDER_WIKIDATA = "CONSIDER_WIKIDATA";
	public static String LAZY= "LAZY";
	
}
