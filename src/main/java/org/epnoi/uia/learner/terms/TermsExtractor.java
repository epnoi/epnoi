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
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.OntologyLearningParameters;
import org.epnoi.uia.learner.nlp.gate.NLPAnnotationsHelper;

public class TermsExtractor {
	private static final Logger logger = Logger.getLogger(TermsExtractor.class
			.getName());
	private Core core;
	private List<String> consideredDomains;
	private String targetDomain;
	String consideredResources;
	Map<String, List<String>> resourcePerConsideredDomain;
	TermsIndex termsIndex;
	ResourcesIndex resourcesIndex;
	DomainsIndex domainsIndex;
	double cValueWeight = 0.5;
	double domainPertinenceWeight = 0.3;
	double domainConsensusWeight = 1 - cValueWeight - domainPertinenceWeight;
	Parameters parameters;

	// -----------------------------------------------------------------------------------

	public void init(Core core, Parameters parameters) {
		logger.info("Initializing the TermExtractor for the domains ");
		this.core = core;
		this.parameters = parameters;

		this.resourcePerConsideredDomain = new HashMap<>();
		this.consideredDomains = (List<String>) parameters
				.getParameterValue(OntologyLearningParameters.CONSIDERED_DOMAINS);
		this.consideredResources = (String) parameters
				.getParameterValue(OntologyLearningParameters.CONSIDERED_RESOURCES);
		this.termsIndex = new TermsIndex();
		this.termsIndex.init();
		this.resourcesIndex = new ResourcesIndex();
		this.resourcesIndex.init();
		this.domainsIndex = new DomainsIndex();
		this.domainsIndex.init();
	}

	// -----------------------------------------------------------------------------------

	public void gatherResourcesPerConsideredDomain() {

		System.out.print("Considered---> " + this.core);
		for (String domain : this.consideredDomains) {
			List<String> foundURIs = core.getAnnotationHandler().getLabeledAs(
					domain, this.consideredResources);

			this.resourcePerConsideredDomain.put(domain,
					_cleanResources(foundURIs));
			System.out.println("The considered domain " + domain + " has "
					+ foundURIs.size() + " elements");
		}

		// System.out.println("----> "+this.resourcePerConsideredDomain);
	}

	// -----------------------------------------------------------------------------------

	private List<String> _cleanResources(List<String> foundURIs) {
		List<String> cleanedURIs = new ArrayList<String>();
		for (String uri : foundURIs) {
			if (core.getInformationHandler().contains(uri,
					this.consideredResources)) {
				cleanedURIs.add(uri);
			}
		}
		return cleanedURIs;
	}

	// -----------------------------------------------------------------------------------

	public void indexResources() {
		this.gatherResourcesPerConsideredDomain();
		for (String domain : this.consideredDomains) {
			System.out
					.println("Indexing the domain:> "
							+ domain
							+ " ----------------------------------------------------------------");
			this._indexDomainResoures(domain);
		}

	}

	// -----------------------------------------------------------------------------------

	private void _indexDomainResoures(String domain) {
		List<String> resourcesURIs = this.resourcePerConsideredDomain
				.get(domain);
		for (String resourceURI : resourcesURIs) {
			System.out.println("Indexing the element " + resourceURI);
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
		Document annotatedDocument = retrieveAnnotatedDocument(URI);
		TermCandidateBuilder termCandidateBuilder = new TermCandidateBuilder(
				annotatedDocument);

		for (Annotation annotation : annotatedDocument.getAnnotations().get(
				NLPAnnotationsHelper.TERM_CANDIDATE)) {

			AnnotatedWord<TermMetadata> termCandidate = termCandidateBuilder
					.buildTermCandidate(annotation);

			this.termsIndex.updateTerm(domain, termCandidate);
			this.resourcesIndex.updateTerm(domain, URI, termCandidate);

			for (AnnotatedWord<TermMetadata> subTerm : termCandidateBuilder
					.splitTermCandidate(termCandidate)) {
				this.resourcesIndex.updateTerm(domain, URI, subTerm);
			}

			this.domainsIndex.updateTerm(domain, URI);
		}
	}

	// -----------------------------------------------------------------------------------

	private Document retrieveAnnotatedDocument(String URI) {

		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.URI, URI);
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.PAPER_CLASS);
		selector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI, URI + "/"
				+ AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE);

		Content<String> annotatedContent = core.getInformationHandler()
				.getAnnotatedContent(selector);
		Document document = null;
		try {
			document = (Document) Factory
					.createResource(
							"gate.corpora.DocumentImpl",
							Utils.featureMap(
									gate.Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME,
									annotatedContent.getContent(),
									gate.Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME,
									"text/xml"));

		} catch (ResourceInstantiationException e) { // TODO Auto-generated
			System.out
					.println("Couldn't retrieve the GATE document that represents the annotated content of "
							+ URI);
			e.printStackTrace();
		}
		return document;
	}

	// -----------------------------------------------------------------------------------

	private void filterResources() {
		// TOBE DONE
		// Filter things that doesn't look like terms (example formulas)
		// Filter stopwords
	}

	// -----------------------------------------------------------------------------------

	public void extractTerms() {
		this.indexResources();
		this.filterResources();
		this.calculateCValues();
		this.calculateDomainConsensus();
		this.calculateDomainPertinence();
		this.normalizeAnDeriveMeasures();
	}

	// -----------------------------------------------------------------------------------

	private void normalizeAnDeriveMeasures() {

		logger.info("Starting the normalization of cValue and Domain Consensus values");
		for (String domain : this.consideredDomains) {
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
		for (String domain : this.consideredDomains) {

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

			/*
			 * for (AnnotatedWord<TermMetadata> term : this.termsIndex
			 * .getTerms(domain)) {
			 * 
			 * System.out.println("term(" + term.getWord() + ")> " + term);
			 * 
			 * 
			 * } System.out .println(
			 * "========================================================================================================================="
			 * );
			 */
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
				newTerm.setURI(aDomain.getWord()
						+ "/"
						+ StringUtils.replace(term.getWord(), "[^a-zA-Z0-9]",
								"_"));
				newTerm.setAnnotatedTerm(term);
				/*
				 * System.out.println("Introducing--------> " +
				 * newTerm.getURI());
				 */
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

	private void calculateCValues() {

		logger.info("Starting the calculation of the cValues");
		for (String domain : this.consideredDomains) {
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
		for (String domain : this.consideredDomains) {
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
				for (String otherDomain : this.consideredDomains) {
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
		for (String domain : this.consideredDomains) {

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
		TermsTable termsTable = new TermsTable();
		this.extractTerms();
		this.storeResult();

		termsTable = this.retrieve();
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

		List<String> consideredDomains = Arrays.asList("cs", "math");
		String targetDomain = "cs";
		Double hyperymMinimumThreshold = 0.7;
		boolean extractTerms = false;
		Integer numberInitialTerms = 10;
		String consideredResources = RDFHelper.PAPER_CLASS;

		Parameters ontologyLearningParameters = new OntologyLearningParameters();
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.CONSIDERED_DOMAINS,
				consideredDomains);
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.TARGET_DOMAIN, targetDomain);
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.HYPERNYM_RELATION_THRESHOLD,
				hyperymMinimumThreshold);
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.EXTRACT_TERMS, extractTerms);
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.NUMBER_INITIAL_TERMS,
				numberInitialTerms);

		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.CONSIDERED_RESOURCES,
				consideredResources);

		Core core = CoreUtility.getUIACore();

		termExtractor.init(core, ontologyLearningParameters);
		// termExtractor.removeTerms();
		termExtractor.extractTerms();
		termExtractor.storeResult();

		TermsTable termsTable = termExtractor.retrieve();
		System.out
				.println("The retrieved terms table>--------------------------------------- ");

		System.out
				.println("----------------------------------------------------------------- ");
		// System.out.println(termsTable);

		System.out.println("# terms---> " + termsTable.size());
		int i = 1;
		for (Term term : termsTable.getMostProbable(30)) {
			System.out.println("Term (" + i++ + ") "
					+ term.getAnnotatedTerm().getAnnotation().getTermhood()
					+ " " + term);
		}
	}

}
