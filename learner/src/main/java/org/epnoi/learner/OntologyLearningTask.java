package org.epnoi.learner;

import org.epnoi.learner.relations.RelationsHandler;
import org.epnoi.learner.relations.RelationsRetriever;
import org.epnoi.learner.relations.extractor.RelationsExtractor;
import org.epnoi.learner.terms.TermsExtractor;
import org.epnoi.learner.terms.TermsRetriever;
import org.epnoi.learner.terms.TermsTable;
import org.epnoi.model.Domain;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.core.CoreUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

public class OntologyLearningTask {
	private static final Logger logger = Logger
			.getLogger(OntologyLearningTask.class.getName());
	private LearningParameters learningParameters;
	private TermsExtractor termExtractor;
	private TermsRetriever termsRetriever;
	private TermsTable termsTable;
	private RelationsTable relationsTable;
	private RelationsHandler relationsHandler;
	private RelationsExtractor relationsTableExtractor;
	private RelationsRetriever relationsTableRetriever;

	private DomainsTableCreator domainsTableCreator;
	private DomainsTable domainsTable;

	Domain domain;
	private double hypernymRelationsThreshold;
	private boolean extractTerms;
	private boolean extractRelations;
	public static String DOMAIN_URI = "http://www.epnoi.org/CGTestCorpusDomain";
	// ---------------------------------------------------------------------------------------------------------

	public void init(Core core,
			LearningParameters learningParameters)
			throws EpnoiInitializationException {

		logger.info("Initializing the OntologyLearningTask with the following parameters: ");
		logger.info(learningParameters.toString());

		this.learningParameters = learningParameters;

		this.hypernymRelationsThreshold = (double) this.learningParameters
				.getParameterValue(LearningParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD);
		this.extractTerms = (boolean) this.learningParameters
				.getParameterValue(LearningParameters.EXTRACT_TERMS);

		this.learningParameters = learningParameters;

		this.domainsTableCreator = new DomainsTableCreator();
		this.domainsTableCreator.init(core, learningParameters);
		this.domainsTable = this.domainsTableCreator.create(domain);

		this.termExtractor = new TermsExtractor();
		this.termExtractor.init(core, this.domainsTable,
				learningParameters);

		this.termsRetriever = new TermsRetriever(core);

		this.relationsTableExtractor = new RelationsExtractor();
		this.relationsTableExtractor.init(core, this.domainsTable,
				learningParameters);

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
/*
		System.out.println("Extracting relations table");

		this.relationsTable = this.relationsTableExtractor
				.extract(this.termsTable);

		System.out.println("Relations Table> " + this.relationsTable);
*/
		System.out.println("end");

	}

	// ---------------------------------------------------------------------------------------------------------

	public void perform(Core core, Domain domain) {
		System.out.println("Starting the Ontology Learning Task");
		this.domain = domain;
		ArrayList<Domain> consideredDomains = new ArrayList(Arrays.asList(domain));


/*
		LearningParameters learningParameters = new LearningParameters();
		learningParameters.setParameter(
				LearningParameters.CONSIDERED_DOMAINS,
				consideredDomains);

		learningParameters.setParameter(
				LearningParameters.TARGET_DOMAIN,
				domain.getUri());
		learningParameters
				.setParameter(
						LearningParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD,
						0.7);

		learningParameters
				.setParameter(
						LearningParameters.HYPERNYM_RELATION_EXTRACTION_THRESHOLD,
						0.00195);
		learningParameters.setParameter(
				LearningParameters.EXTRACT_TERMS, true);
		learningParameters.setParameter(
				LearningParameters.NUMBER_INITIAL_TERMS, 10);

		learningParameters
				.setParameter(
						LearningParameters.HYPERNYM_MODEL_PATH,
						"/opt/epnoi/epnoideployment/firstReviewResources/lexicalModel/model.bin");
		learningParameters.setParameter(LearningParameters.CONSIDER_KNOWLEDGE_BASE, true);
*/
/*
		try {
			init(core, learningParameters);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
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
