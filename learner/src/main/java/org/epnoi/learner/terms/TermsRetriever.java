package org.epnoi.learner.terms;

import org.epnoi.learner.DomainsTableCreator;
import org.epnoi.learner.DomainsTable;
import org.epnoi.learner.LearningParameters;
import org.epnoi.model.Context;
import org.epnoi.model.Domain;
import org.epnoi.model.Term;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.core.CoreUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class TermsRetriever {

    private static final Logger logger = Logger.getLogger(TermsRetriever.class
            .getName());

    private Core core;



    // -----------------------------------------------------------------------------------

    public TermsRetriever(Core core) {
        this.core = core;
    }

    // -----------------------------------------------------------------------------------

    public void store(Domain domain, TermsTable termsTable) {
        logger.info("Storing the Terms Table for domain " + domain);

        for (Term term : termsTable.getTerms()) {
            core.getInformationHandler().put(term, Context.getEmptyContext());
            core.getAnnotationHandler().label(term.getUri(), domain.getLabel());
        }
        System.out
                .println("=========================================================================================================================");
    }

    // -----------------------------------------------------------------------------------

    // -----------------------------------------------------------------------------------

    public TermsTable retrieve(Domain domain) {
        String domainLabel = domain.getLabel();

        TermsTable termsTable = getTermsTable(domainLabel);
        return termsTable;
    }

    // -----------------------------------------------------------------------------------

    public TermsTable retrieve(String domainUri) {
        Domain domain = (Domain) core.getInformationHandler().get(domainUri,
                RDFHelper.DOMAIN_CLASS);

        if (domain != null) {

            TermsTable termsTable = getTermsTable(domain.getLabel());
            return termsTable;
        }
        return new TermsTable();
    }

    // -----------------------------------------------------------------------------------

    private TermsTable getTermsTable(String domainLabel) {
        TermsTable termsTable = new TermsTable();

        // First we retrieve the URIs of the resources associated with the
        // considered domain
        List<String> foundURIs = this.core.getAnnotationHandler().getLabeledAs(
                domainLabel, RDFHelper.TERM_CLASS);
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
        /*
		TermsExtractor termExtractor = new TermsExtractor();

		// List<String> consideredDomains = Arrays.asList("cs", "math");

		ArrayList<String> consideredDomains = new ArrayList(Arrays.asList("CGTestCorpus"));
		String targetDomain = "CGTestCorpus";
		Double hyperymMinimumThreshold = 0.7;
		boolean extractTerms = true;
		Integer numberInitialTerms = 10;
		String consideredResources = RDFHelper.PAPER_CLASS;

		LearningParameters learningParameters = new LearningParameters();
		learningParameters.setParameter(
				LearningParameters.CONSIDERED_DOMAINS,
				consideredDomains);
		learningParameters.setParameter(
				LearningParameters.TARGET_DOMAIN_URI, targetDomain);
		learningParameters
				.setParameter(
						LearningParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD,
						hyperymMinimumThreshold);
		learningParameters.setParameter(
				LearningParameters.EXTRACT_TERMS, extractTerms);
		learningParameters.setParameter(
				LearningParameters.NUMBER_INITIAL_TERMS,
				numberInitialTerms);

		Core core = CoreUtility.getUIACore();
		DomainsTableCreator domainGatherer = new DomainsTableCreator();
		domainGatherer.init(core, learningParameters);

		DomainsTable domainsTable = domainGatherer.create();

		termExtractor.init(core, domainsTable, learningParameters);
		// termExtractor.removeTerms();
		TermsTable termsTable = termExtractor.extract();
		termExtractor.storeTable(termsTable);
*/
    }

}
