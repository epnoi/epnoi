package org.epnoi.learner;


import org.epnoi.learner.relations.RelationsHandler;
import org.epnoi.learner.relations.RelationsRetriever;
import org.epnoi.learner.relations.extractor.RelationsExtractor;
import org.epnoi.learner.terms.TermVertice;
import org.epnoi.learner.terms.TermsExtractor;
import org.epnoi.learner.terms.TermsRetriever;
import org.epnoi.learner.terms.TermsTable;
import org.epnoi.model.Domain;
import org.epnoi.model.Relation;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.Term;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.core.CoreUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class OntologyLearningWorkflow {
	private static final Logger logger = Logger
			.getLogger(OntologyLearningWorkflow.class.getName());
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

	private double hypernymRelationsThreshold;
	private boolean extractTerms;
	private boolean extractRelations;
	public static final String DOMAIN_URI = "http://www.epnoi.org/CGTestCorpusDomain";
	// ---------------------------------------------------------------------------------------------------------

	public void init(Core core,
			LearningParameters learningParameters)
			throws EpnoiInitializationException {

		logger.info("Initializing the OntologyLearningWorlow with the following parameters: ");
		logger.info(learningParameters.toString());

		this.learningParameters = learningParameters;
		this.hypernymRelationsThreshold = (double) this.learningParameters
				.getParameterValue(LearningParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD);
		this.extractTerms = (boolean) this.learningParameters
				.getParameterValue(LearningParameters.EXTRACT_TERMS);

		this.learningParameters = learningParameters;

		this.domainsTableCreator = new DomainsTableCreator();
		this.domainsTableCreator.init(core, learningParameters);
		this.domainsTable = this.domainsTableCreator.create();

		this.termExtractor = new TermsExtractor();

		
		this.termsRetriever = new TermsRetriever(core);
		
		
		
		this.relationsTableExtractor = new RelationsExtractor();
		this.relationsTableExtractor.init(core, this.domainsTable,
				learningParameters);

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

		//termsTable.show(30);

		System.out.println("Extracting relations table");

		this.relationsTable = this.relationsTableExtractor
				.extract(this.termsTable);

		System.out.println("Relations Table> " + this.relationsTable);

		System.out.println("end");
		System.exit(0);
		OntologyGraph ontologyNoisyGraph = OntologyGraphFactory.build(
				this.learningParameters, this.termsTable,
				this.relationsTable);

		Set<TermVertice> visitedTerms = new HashSet<TermVertice>();

		Set<TermVertice> termsVerticesToExpand = ontologyNoisyGraph.vertexSet();

		do {

			for (TermVertice termVerticeToExpand : termsVerticesToExpand) {
				for (Relation relation : relationsTable.getRelations(
						termVerticeToExpand.getTerm().getUri(),
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
	
	// ---------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out.println("Starting the Ontology Learning Process!");

		// Core initialization
		Core core = CoreUtility.getUIACore();



		Domain domain = null;

		if (core.getInformationHandler().contains(DOMAIN_URI,
				RDFHelper.DOMAIN_CLASS)) {
			domain = (Domain) core.getInformationHandler().get(DOMAIN_URI,
					RDFHelper.DOMAIN_CLASS);
		} else {
			System.out.println("The target domian "+DOMAIN_URI+ "couldn't be found in the UIA");
			System.exit(0);
		}

		ArrayList<Domain> consideredDomains = new ArrayList(Arrays.asList(domain));
		String targetDomain = DOMAIN_URI;

		Double hyperymExpansionMinimumThreshold = 0.7;
		Double hypernymExtractionMinimumThresohold = 0.091;
		boolean extractTerms = true;
		Integer numberInitialTerms = 10;
		String hypernymsModelPath = "/opt/epnoi/epnoideployment/firstReviewResources/lexicalModel/model.bin";

		LearningParameters learningParameters = new LearningParameters();
		learningParameters.setParameter(
				LearningParameters.CONSIDERED_DOMAINS,
				consideredDomains);

		learningParameters.setParameter(
				LearningParameters.TARGET_DOMAIN_URI, targetDomain);
		learningParameters
				.setParameter(
						LearningParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD,
						hyperymExpansionMinimumThreshold);

		learningParameters
				.setParameter(
						LearningParameters.HYPERNYM_RELATION_EXTRACTION_THRESHOLD,
						hypernymExtractionMinimumThresohold);
		learningParameters.setParameter(
				LearningParameters.EXTRACT_TERMS, extractTerms);
		learningParameters.setParameter(
				LearningParameters.NUMBER_INITIAL_TERMS,
				numberInitialTerms);

		learningParameters.setParameter(
				LearningParameters.HYPERNYM_MODEL_PATH,
				hypernymsModelPath);
		learningParameters.setParameter(LearningParameters.CONSIDER_KNOWLEDGE_BASE, false);

		OntologyLearningWorkflow ontologyLearningProcess = new OntologyLearningWorkflow();

		try {
			ontologyLearningProcess.init(core, learningParameters);
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ontologyLearningProcess.execute();
		System.out.println("Ending the Ontology Learning Process!");
	}

}
