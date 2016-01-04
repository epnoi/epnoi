package org.epnoi.learner;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.learner.modules.Learner;
import org.epnoi.learner.relations.extractor.parallel.ParallelRelationsExtractor;
import org.epnoi.model.Relation;
import org.epnoi.model.Term;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by rgonzalez on 3/12/15.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LearnerConfig.class)
@ActiveProfiles("develop")
@TestPropertySource(properties = {"learner.task.terms.extract = false", "learner.task.terms.store = false", "learner.task.relations.parallel = true"})


public class LearnerTest {
    @Autowired
    Learner learner;


    @Autowired
    LearningParameters learnerProperties;

    @Autowired
    @Value("${learner.demo.harvester.uri}")
    String domainUri;



    @Test
    public void startContext() {
        System.out.println("Starting an ontology learning task for " + domainUri);
        System.out.println("Using the following parameters "+learnerProperties);


        learner.learn(domainUri);
        /*
        System.out.println("Terminology test===========================================================");
        for (Term term : learner.retrieveTerminology(domainUri).getMostProbable(5)) {
            System.out.println("term> " + term);
        }
        */

        System.out.println("Relations test===========================================================");
        System.out.println("There were "+learner.retrieveRelations(domainUri).getRelations().size()+" relations in the domain");
        for (Relation relation: learner.retrieveRelations(domainUri).getMostProbable(5)){
            System.out.println("relation> " + relation);
        }


       // parallelRelationsExtractor.extract(domainUri);

        assert (true);

    }

}
