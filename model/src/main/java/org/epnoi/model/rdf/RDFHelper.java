package org.epnoi.model.rdf;

public class RDFHelper {
	public static final String PORT="PORT";
	public static final String HOST="HOST";
	public static final String GRAPH="GRAPH";
	
	
	public static final String TYPE_PROPERTY ="http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	public static final String COMMENT_PROPERTY ="http://www.w3.org/1999/02/22-rdf-syntax-ns#comment";
	public static final String LABEL_PROPERTY = "http://www.w3.org/2000/01/rdf-schema#label";
	
	
	public static final String CLASS_CLASS ="http://www.w3.org/2000/01/rdf-schema#Class";
	public static final String URL_PROPERTY = "http://www.epnoi.org/ontology/epnoi#URL";
	public static final String NAME_PROPERTY = "http://www.epnoi.org/ontology/epnoi#name";
	
	public static final String PUBDATE_PROPERTY="http://www.epnoi.org/ontology#pubDate";
	
	public static final String PAPER_CLASS = "http://www.epnoi.org/ontology#Paper";
	public static final String RESEARCH_OBJECT_CLASS = "http://www.epnoi.org/ontology#ResearchObject";
	public static final String WIKIPEDIA_PAGE_CLASS = "http://www.epnoi.org/ontology#WikipediaPage";
	public static final String TERM_CLASS = "http://www.epnoi.org/ontology#Term";
	public static final String RELATIONAL_SENTECES_CORPUS_CLASS = "http://www.epnoi.org/ontology#RelationalSentencesCorpus";
	
	public static final String DOMAIN_CLASS = "http://www.epnoi.org/ontology#Domain";
	public static final String RELATIONS_TABLE_CLASS = "http://www.epnoi.org/ontology#RelationsTable";
	public static final String WIKIDATA_VIEW_CLASS = "http://www.epnoi.org/ontology#WikidataView";
	
	//hasResources property for Domain (domain-hasResources-research object)
	public static final String HAS_RESOURCES_PROPERTY = "http://www.epnoi.org/ontology#hasResources";
}
