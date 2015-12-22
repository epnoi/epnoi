package org.epnoi.storage.graph;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.storage.graph.domain.DocumentNode;
import org.epnoi.storage.graph.domain.ItemNode;
import org.epnoi.storage.graph.domain.relationships.BundleDocumentItem;
import org.epnoi.storage.graph.repository.DocumentGraphRepository;
import org.epnoi.storage.graph.repository.ItemGraphRepository;
import org.epnoi.storage.graph.repository.TopicGraphRepository;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 22/12/15.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GraphConfig.class)
@TestPropertySource(properties = { "epnoi.neo4j.contactpoints = zavijava.dia.fi.upm.es", "epnoi.neo4j.port = 5030" })
public class RelationTest {

    @Autowired
    DocumentGraphRepository dRepository;

    @Autowired
    ItemGraphRepository iRepository;

    @Autowired
    TopicGraphRepository tRepository;

    @Autowired
    Session session;

    @Test
    public void insertBundle(){

        // Document
        String dUri = "documents/1212-1212-1212-1212";
        DocumentNode dNode = dRepository.findOneByUri(dUri);
        if (dNode == null){
            dNode = new DocumentNode();
            dNode.setUri(dUri);
        }

        // Item
        String iUri = "items/1212-1212-1212-1212";
        ItemNode iNode = iRepository.findOneByUri(iUri);
        if (iNode == null){
            iNode = new ItemNode();
            iNode.setUri(iUri);
        }

        // Relation
        BundleDocumentItem bundle = new BundleDocumentItem();
        bundle.setDocument(dNode);
        bundle.setItem(iNode);
        dNode.addBundleRelation(bundle);

        // save
        dRepository.save(dNode);

    }

    @Test
    public void readItemFromDocument(){

        DocumentNode result = dRepository.findOneByUri("documents/1212-1212-1212-1212");
        System.out.println(result);

        System.out.println(result.getBundles());
    }


    @Test
    public void clean(){
        iRepository.deleteAll();
        dRepository.deleteAll();
        tRepository.deleteAll();
    }

}
