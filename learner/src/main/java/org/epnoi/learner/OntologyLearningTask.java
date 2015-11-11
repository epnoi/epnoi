package org.epnoi.learner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import org.epnoi.learner.relations.extractor.RelationsExtractor;
import org.epnoi.learner.relations.RelationsHandler;
import org.epnoi.learner.relations.RelationsRetriever;
import org.epnoi.learner.terms.TermsExtractor;
import org.epnoi.learner.terms.TermsRetriever;
import org.epnoi.learner.terms.TermsTable;
import org.epnoi.model.Domain;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.core.CoreUtility;

public class OntologyLearningTask {
	private static final Logger logger = Logger
			.getLogger(OntologyLearningTask.class.getName());
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

	Domain domain;
	private double hypernymRelationsThreshold;
	private boolean extractTerms;
	private boolean extractRelations;
	public static String DOMAIN_URI = "http://www.epnoi.org/CGTestCorpusDomain";
	// ---------------------------------------------------------------------------------------------------------

	public void init(Core core,
			OntologyLearningWorkflowParameters ontologyLearningParameters)
			throws EpnoiInitializationException {

		logger.info("Initializing the OntologyLearningTask with the following parameters: ");
		logger.info(ontologyLearningParameters.toString());

		this.ontologyLearningParameters = ontologyLearningParameters;

		this.hypernymRelationsThreshold = (double) this.ontologyLearningParameters
				.getParameterValue(OntologyLearningWorkflowParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD);
		this.extractTerms = (boolean) this.ontologyLearningParameters
				.getParameterValue(OntologyLearningWorkflowParameters.EXTRACT_TERMS);

		this.ontologyLearningParameters = ontologyLearningParameters;

		this.domainsGatherer = new DomainsGatherer();
		this.domainsGatherer.init(core, ontologyLearningParameters);
		this.domainsTable = this.domainsGatherer.gather(domain);

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
		logger.info("Starting the execution of a Ontology Learning Task");

		Domain targetDomain = this.domainsTable.getTargetDomain();

		if (extractTerms) {

			this.termsTable = this.termExtractor.extract();
		} else {
			this.termsTable = this.termsRetriever.retrieve(targetDomain);
		}

		// termsTable.show(30);

		System.out.println("Extracting relations table");

		this.relationsTable = this.relationsTableExtractor
				.extract(this.termsTable);

		System.out.println("Relations Table> " + this.relationsTable);

		System.out.println("end");

	}

	// ---------------------------------------------------------------------------------------------------------

	public void perform(Core core, Domain domain) {
		System.out.println("Starting the Ontology Learning Task");
		this.domain = domain;
		ArrayList<Domain> consideredDomains = new ArrayList(Arrays.asList(domain));

		OntologyLearningWorkflowParameters ontologyLearningParameters = new OntologyLearningWorkflowParameters();
		ontologyLearningParameters.setParameter(
				OntologyLearningWorkflowParameters.CONSIDERED_DOMAINS,
				consideredDomains);

		ontologyLearningParameters.setParameter(
				OntologyLearningWorkflowParameters.TARGET_DOMAIN,
				domain.getUri());
		ontologyLearningParameters
				.setParameter(
						OntologyLearningWorkflowParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD,
						0.7);

		ontologyLearningParameters
				.setParameter(
						OntologyLearningWorkflowParameters.HYPERNYM_RELATION_EXTRACTION_THRESHOLD,
						0.00195);
		ontologyLearningParameters.setParameter(
				OntologyLearningWorkflowParameters.EXTRACT_TERMS, true);
		ontologyLearningParameters.setParameter(
				OntologyLearningWorkflowParameters.NUMBER_INITIAL_TERMS, 10);

		ontologyLearningParameters
				.setParameter(
						OntologyLearningWorkflowParameters.HYPERNYM_MODEL_PATH,
						"/opt/epnoi/epnoideployment/firstReviewResources/lexicalModel/model.bin");
		ontologyLearningParameters.setParameter(OntologyLearningWorkflowParameters.CONSIDER_KNOWLEDGE_BASE, true);
		

		try {
			init(core, ontologyLearningParameters);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		execute();
		System.out.println("Ending the Ontology Learning Process!");
	}

	// ---------------------------------------------------------------------------------------------------------

	public TermsTable getTermsTable() {
		return this.termsTable;
	}

	// ---------------------------------------------------------------------------------------------------------

	public RelationsTable getRelationsTable() {
		return this.relationsTable;
	}

	// ---------------------------------------------------------------------------------------------------------
	
	public static void main(String[] args) {

		Core core = CoreUtility.getUIACore();
		OntologyLearningTask ontologyLearningTask = new OntologyLearningTask();

		Domain domain = null;

		if (core.getInformationHandler().contains(DOMAIN_URI,
				RDFHelper.DOMAIN_CLASS)) {
			domain = (Domain) core.getInformationHandler().get(DOMAIN_URI,
					RDFHelper.DOMAIN_CLASS);
		} else {
			System.out.println("The target domain "+DOMAIN_URI+ "couldn't be found in the UIA");
			System.exit(0);
		}


		ontologyLearningTask.perform(core, domain);

	}

}
