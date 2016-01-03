package org.epnoi.learner;

import com.rits.cloning.Cloner;
import org.apache.spark.api.java.JavaSparkContext;
import org.epnoi.learner.relations.RelationsRetriever;
import org.epnoi.learner.relations.extractor.RelationsExtractor;
import org.epnoi.learner.relations.extractor.parallel.ParallelRelationsExtractor;
import org.epnoi.learner.terms.TermsExtractor;
import org.epnoi.learner.terms.TermsRetriever;
import org.epnoi.learner.terms.TermsTable;
import org.epnoi.model.Domain;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;

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

    private RelationsExtractor relationsTableExtractor;
    private ParallelRelationsExtractor parallelRelationsExtractor;
    private RelationsRetriever relationsTableRetriever;


    private DomainsTableCreator domainsTableCreator;
    private DomainsTable domainsTable;
    Core core;
    Domain domain;
    private double hypernymRelationsThreshold;
    private boolean obtainTerms;
    private boolean obtainRelations;
    private boolean parallelRelationsExtraction;
    private boolean extractTerms;
    private boolean extractRelations;
    private Cloner cloner = new Cloner();

    private JavaSparkContext sparkContext;

    //
    public void init(Core core, LearningParameters parameters, JavaSparkContext sparkContext) throws EpnoiInitializationException{
        this.core = core;
        this.learningParameters = cloner.deepClone(parameters);
        this.sparkContext = sparkContext;
    }
    // ---------------------------------------------------------------------------------------------------------


    private void _init(Core core,
                       LearningParameters learningParameters){


        logger.info("Initializing the OntologyLearningTask with the following parameters: ");
        logger.info(learningParameters.toString());

        learningParameters = cloner.deepClone(learningParameters);
        this.obtainTerms = (boolean) learningParameters
                .getParameterValue(LearningParameters.OBTAIN_TERMS);
        this.obtainRelations = (boolean) learningParameters
                .getParameterValue(LearningParameters.OBTAIN_RELATIONS);
        this.hypernymRelationsThreshold = (double) this.learningParameters
                .getParameterValue(LearningParameters.HYPERNYM_RELATION_EXPANSION_THRESHOLD);
        this.extractTerms = (boolean) this.learningParameters
                .getParameterValue(LearningParameters.EXTRACT_TERMS);

        this.extractRelations = (boolean) this.learningParameters
                .getParameterValue(LearningParameters.EXTRACT_RELATIONS);

        this.parallelRelationsExtraction = (boolean)this.learningParameters.getParameterValue(LearningParameters.EXTRACT_RELATIONS_PARALLEL);

        this.domainsTableCreator = new DomainsTableCreator();
        this.domainsTableCreator.init(core, learningParameters);
        this.domainsTable = this.domainsTableCreator.create(domain);
        this.domainsTableCreator = new DomainsTableCreator();
        this.domainsTableCreator.init(core, learningParameters);

        if (obtainTerms) {
            this.termExtractor = new TermsExtractor();
            this.termExtractor.init(core, this.domainsTable,
                    learningParameters);


            this.termsRetriever = new TermsRetriever(core);
        }
        if (obtainRelations) {
            if(parallelRelationsExtraction){
                this.parallelRelationsExtractor = new ParallelRelationsExtractor();
                this.parallelRelationsExtractor.init(learningParameters,domainsTable, core,sparkContext);

            }else {
                this.relationsTableExtractor = new RelationsExtractor();
                this.relationsTableExtractor.init(core, this.domainsTable,
                        learningParameters);
            }
            this.relationsTableRetriever = new RelationsRetriever(core);
        }
    }

    // ---------------------------------------------------------------------------------------------------------

    public void _execute() {
        logger.info("Starting the execution of a Ontology Learning Task");

        Domain targetDomain = this.domainsTable.getTargetDomain();
        if (obtainTerms) {
            if (extractTerms) {

                this.termsTable = this.termExtractor.extract();
            } else {
                this.termsTable = this.termsRetriever.retrieve(targetDomain);
            }
        }
        //      termsTable.show(30);


        if (obtainRelations) {
            if (extractRelations) {

                if(parallelRelationsExtraction){
                    this.relationsTable= this.parallelRelationsExtractor.extract(this.domainsTable);
                }else {
                    this.relationsTable = this.relationsTableExtractor.extract(this.termsTable);
                }
            } else {

                this.relationsTable = this.relationsTableRetriever.retrieve(targetDomain);
            }

        }
        System.out.println("Relations Table> " + this.relationsTable);

        System.out.println("end");

    }

    // ---------------------------------------------------------------------------------------------------------

    public void perform(Domain domain) {

        logger.info("Starting the Ontology Learning Task");
        ArrayList<Domain> consideredDomains = new ArrayList(Arrays.asList(domain));
        this.learningParameters.setParameter(
                LearningParameters.CONSIDERED_DOMAINS,
                consideredDomains);

        this.learningParameters.setParameter(
                LearningParameters.TARGET_DOMAIN_URI,
                domain.getUri());


        this.domain = domain;
        // this.domainsTable = this.domainsTableCreator.create(domain);


            _init(core, learningParameters);
        
        _execute();
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
/*
        Core core = CoreUtility.getUIACore();
        OntologyLearningTask ontologyLearningTask = new OntologyLearningTask();

        Domain domain = null;

        if (core.getInformationHandler().contains(DOMAIN_URI,
                RDFHelper.DOMAIN_CLASS)) {
            domain = (Domain) core.getInformationHandler().get(DOMAIN_URI,
                    RDFHelper.DOMAIN_CLASS);
        } else {
            System.out.println("The target domain " + DOMAIN_URI + "couldn't be found in the UIA");
            System.exit(0);
        }


        ontologyLearningTask.perform(domain);
*/
    }

}
