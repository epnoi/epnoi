package org.epnoi;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.Context;
import org.epnoi.model.Relation;
import org.epnoi.model.RelationsTable;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by rgonzalez on 3/12/15.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = org.epnoi.EpnoiConfig.class)
@ActiveProfiles("develop")



public class CoreTest {
    @Autowired
    Core core;

    @Test
    public void startContext() {
        System.out.println(core);
        RelationsTable relationsTable = new RelationsTable();
        relationsTable.setUri("http://test/relations");
        Relation relation = new Relation();
        relation.setUri("http://lauri");
        relation.setSource("source");
        relation.setTarget("target");
        relation.setType("hypernymy");
        relation.addProvenanceSentence("rieoirwoeirew",0.2);
        relationsTable.addRelation(relation);


        core.getInformationHandler().put(relationsTable, Context.getEmptyContext());
        RelationsTable retrievedRelationsTable = (RelationsTable) core.getInformationHandler().get("http://test/relations", RDFHelper.RELATIONS_TABLE_CLASS);
        System.out.println(retrievedRelationsTable);
        assert (retrievedRelationsTable!=null);

    }

}
