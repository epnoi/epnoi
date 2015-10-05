package org.epnoi.uia.informationstore.dao.cassandra;

public class TermCassandraHelper {
	/*
	 * private int length; private String[] words; private long ocurrences;
	 * private long ocurrencesAsSubterm; private long numberOfSuperterns;
	 * private double cValue; private double domainConsensus; private double
	 * domainPertinence;
	 * 
	 * private double termhood;
	 */
	public static final String COLUMN_FAMILY = "Term";

	public static final String LENGTH = "LENGTH";
	public static final String WORDS = "WORDS";
	public static final String WORD = "WORD";
	public static final String OCURRENCES = "OCURRENCES";
	public static final String OCURRENCES_AS_SUBTERM = "OCURRENCESASSUBTERM";
	public static final String NUMBER_OF_SUPERTERMS = "NUMBER_OF_SUPERTERMS";
	public static final String CVALUE = "CVALUE";
	public static final String DOMAIN_CONSENSUS = "DOMAIN_CONSENSUS";
	public static final String DOMAIN_PERTINENCE = "DOMAIN_PERTINENCE";
	public static final String TERMHOOD = "TERMHOOD";
	public static final String TERM_PROBABILITY = "TERM_PROBABILITY";

}
