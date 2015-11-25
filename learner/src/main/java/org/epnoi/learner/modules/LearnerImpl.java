package org.epnoi.learner.modules;

import org.epnoi.learner.LearnerConfig;
import org.epnoi.learner.LearningParameters;
import org.epnoi.learner.OntologyLearningTask;
import org.epnoi.learner.relations.RelationsRetriever;
import org.epnoi.learner.terms.TermsRetriever;
import org.epnoi.learner.terms.TermsTable;
import org.epnoi.model.Domain;
import org.epnoi.model.Relation;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
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

    @PostConstruct
    public void init() throws EpnoiInitializationException {
        logger.info("Initializing the Learner");
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
                ontologyLearningTask.perform(core, domain);


            }
        } catch (Exception e) {
            logger.info("Something went wrong when learning about the domain " + domainUri);
            e.printStackTrace();
        }

    }

    @Override
    public RelationsTable retrieveRelations(String domainUri) {
        RelationsRetriever relationsRetriever = new RelationsRetriever(core);
        return relationsRetriever.retrieve(domainUri);
    }

    @Override
    public TermsTable retrieveTerminology(String domainUri) {

        TermsRetriever termsRetriever = new TermsRetriever(core);
        return termsRetriever.retrieve(domainUri);
    }
}