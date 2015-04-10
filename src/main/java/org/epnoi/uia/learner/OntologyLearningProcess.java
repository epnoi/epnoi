package org.epnoi.uia.learner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.epnoi.model.Domain;
import org.epnoi.model.Term;
import org.epnoi.uia.commons.Parameters;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.domains.DomainsHandler;
import org.epnoi.uia.exceptions.EpnoiInitializationException;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.learner.relations.Relation;
import org.epnoi.uia.learner.relations.RelationsExtractor;
import org.epnoi.uia.learner.relations.RelationsTable;
import org.epnoi.uia.learner.terms.TermVertice;
import org.epnoi.uia.learner.terms.TermsExtractor;
import org.epnoi.uia.learner.terms.TermsTable;

public class OntologyLearningProcess {
	private static final Logger logger = Logger
			.getLogger(OntologyLearningProcess.class.getName());
	private OntologyLearningParameters ontologyLearningParameters;
	private TermsExtractor termExtractor;
	private TermsTable termsTable;
	private RelationsTable relationsTable;
	private RelationsExtractor relationsTableExtractor;

	private DomainsGatherer domainsGatherer;
	private DomainsTable domainsTable;

	private double hypernymRelationsThreshold;
	private boolean extractTerms;

	// ---------------------------------------------------------------------------------------------------------

	public void init(Core core,
			OntologyLearningParameters ontologyLearningParameters)
			throws EpnoiInitializationException {

		logger.info("Initializing the OntologyLearningProcess with the following parameters");
		logger.info(ontologyLearningParameters.toString());

		this.ontologyLearningParameters = ontologyLearningParameters;
		this.hypernymRelationsThreshold = (double) this.ontologyLearningParameters
				.getParameterValue(OntologyLearningParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD);
		this.extractTerms = (boolean) this.ontologyLearningParameters
				.getParameterValue(OntologyLearningParameters.EXTRACT_TERMS);

		this.ontologyLearningParameters = ontologyLearningParameters;

		this.domainsGatherer = new DomainsGatherer();
		this.domainsGatherer.init(core, ontologyLearningParameters);
		this.domainsTable = this.domainsGatherer.gather();

		this.termExtractor = new TermsExtractor();
		this.termExtractor.init(core, this.domainsTable,
				ontologyLearningParameters);

		this.relationsTableExtractor = new RelationsExtractor();
		this.relationsTableExtractor.init(core, this.domainsTable,
				ontologyLearningParameters);

	}

	// ---------------------------------------------------------------------------------------------------------

	public void execute() {
		logger.info("Starting the execution of a Ontology Learning Process");

		if (extractTerms) {
			this.termsTable = this.termExtractor.extract();
		} else {
			this.termsTable = this.termExtractor.retrieve();
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
						termVerticeToExpand, hypernymRelationsThreshold)) {
					Term destinationTerm = relation.getTarget();
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

		Domain domain = new Domain();
		domain.setURI("CGTestCorpus");
		domain.setConsideredResource(RDFHelper.PAPER_CLASS);
		
		List<Domain> consideredDomains = Arrays.asList(domain);
		String targetDomain = "CGTestCorpus";
		
		Double hyperymExpansionMinimumThreshold = 0.7;
		Double hypernymExtractionMinimumThresohold = 0.1;
		boolean extractTerms = true;
		Integer numberInitialTerms = 10;
		String hypernymsModelPath = "/epnoi/epnoideployment/firstReviewResources/lexicalModel/model.bin";

		OntologyLearningParameters ontologyLearningParameters = new OntologyLearningParameters();
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.CONSIDERED_DOMAINS,
				consideredDomains);


		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.TARGET_DOMAIN, targetDomain);
		ontologyLearningParameters
				.setParameter(
						OntologyLearningParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD,
						hyperymExpansionMinimumThreshold);

		ontologyLearningParameters
				.setParameter(
						OntologyLearningParameters.HYPERNYM_RELATION_EXTRACTION_THRESHOLD,
						hyperymExpansionMinimumThreshold);
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.EXTRACT_TERMS, extractTerms);
		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.NUMBER_INITIAL_TERMS,
				numberInitialTerms);

		ontologyLearningParameters.setParameter(
				OntologyLearningParameters.HYPERNYM_MODEL_PATH,
				hypernymsModelPath);

		Core core = CoreUtility.getUIACore();

		OntologyLearningProcess ontologyLearningProcess = new OntologyLearningProcess();

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
