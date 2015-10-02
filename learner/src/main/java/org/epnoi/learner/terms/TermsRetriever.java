package org.epnoi.learner.terms;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.epnoi.learner.DomainsGatherer;
import org.epnoi.learner.DomainsTable;
import org.epnoi.learner.automata.OntologyLearningWorkflowParameters;
import org.epnoi.model.Context;
import org.epnoi.model.Domain;
import org.epnoi.model.Term;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;

public class TermsRetriever {

	private static final Logger logger = Logger.getLogger(TermsRetriever.class
			.getName());

	private Core core;
	// private List<String> consideredDomains;
	private String targetDomain;

	// -----------------------------------------------------------------------------------

	public TermsRetriever(Core core) {
		this.core = core;
	}

	// -----------------------------------------------------------------------------------

	public void store(Domain domain, TermsTable termsTable) {
		logger.info("Storing the Terms Table for domain " + domain.getURI());

		for (Term term : termsTable.getTerms()) {
			System.out.println("Storing " + term);
			core.getInformationHandler().put(term, Context.getEmptyContext());

			core.getAnnotationHandler().label(term.getURI(), this.targetDomain);
		}
		System.out
				.println("=========================================================================================================================");
	}

	// -----------------------------------------------------------------------------------

	// -----------------------------------------------------------------------------------

	public TermsTable retrieve(Domain domain) {
		TermsTable termsTable = new TermsTable();

		// First we retrieve the URIs of the resources associated with the
		// considered domain
		List<String> foundURIs = this.core.getAnnotationHandler().getLabeledAs(
				domain.getLabel(), RDFHelper.TERM_CLASS);
		// The terms are then retrieved and added to the Terms Table
		for (String termURI : foundURIs) {
			Term term = (Term) this.core.getInformationHandler().get(termURI,
					RDFHelper.TERM_CLASS);
			termsTable.addTerm(term);
		}
		return termsTable;
	}

	// -----------------------------------------------------------------------------------

	private void remove(Domain domain) {
		List<String> foundURIs = this.core.getAnnotationHandler().getLabeledAs(
				domain.getLabel(), RDFHelper.TERM_CLASS);

		for (String termURI : foundURIs) {
			System.out.println("Removing the term " + termURI);
			this.core.getInformationHandler().remove(termURI,
					RDFHelper.TERM_CLASS);
		}
	}

	// -----------------------------------------------------------------------------------

	public static void main(String[] args) {
		TermsExtractor termExtractor = new TermsExtractor();

		// List<String> consideredDomains = Arrays.asList("cs", "math");

		List<String> consideredDomains = Arrays.asList("CGTestCorpus");
		String targetDomain = "CGTestCorpus";
		Double hyperymMinimumThreshold = 0.7;
		boolean extractTerms = true;
		Integer numberInitialTerms = 10;
		String consideredResources = RDFHelper.PAPER_CLASS;

		OntologyLearningWorkflowParameters ontologyLearningParameters = new OntologyLearningWorkflowParameters();
		ontologyLearningParameters.setParameter(
				OntologyLearningWorkflowParameters.CONSIDERED_DOMAINS,
				consideredDomains);
		ontologyLearningParameters.setParameter(
				OntologyLearningWorkflowParameters.TARGET_DOMAIN, targetDomain);
		ontologyLearningParameters
				.setParameter(
						OntologyLearningWorkflowParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD,
						hyperymMinimumThreshold);
		ontologyLearningParameters.setParameter(
				OntologyLearningWorkflowParameters.EXTRACT_TERMS, extractTerms);
		ontologyLearningParameters.setParameter(
				OntologyLearningWorkflowParameters.NUMBER_INITIAL_TERMS,
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
