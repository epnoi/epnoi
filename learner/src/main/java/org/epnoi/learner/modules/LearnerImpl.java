package org.epnoi.learner.modules;

import org.apache.spark.api.java.JavaSparkContext;
import org.epnoi.learner.LearnerConfig;
import org.epnoi.learner.LearningParameters;
import org.epnoi.learner.OntologyLearningTask;
import org.epnoi.learner.relations.RelationsRetriever;
import org.epnoi.learner.terms.TermsRetriever;
import org.epnoi.learner.terms.TermsTable;
import org.epnoi.model.Domain;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

/**
 * Created by rgonza on 13/11/15.
 */
@Component
public class LearnerImpl implements Learner {

    private static final Logger logger = Logger.getLogger(LearnerConfig.class
            .getName());
    @Autowired
    private Core core;

    @Autowired
    Trainer trainer;

    @Autowired
    LearningParameters learningParameters;

    @Autowired
    JavaSparkContext sparkContext;

    private TermsRetriever termsRetriever;

    RelationsRetriever relationsRetriever = new RelationsRetriever(core);

    @PostConstruct
    public void init() throws EpnoiInitializationException {
        logger.info("Initializing the Learner");
        this.termsRetriever = new TermsRetriever(core);
        this.relationsRetriever = new RelationsRetriever(core);
    }

    @Override
    public Trainer getTrainer() {
        return this.trainer;
    }

    @Override
    public LearningParameters getParameters() {
        return this.learningParameters;
    }

    @Override
    public void learn(String domainUri) {

        try {
            Domain domain = (Domain) core.getInformationHandler().get(domainUri,
                    RDFHelper.DOMAIN_CLASS);

            if (domain != null) {
                OntologyLearningTask ontologyLearningTask = new OntologyLearningTask();
                ontologyLearningTask.init(this.core, this.learningParameters, this.sparkContext);
                try {
                    ontologyLearningTask.perform(domain);
                    _storeLearningResults(ontologyLearningTask, domain);
                } catch (Exception e) {
                    logger.severe("There was a problem while learning the domain " + domain.getUri());
                    e.printStackTrace();
                }

            } else {
                logger.severe("The retrieved domain was null!!!!");
            }
        } catch (Exception e) {
            logger.info("Something went wrong when learning about the domain " + domainUri);
            e.printStackTrace();
        }

    }

    private void _storeLearningResults(OntologyLearningTask ontologyLearningTask, Domain domain) {
        if (((boolean) learningParameters.getParameterValue(LearningParameters.OBTAIN_TERMS))
                && ((boolean) learningParameters.getParameterValue(LearningParameters.STORE_TERMS))) {
            this.termsRetriever.store(domain, ontologyLearningTask.getTermsTable());
        }

        if (((boolean) learningParameters.getParameterValue(LearningParameters.OBTAIN_RELATIONS)
                && ((boolean) learningParameters.getParameterValue(LearningParameters.STORE_RELATIONS)))) {
            this.relationsRetriever.store(ontologyLearningTask.getRelationsTable());
        }
    }

    @Override
    public RelationsTable retrieveRelations(String domainUri) {

        return relationsRetriever.retrieve(domainUri);
    }

    @Override
    public TermsTable retrieveTerminology(String domainUri) {


        return termsRetriever.retrieve(domainUri);
    }
}