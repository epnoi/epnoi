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

import org.epnoi.model.Content;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;

public class TermExtractor {
	private static final Logger logger = Logger.getLogger(TermExtractor.class
			.getName());
	Core core;
	List<String> consideredDomains;
	String targetDomain;
	String consideredResources;
	Map<String, List<String>> resourcePerConsideredDomain;
	TermsIndex termsIndex;
	ResourcesIndex resourcesIndex;
	DomainsIndex domainsIndex;
	double cValueWeight = 0.33;
	double domainPertinenceWeight = 0.33;
	double domainConsensusWeight = 1 - cValueWeight - domainPertinenceWeight;

	// -----------------------------------------------------------------------------------

	public void init(List<String> domains, String consideredResources) {
		logger.info("Initializing the TermExtractor for the domains ");
		this.core = CoreUtility.getUIACore();
		this.resourcePerConsideredDomain = new HashMap<>();
		this.consideredDomains = domains;
		this.consideredResources = consideredResources;
		this.termsIndex = new TermsIndex();
		this.termsIndex.init();
		this.resourcesIndex = new ResourcesIndex();
		this.resourcesIndex.init();
		this.domainsIndex = new DomainsIndex();
		this.domainsIndex.init();
	}

	// -----------------------------------------------------------------------------------

	public void gatherResourcesPerConsideredDomain() {
		for (String domain : this.consideredDomains) {
			List<String> foundURIs = core.getAnnotationHandler().getLabeledAs(
					domain, this.consideredResources);

			this.resourcePerConsideredDomain.put(domain, foundURIs);
			System.out.println("The considered domain " + domain + " has "
					+ foundURIs.size() + " elements");
		}

		// System.out.println("----> "+this.resourcePerConsideredDomain);
	}

	// -----------------------------------------------------------------------------------

	public void indexResources() {
		this.gatherResourcesPerConsideredDomain();
		for (String domain : this.consideredDomains) {
			System.out
					.println("Indexing the domain:> "
							+ domain
							+ " ----------------------------------------------------------------");
			this.indexDomainResoures(domain);
		}

	}

	// -----------------------------------------------------------------------------------

	private void indexDomainResoures(String domain) {
		List<String> resourcesURIs = this.resourcePerConsideredDomain
				.get(domain);
		for (String resourceURI : resourcesURIs) {
			System.out.println("Indexing the element " + resourceURI);
			indexResource(domain, resourceURI);
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

	private void indexResource(String domain, String URI) {
		Document annotatedDocument = retrieveAnnotatedDocument(URI);
		TermCandidateBuilder termCandidateBuilder = new TermCandidateBuilder(
				annotatedDocument);

		for (Annotation annotation : annotatedDocument.getAnnotations().get(
				"TermCandidate")) {

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
		Content<String> annotatedContent = core.getInformationAccess()
				.getAnnotatedContent(URI, this.consideredResources);
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
			}

			System.out
					.println("=========================================================================================================================");

			System.out.println("Domain (" + domain + ")> " + domain);

			System.out
					.println("=========================================================================================================================");

			for (AnnotatedWord<TermMetadata> term : this.termsIndex
					.getTerms(domain)) {

				System.out.println("term(" + term.getWord() + ")> " + term);

				/*
				 * System.out.println("term> " + term.getWord() + " cValue>" +
				 * term.getAnnotation().getCValue() + " domainConsensus> " +
				 * term.getAnnotation().getDomainConsensus() +
				 * " domainPertinence> " +
				 * term.getAnnotation().getDomainPertinence() + "  ocurrences "
				 * + term.getAnnotation().getOcurrences());
				 */
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

	public static void main(String[] args) {
		TermExtractor termExtractor = new TermExtractor();
		List<String> consideredDomains = Arrays.asList("cs","math");
		// List<String> consideredDomains = Arrays.asList("math");

		/*
		 * List<String> consideredDomains =
		 * Arrays.asList("Physics   Biological Physics");
		 */
/*
		List<String> consideredDomains = Arrays.asList(
				"Quantitative Biology   Populations and Evolution",
				"Physics   Biological Physics",
				"Nonlinear Sciences   Exactly Solvable and Integrable Systems");
	
		*/
		/*
		 * 
		 * List<String> consideredDomains = Arrays
		 * .asList("Nonlinear Sciences   Exactly Solvable and Integrable Systems"
		 * );
		 */
		String consideredResources = RDFHelper.PAPER_CLASS;
		termExtractor.init(consideredDomains, consideredResources);
		termExtractor.extractTerms();
		termExtractor.showResult();

	}

}
