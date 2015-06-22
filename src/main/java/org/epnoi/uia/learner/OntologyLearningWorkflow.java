package org.epnoi.uia.learner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.epnoi.model.Domain;
import org.epnoi.model.Relation;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.Term;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.relations.RelationsExtractor;
import org.epnoi.uia.learner.relations.RelationsHandler;
import org.epnoi.uia.learner.relations.RelationsRetriever;
import org.epnoi.uia.learner.terms.TermVertice;
import org.epnoi.uia.learner.terms.TermsExtractor;
import org.epnoi.uia.learner.terms.TermsRetriever;
import org.epnoi.uia.learner.terms.TermsTable;

public class OntologyLearningWorkflow {
	private static final Logger logger = Logger
			.getLogger(OntologyLearningWorkflow.class.getName());
	private OntologyLearningWorkflowParameters ontologyLearningParameters;
	private TermsExtractor termExtractor;
	private TermsRetriever termsRetriever;
	private TermsTable termsTable;
	private RelationsTable relationsTable;
	private RelationsHandler relationsHandler;
	private RelationsExtractor relationsTableExtractor;
	private RelationsRetriever relationsTableRetriever;

	private DomainsGatherer domainsGatherer;
	private DomainsTable domainsTable;

	private double hypernymRelationsThreshold;
	private boolean extractTerms;
	private boolean extractRelations;
	// ---------------------------------------------------------------------------------------------------------

	public void init(Core core,
			OntologyLearningWorkflowParameters ontologyLearningParameters)
			throws EpnoiInitializationException {

		logger.info("Initializing the OntologyLearningWorlow with the following parameters: ");
		logger.info(ontologyLearningParameters.toString());

		this.ontologyLearningParameters = ontologyLearningParameters;
		this.hypernymRelationsThreshold = (double) this.ontologyLearningParameters
				.getParameterValue(OntologyLearningWorkflowParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD);
		this.extractTerms = (boolean) this.ontologyLearningParameters
				.getParameterValue(OntologyLearningWorkflowParameters.EXTRACT_TERMS);

		this.ontologyLearningParameters = ontologyLearningParameters;

		this.domainsGatherer = new DomainsGatherer();
		this.domainsGatherer.init(core, ontologyLearningParameters);
		this.domainsTable = this.domainsGatherer.gather();

		this.termExtractor = new TermsExtractor();
		this.termExtractor.init(core, this.domainsTable,
				ontologyLearningParameters);

		
		this.termsRetriever = new TermsRetriever(core);
		
		
		
		this.relationsTableExtractor = new RelationsExtractor();
		this.relationsTableExtractor.init(core, this.domainsTable,
				ontologyLearningParameters);

		this.relationsTableRetriever = new RelationsRetriever(core);
		
	}

	// ---------------------------------------------------------------------------------------------------------

	public void execute() {
		logger.info("Starting the execution of a Ontology Learning Process");

		Domain targetDomain = this.domainsTable.getTargetDomain();
		
		if (extractTerms) {

			this.termsTable = this.termExtractor.extract();
		} else {
			this.termsTable = this.termsRetriever.retrieve(targetDomain);
		}

		termsTable.show(30);

		System.out.println("Extracting relations table");

		this.relationsTable = this.relationsTableExtractor
				.extract(this.termsTable);

		System.out.println("Relations Table> " + this.relationsTable);

		System.out.println("end");
		System.exit(0);
		OntologyGraph ontologyNoisyGraph = OntologyGraphFactory.build(
				this.ontologyLearningParameters, this.termsTable,
				this.relationsTable);

		Set<TermVertice> visitedTerms = new HashSet<TermVertice>();

		Set<TermVertice> termsVerticesToExpand = ontologyNoisyGraph.vertexSet();

		do {

			for (TermVertice termVerticeToExpand : termsVerticesToExpand) {
				for (Relation relation : relationsTable.getRelations(
						termVerticeToExpand.getTerm().getURI(),
						hypernymRelationsThreshold)) {
					Term destinationTerm = this.termsTable.getTerm(relation
							.getTarget());
					TermVertice destinationTermVertice = new TermVertice(
							destinationTerm);
					ontologyNoisyGraph.addEdge(termVerticeToExpand,
							destinationTermVertice);

					// If the destination term vertice has not been visited, we
					// must add it to the vertices to expnad so that it is
					// considered in the next iteration.
					if (!visitedTerms.contains(destinationTerm)) {
						termsVerticesToExpand.add(destinationTermVertice);
					}
				}
				visitedTerms.add(termVerticeToExpand);
			}
			// We stop the expansion when we have no further term vertices to
			// expand
		} while (termsVerticesToExpand.size() > 0);

		// In future versions the ontology graph should be cleaned here.

	}

	public static void main(String[] args) {
		System.out.println("Starting the Ontology Learning Process!");

		// Core initialization
		Core core = CoreUtility.getUIACore();

		String corpusURI = "http://CGTestCorpus";

		Domain domain = null;

		if (core.getInformationHandler().contains(corpusURI,
				RDFHelper.DOMAIN_CLASS)) {
			domain = (Domain) core.getInformationHandler().get(corpusURI,
					RDFHelper.DOMAIN_CLASS);
		} else {
			domain = new Domain();
			domain.setLabel("CGTestCorpus");
			domain.setURI(corpusURI);
			domain.setConsideredResource(RDFHelper.PAPER_CLASS);
		}

		List<Domain> consideredDomains = Arrays.asList(domain);
		String targetDomain = corpusURI;

		Double hyperymExpansionMinimumThreshold = 0.7;
		Double hypernymExtractionMinimumThresohold = 0.1;
		boolean extractTerms = true;
		Integer numberInitialTerms = 10;
		String hypernymsModelPath = "/epnoi/epnoideployment/firstReviewResources/lexicalModel/model.bin";

		OntologyLearningWorkflowParameters ontologyLearningParameters = new OntologyLearningWorkflowParameters();
		ontologyLearningParameters.setParameter(
				OntologyLearningWorkflowParameters.CONSIDERED_DOMAINS,
				consideredDomains);

		ontologyLearningParameters.setParameter(
				OntologyLearningWorkflowParameters.TARGET_DOMAIN, targetDomain);
		ontologyLearningParameters
				.setParameter(
						OntologyLearningWorkflowParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD,
						hyperymExpansionMinimumThreshold);

		ontologyLearningParameters
				.setParameter(
						OntologyLearningWorkflowParameters.HYPERNYM_RELATION_EXTRACTION_THRESHOLD,
						hyperymExpansionMinimumThreshold);
		ontologyLearningParameters.setParameter(
				OntologyLearningWorkflowParameters.EXTRACT_TERMS, extractTerms);
		ontologyLearningParameters.setParameter(
				OntologyLearningWorkflowParameters.NUMBER_INITIAL_TERMS,
				numberInitialTerms);

		ontologyLearningParameters.setParameter(
				OntologyLearningWorkflowParameters.HYPERNYM_MODEL_PATH,
				hypernymsModelPath);

		OntologyLearningWorkflow ontologyLearningProcess = new OntologyLearningWorkflow();

		try {
			ontologyLearningProcess.init(core, ontologyLearningParameters);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ontologyLearningProcess.execute();
		System.out.println("Ending the Ontology Learning Process!");
	}

}
