package org.epnoi.uia.learner.terms;

import gate.Annotation;
import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.Utils;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import gate.util.InvalidOffsetException;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.epnoi.model.Content;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.nlp.TermCandidatesFinder;

public class TermExtractor {
	private static final Logger logger = Logger.getLogger(TermExtractor.class
			.getName());
	Core core;
	List<String> consideredDomains;
	String targetDomain;
	String consideredResources;
	Map<String, List<String>> resourcePerConsideredDomain;
	TermsIndex termsIndex;

	// -----------------------------------------------------------------------------------

	public void init(List<String> domains, String consideredResources) {
		logger.info("Initializing the TermExtractor for the domains ");
		this.core = CoreUtility.getUIACore();
		this.resourcePerConsideredDomain = new HashMap<>();
		this.consideredDomains = domains;
		this.consideredResources = consideredResources;
		this.termsIndex = new TermsIndex();
		this.termsIndex.init();
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
			indexResource(resourceURI);
		}
	}

	// -----------------------------------------------------------------------------------

	private void indexResource(String URI) {

		Document annotatedDocument = retrieveAnnotatedDocument(URI);
		TermCandidateBuilder termCandidateBuilder = new TermCandidateBuilder(
				annotatedDocument);

		// System.out.println(annotatedDocument.getAnnotations());

		for (Annotation annotation : annotatedDocument.getAnnotations().get(
				"TermCandidate")) {

			AnnotatedWord<TermCandidateMetadata> termCandidate = termCandidateBuilder
					.buildTermCandidate(annotation);

			// System.out.println("The Term Candidate is  :>" + termCandidate);
			this.termsIndex.updateTerm(termCandidate);
			/*
			 * System.out.println("TERM CANDIDATE "+termCandidate.getWord()+
			 * " -----------------------------------------------");
			 * for(AnnotatedWord<TermCandidateMetadata> subTermCandidate:
			 * termCandidateBuilder.splitTermCandidate(termCandidate)){
			 * System.out.println("subTermCandidate > "+subTermCandidate); }
			 */
			/*
			 * try { System.out.println(document.getContent().getContent(
			 * annotation.getStartNode().getOffset(),
			 * annotation.getEndNode().getOffset())); } catch
			 * (InvalidOffsetException e) {
			 * 
			 * e.printStackTrace(); }
			 */

		}

	}

	// -----------------------------------------------------------------------------------

	private Document retrieveAnnotatedDocument(String URI) {
		Content<String> annotatedContent = core.getInformationAccess()
				.getAnnotatedContent(URI, this.consideredResources);
		// System.out.println(">>>>>>>>"
		// +annotatedContent.getContent());
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
					.println("Couldn't retrieve the GATE document that represents the annotated content");
			e.printStackTrace();
		}
		return document;
	}

	// -----------------------------------------------------------------------------------

	// -----------------------------------------------------------------------------------

	public static void main(String[] args) {
		TermExtractor termExtractor = new TermExtractor();
		List<String> consideredDomains = Arrays.asList("cs");
		// List<String> consideredDomains = Arrays.asList("math");
		// List<String> consideredDomains =
		// Arrays.asList("Physics   Biological Physics");
		/*
		 * List<String> consideredDomains = Arrays
		 * .asList("Quantitative Biology   Populations and Evolution");
		 */

		// List<String> consideredDomains =
		// Arrays.asList("Nonlinear Sciences   Exactly Solvable and Integrable Systems");

		String consideredResources = RDFHelper.PAPER_CLASS;
		termExtractor.init(consideredDomains, consideredResources);
		termExtractor.extractTerms();
		termExtractor.showResult();

	}

	private void filterResources() {
		// TOBE DONE
		// Filter things that doesn't look like terms (example formulas)
		// Filter stopwords
	}

	public void extractTerms() {
		this.indexResources();
		this.filterResources();
		this.calculateCValues();
	}

	// -----------------------------------------------------------------------------------

	private void showResult() {
		for (AnnotatedWord<TermCandidateMetadata> term : this.termsIndex
				.getTerms()) {
			System.out.println("term> " + term.getWord() + " cValue>"
					+ term.getAnnotation().getCValue());
		}

	}

	// -----------------------------------------------------------------------------------

	private void calculateCValues() {
		TermCandidateBuilder termCandidateBuilder = new TermCandidateBuilder(
				null);
		for (AnnotatedWord<TermCandidateMetadata> termCandidate : this.termsIndex
				.getTermCandidates()) {
			// System.out.println(" > " + termCandidate.getWord() + " | "
			// + termCandidate.getAnnotation().getLength());
			for (AnnotatedWord<TermCandidateMetadata> subTerm : termCandidateBuilder
					.splitTermCandidate(termCandidate)) {
				// System.out.println("--> " + subTerm.getWord());
				this.termsIndex.updateSubTerm(termCandidate, subTerm);
			}
		}

		for (AnnotatedWord<TermCandidateMetadata> termCandidate : this.termsIndex
				.getTermCandidates()) {

			double cValue = CValueCalculator.calculateCValue(termCandidate);
			termCandidate.getAnnotation().setCValue(cValue);
			/*
			 * System.out.println("cvalue(" + termCandidate.getWord() + ")> " +
			 * cValue + " #ocurrences " +
			 * termCandidate.getAnnotation().getOcurrences() + " #assubterm " +
			 * termCandidate.getAnnotation().getOcurrencesAsSubterm() +
			 * " #superterms " +
			 * termCandidate.getAnnotation().getNumberOfSuperterns());
			 */
		}
	}

	// -----------------------------------------------------------------------------------

}
