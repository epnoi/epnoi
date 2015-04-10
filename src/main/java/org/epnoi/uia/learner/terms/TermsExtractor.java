package org.epnoi.uia.learner.terms;

import gate.Annotation;
import gate.Document;
import gate.Factory;
import gate.Utils;
import gate.creole.ResourceInstantiationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.Term;
import org.epnoi.uia.commons.Parameters;
import org.epnoi.uia.commons.StringUtils;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.domains.DomainsHandler;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.DomainsGatherer;
import org.epnoi.uia.learner.DomainsTable;
import org.epnoi.uia.learner.OntologyLearningParameters;
import org.epnoi.uia.learner.nlp.gate.NLPAnnotationsHelper;

public class TermsExtractor {

	private static final Logger logger = Logger.getLogger(TermsExtractor.class
			.getName());
	private static final List<String> stopwords = Arrays.asList(new String[] {
			"comment", "comments", "proceedings", "example", "examples",
			"symposium", "conference", "copyright", "approach", "figure",
			"figures" });
	private static final int MIN_TERM_LENGTH = 4;
	private Core core;
	// private List<String> consideredDomains;
	private String targetDomain;
	private String consideredResources;
	// private Map<String, List<String>> resourcePerConsideredDomain;
	private TermsIndex termsIndex;
	private ResourcesIndex resourcesIndex;
	private DomainsIndex domainsIndex;
	private double cValueWeight = 0.5;
	private final double domainPertinenceWeight = 0.2;
	private final double domainConsensusWeight = 1 - cValueWeight
			- domainPertinenceWeight;

	OntologyLearningParameters parameters;

	private DomainsTable domainsTable;

	// -----------------------------------------------------------------------------------

	public void init(Core core, DomainsTable domainsTable,
			OntologyLearningParameters parameters) {
		logger.info("Initializing the TermExtractor with the following parameters");
		logger.info(parameters.toString());
		this.core = core;
		this.parameters = parameters;

		this.domainsTable = domainsTable;
		
		this.targetDomain = (String) parameters
				.getParameterValue(OntologyLearningParameters.TARGET_DOMAIN);
		this.termsIndex = new TermsIndex();
		this.termsIndex.init();
		this.resourcesIndex = new ResourcesIndex();
		this.resourcesIndex.init();
		this.domainsIndex = new DomainsIndex();
		this.domainsIndex.init();
	}

	// -----------------------------------------------------------------------------------

	public void indexResources() {

		for (String domain : this.domainsTable.getConsideredDomains()) {
			logger.info("Indexing the domain: " + domain);
			this._indexDomainResoures(domain);
		}

	}

	// -----------------------------------------------------------------------------------

	private void _indexDomainResoures(String domain) {
		List<String> resourcesURIs = this.domainsTable.getDomains().get(domain);
		System.out.println(" resourceURIS"+ resourcesURIs);
		for (String resourceURI : resourcesURIs) {
			logger.info("Indexing the resource " + resourceURI);
			_indexResource(domain, resourceURI);
		}
		long total = 0;
		for (AnnotatedWord<ResourceMetadata> resource : this.resourcesIndex
				.getResources(domain)) {
			total += resource.getAnnotation().getNumberOfTerms();
		}
		AnnotatedWord<DomainMetadata> indexedDomain = this.domainsIndex
				.lookUp(domain);
		indexedDomain.getAnnotation().setNumberOfTerms(total);
	}

	// -----------------------------------------------------------------------------------

	private void _indexResource(String domain, String URI) {
		Document annotatedDocument = (Document)retrieveAnnotatedDocument(URI).getContent();
		TermCandidateBuilder termCandidateBuilder = new TermCandidateBuilder(
				annotatedDocument);

		for (Annotation annotation : annotatedDocument.getAnnotations().get(
				NLPAnnotationsHelper.TERM_CANDIDATE)) {

			AnnotatedWord<TermMetadata> termCandidate = termCandidateBuilder
					.buildTermCandidate(annotation);
			String word = termCandidate.getWord();

			if ((word.length() > MIN_TERM_LENGTH) && !stopwords.contains(word)) {
				this.termsIndex.updateTerm(domain, termCandidate);
				this.resourcesIndex.updateTerm(domain, URI, termCandidate);

				for (AnnotatedWord<TermMetadata> subTerm : termCandidateBuilder
						.splitTermCandidate(termCandidate)) {
					this.resourcesIndex.updateTerm(domain, URI, subTerm);
				}

				this.domainsIndex.updateTerm(domain, URI);
			}
		}
	}

	// -----------------------------------------------------------------------------------

	private Content<Object> retrieveAnnotatedDocument(String URI) {

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.URI, URI);
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.PAPER_CLASS);
		selector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI, URI + "/"
				+ AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE);

		Content<Object> annotatedContent = core.getInformationHandler()
				.getAnnotatedContent(selector);
	/*
		Document document = null;
		try {
			document = (Document) Factory
					.createResource(
							"gate.corpora.DocumentImpl",
							Utils.featureMap(
									gate.Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME,
									(String)annotatedContent.getContent(),
									gate.Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME,
									"text/xml"));

		} catch (ResourceInstantiationException e) { // TODO Auto-generated
			logger.severe("Couldn't retrieve the GATE document that represents the annotated content of "
					+ URI);
			logger.severe(e.getMessage());
		}
		*/
		return annotatedContent;
	}

	// -----------------------------------------------------------------------------------

	public void extractTerms() {
		this.indexResources();
		this.calculateCValues();
		this.calculateDomainConsensus();
		this.calculateDomainPertinence();
		this.normalizeAnDeriveMeasures();
	}

	// -----------------------------------------------------------------------------------

	private void normalizeAnDeriveMeasures() {

		logger.info("Starting the normalization of cValue and Domain Consensus values");
		for (String domain : this.domainsTable.getConsideredDomains()) {
			double maxCValue = this.domainsIndex.lookUp(domain).getAnnotation()
					.getMaxCValue();
			double minCValue = this.domainsIndex.lookUp(domain).getAnnotation()
					.getMinCValue();

			double maxDomainConsesus = this.domainsIndex.lookUp(domain)
					.getAnnotation().getMaxDomainConsensus();
			double minDomainConsesus = this.domainsIndex.lookUp(domain)
					.getAnnotation().getMinDomainConsensus();

			for (AnnotatedWord<TermMetadata> termCandidate : this.termsIndex
					.getTermCandidates(domain)) {

				termCandidate.getAnnotation().setCValue(
						_normalize(termCandidate.getAnnotation().getCValue(),
								minCValue, maxCValue));
				termCandidate.getAnnotation().setDomainConsensus(
						_normalize(termCandidate.getAnnotation()
								.getDomainConsensus(), minDomainConsesus,
								maxDomainConsesus));

				//
				_termhoodCalculation(termCandidate);

			}

		}
	}

	// -----------------------------------------------------------------------------------

	private void _termhoodCalculation(AnnotatedWord<TermMetadata> termCandidate) {
		termCandidate.getAnnotation().setTermhood(
				termCandidate.getAnnotation().getCValue() * cValueWeight
						+ termCandidate.getAnnotation().getDomainConsensus()
						* domainConsensusWeight
						+ termCandidate.getAnnotation().getDomainPertinence()
						* domainPertinenceWeight);

	}

	// -----------------------------------------------------------------------------------

	private double _normalize(double value, double min, double max) {
		return (value - min) / (max - min);
	}

	// -----------------------------------------------------------------------------------

	private void showResult() {
		for (String domain : this.domainsTable.getConsideredDomains()) {

			System.out
					.println("Domains----------------------------------------------------------");

			for (AnnotatedWord<DomainMetadata> aDomain : this.domainsIndex
					.getDomains()) {
				System.out.println("domain> " + aDomain.getWord()
						+ "  #resources "
						+ aDomain.getAnnotation().getResources().size()
						+ "  #terms "
						+ aDomain.getAnnotation().getNumberOfTerms());

				System.out.println("---> "
						+ aDomain.getAnnotation().getResources());
			}

			System.out
					.println("=========================================================================================================================");

			System.out.println("Domain (" + domain + ")> " + domain);

			System.out
					.println("=========================================================================================================================");

			for (AnnotatedWord<TermMetadata> term : this.termsIndex
					.getTerms(domain)) {

				System.out.println("term(" + term.getWord() + ")> " + term);

			}
			System.out
					.println("=========================================================================================================================");

		}
	}

	// -----------------------------------------------------------------------------------

	private void storeResult() {
		System.out.println("Storing the Term Extraction result");

		for (AnnotatedWord<DomainMetadata> aDomain : this.domainsIndex
				.getDomains()) {

			System.out
					.println("=========================================================================================================================");
			System.out
					.println("=========================================================================================================================");

			for (AnnotatedWord<TermMetadata> term : this.termsIndex
					.getTerms(aDomain.getWord())) {

				// System.out.println("term(" + term.getWord() + ")> " + term);

				Term newTerm = new Term();
				// The term URI is obtained using an auxiliary function
				newTerm.setURI(Term.buildURI(term.getWord(), aDomain.getWord()));
				newTerm.setAnnotatedTerm(term);

				core.getInformationHandler().put(newTerm,
						Context.getEmptyContext());
				/*
				 * System.out .println("Labeling " + newTerm.getURI() + " as " +
				 * this.parameters
				 * .getParameterValue(OntologyLearningParameters.
				 * TARGET_DOMAIN));
				 */

				/*
				 * core.getAnnotationHandler() .label(newTerm.getURI(), (String)
				 * this.parameters
				 * .getParameterValue(OntologyLearningParameters.
				 * TARGET_DOMAIN));
				 */
				core.getAnnotationHandler().label(newTerm.getURI(),
						aDomain.getWord());
			}
			System.out
					.println("=========================================================================================================================");
		}
	}

	// -----------------------------------------------------------------------------------

	public void storeTable(TermsTable termsTable) {
		System.out.println("Storing a Terms Table");

		for (Term term : termsTable.getTerms()) {
			System.out.println("Storing " + term);
			core.getInformationHandler().put(term, Context.getEmptyContext());

			core.getAnnotationHandler().label(term.getURI(), this.targetDomain);
		}
		System.out
				.println("=========================================================================================================================");
	}

	// -----------------------------------------------------------------------------------

	private void calculateCValues() {

		logger.info("Starting the calculation of the cValues");
		for (String domain : this.domainsTable.getConsideredDomains()) {
			TermCandidateBuilder termCandidateBuilder = new TermCandidateBuilder(
					null);
			for (AnnotatedWord<TermMetadata> termCandidate : this.termsIndex
					.getTermCandidates(domain)) {

				for (AnnotatedWord<TermMetadata> subTerm : termCandidateBuilder
						.splitTermCandidate(termCandidate)) {
					this.termsIndex.updateSubTerm(domain, termCandidate,
							subTerm);

				}

			}

			for (AnnotatedWord<TermMetadata> termCandidate : this.termsIndex
					.getTermCandidates(domain)) {

				double cValue = CValueCalculator.calculateCValue(termCandidate);
				termCandidate.getAnnotation().setCValue(cValue);
				// System.out.println("el cvalue es " + cValue);
				if (cValue > this.domainsIndex.getDomain(domain)
						.getAnnotation().getMaxCValue()) {
					this.domainsIndex.getDomain(domain).getAnnotation()
							.setMaxCValue(cValue);
				} else if (cValue < this.domainsIndex.getDomain(domain)
						.getAnnotation().getMinCValue()) {
					this.domainsIndex.getDomain(domain).getAnnotation()
							.setMinCValue(cValue);
				}

			}

		}
	}

	// -----------------------------------------------------------------------------------

	private void calculateDomainPertinence() {
		logger.info("Calculating the domain pertinence");
		for (String domain : this.domainsTable.getConsideredDomains()) {
			long totalOcurrences = this.domainsIndex.lookUp(domain)
					.getAnnotation().getNumberOfTerms();

			for (AnnotatedWord<TermMetadata> termCandidate : this.termsIndex
					.getTermCandidates(domain)) {

				termCandidate.getAnnotation()
						.setTermProbability(
								((double) termCandidate.getAnnotation()
										.getOcurrences())
										/ ((double) totalOcurrences));
				List<Double> ocurrencesInOtherDomains = new ArrayList<>();
				for (String otherDomain : this.domainsTable
						.getConsideredDomains()) {
					AnnotatedWord<TermMetadata> term = this.termsIndex.lookUp(
							otherDomain, termCandidate.getWord());

					if (term != null) {

						ocurrencesInOtherDomains.add(((double) term
								.getAnnotation().getOcurrences())
								/ ((double) this.domainsIndex
										.lookUp(otherDomain).getAnnotation()
										.getNumberOfTerms()));
					}
				}

				double maxOcurrences = Collections
						.max(ocurrencesInOtherDomains);
				termCandidate.getAnnotation().setDomainPertinence(
						termCandidate.getAnnotation().getTermProbability()
								/ maxOcurrences);
			}
		}
	}

	// -----------------------------------------------------------------------------------

	private void calculateDomainConsensus() {
		logger.info("Calculating the domain pertinence");
		for (String domain : this.domainsTable.getConsideredDomains()) {

			for (String resourceURI : this.domainsIndex.getDomain(domain)
					.getAnnotation().getResources()) {
				AnnotatedWord<ResourceMetadata> resource = this.resourcesIndex
						.getResource(domain, resourceURI);
				for (Entry<String, Long> termCandidateEntry : resource
						.getAnnotation().getTermsOcurrences().entrySet()) {
					AnnotatedWord<TermMetadata> termCandidate = this.termsIndex
							.lookUp(domain, termCandidateEntry.getKey());
					updateDomainConsensus(
							domain,
							termCandidate,
							resource.getAnnotation().getTermsOcurrences()
									.get(termCandidateEntry.getKey()), resource
									.getAnnotation().getNumberOfTerms());
				}
			}

		}

	}

	// -----------------------------------------------------------------------------------

	private void updateDomainConsensus(String domain,
			AnnotatedWord<TermMetadata> termCandidate, long termOcurrences,
			long resourceTermOcurrences) {
		double probabiltyTermResource = ((double) termOcurrences)
				/ ((double) resourceTermOcurrences);

		double resourceConsensus = -probabiltyTermResource
				* Math.log(probabiltyTermResource);

		termCandidate.getAnnotation().setDomainConsensus(
				termCandidate.getAnnotation().getDomainConsensus()
						+ resourceConsensus);

		if (termCandidate.getAnnotation().getDomainConsensus() > this.domainsIndex
				.getDomain(domain).getAnnotation().getMaxDomainConsensus()) {
			this.domainsIndex
					.getDomain(domain)
					.getAnnotation()
					.setMaxDomainConsensus(
							termCandidate.getAnnotation().getDomainConsensus());
		}

		if (termCandidate.getAnnotation().getDomainConsensus() < this.domainsIndex
				.getDomain(domain).getAnnotation().getMinDomainConsensus()) {
			this.domainsIndex
					.getDomain(domain)
					.getAnnotation()
					.setMinDomainConsensus(
							termCandidate.getAnnotation().getDomainConsensus());
		}

	}

	// -----------------------------------------------------------------------------------

	public TermsTable retrieve() {
		TermsTable termsTable = new TermsTable();

		List<String> foundURIs = this.core
				.getAnnotationHandler()
				.getLabeledAs(
						(String) this.parameters
								.getParameterValue(OntologyLearningParameters.TARGET_DOMAIN),
						RDFHelper.TERM_CLASS);

		for (String termURI : foundURIs) {
			Term term = (Term) this.core.getInformationHandler().get(termURI,
					RDFHelper.TERM_CLASS);
			// System.out.println("retrieved term ---> " + term);
			termsTable.addTerm(term);
		}
		return termsTable;
	}

	// -----------------------------------------------------------------------------------

	public TermsTable extract() {
		this.extractTerms();
		TermsTable termsTable = new TermsTable();
		for (AnnotatedWord<TermMetadata> term : this.termsIndex
				.getTerms(this.targetDomain)) {

			// System.out.println("term(" + term.getWord() + ")> " + term);

			Term newTerm = new Term();
			// The term URI is obtained using an auxiliary function
			newTerm.setURI(Term.buildURI(term.getWord(), this.targetDomain));
			newTerm.setAnnotatedTerm(term);
			termsTable.addTerm(newTerm);
		}

		return termsTable;
	}

	// -----------------------------------------------------------------------------------

	private void removeTerms() {
		List<String> foundURIs = this.core
				.getAnnotationHandler()
				.getLabeledAs(
						(String) this.parameters
								.getParameterValue(OntologyLearningParameters.TARGET_DOMAIN),
						RDFHelper.TERM_CLASS);
		System.out.println("Found " + foundURIs.size() + " to get removed ");
		for (String termURI : foundURIs) {
			System.out.println("Removing the term " + termURI);
			this.core.getInformationHandler().remove(termURI,
					RDFHelper.TERM_CLASS);
		}
	}

	// -----------------------------------------------------------------------------------

	public static void main(String[] args) {
		TermsExtractor termExtractor = new TermsExtractor();
		/*
		 * List<String> consideredDomains = Arrays.asList("cs", "math");
		 */

		// List<String> consideredDomains = Arrays.asList("math");

		/*
		 * List<String> consideredDomains =
		 * Arrays.asList("Physics   Biological Physics");
		 */
		/*
		 * List<String> consideredDomains = Arrays.asList(
		 * "Quantitative Biology   Populations and Evolution",
		 * "Physics   Biological Physics",
		 * "Nonlinear Sciences   Exactly Solvable and Integrable Systems");
		 */
		/*
		 * 
		 * List<String> consideredDomains = Arrays
		 * .asList("Nonlinear Sciences   Exactly Solvable and Integrable Systems"
		 * );
		 */

		// List<String> consideredDomains = Arrays.asList("cs", "math");

		List<String> consideredDomains = Arrays.asList("CGTestCorpus");
		String targetDomain = "CGTestCorpus";
		Double hyperymMinimumThreshold = 0.7;
		boolean extractTerms = true;
		Integer numberInitialTerms = 10;
		String consideredResources = RDFHelper.PAPER_CLASS;

		OntologyLearningParameters ontologyLearningParameters = new OntologyLearningParameters();
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.CONSIDERED_DOMAINS,
				consideredDomains);
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.TARGET_DOMAIN, targetDomain);
		ontologyLearningParameters
				.setParameter(
						OntologyLearningParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD,
						hyperymMinimumThreshold);
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.EXTRACT_TERMS, extractTerms);
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.NUMBER_INITIAL_TERMS,
				numberInitialTerms);


		Core core = CoreUtility.getUIACore();
		DomainsGatherer domainGatherer = new DomainsGatherer();
		domainGatherer.init(core, ontologyLearningParameters);

		DomainsTable domainsTable = domainGatherer.gather();

		termExtractor.init(core, domainsTable, ontologyLearningParameters);
		// termExtractor.removeTerms();
		TermsTable termsTable = termExtractor.extract();
		termExtractor.storeTable(termsTable);
		
	}

}
