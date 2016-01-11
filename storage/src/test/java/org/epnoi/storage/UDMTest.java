package org.epnoi.storage;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.Event;
import org.epnoi.model.Resource;
import org.epnoi.model.modules.BindingKey;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.EventBusSubscriber;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.storage.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cbadenes on 01/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {
        "epnoi.cassandra.contactpoints = zavijava.dia.fi.upm.es",
        "epnoi.cassandra.port = 5011",
        "epnoi.cassandra.keyspace = research",
        "epnoi.elasticsearch.contactpoints = zavijava.dia.fi.upm.es",
        "epnoi.elasticsearch.port = 5021",
        "epnoi.neo4j.contactpoints = zavijava.dia.fi.upm.es",
        "epnoi.neo4j.port = 5030",
        "epnoi.eventbus.uri = amqp://epnoi:drinventor@zavijava.dia.fi.upm.es:5041/drinventor"})

public class UDMTest {

    private static final Logger LOG = LoggerFactory.getLogger(UDMTest.class);

    @Autowired
    UDM udm;

    @Autowired
    EventBus eventBus;

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    TimeGenerator timeGenerator;

    @Test
    public void saveSource(){
        AtomicInteger counter = new AtomicInteger(0);

        eventBus.subscribe(new EventBusSubscriber() {
            @Override
            public void handle(Event event) {
                LOG.info("Handle Event: " + event);
                counter.incrementAndGet();
            }
        }, BindingKey.of(RoutingKey.of(Resource.Type.SOURCE, Resource.State.CREATED),"test"));


        Source source = new Source();
        source.setUri("http://epnoi.org/sources/0b3e80ae-d598-4dd4-8c54-38e2229f0bf8");
        source.setUrl("file://opt/epnoi/inbox/upm");
        source.setName("test-source");
        source.setProtocol("file");
        source.setCreationTime("20160101T22:02");
        source.setDescription("testing purposes");

        LOG.info("Saving source: " + source);
        udm.saveSource(source);
        LOG.info("source saved!");

        Source source2 = udm.readSource(source.getUri());
        Assert.assertEquals(source.getUri(),source2.getUri());
        Assert.assertEquals(source.getName(),source2.getName());

        LOG.info("Deleting source: " + source);
        udm.deleteSource(source.getUri());
        LOG.info("source deleted!");

        Source source3 = udm.readSource(source.getUri());
        Assert.assertNotEquals(source,source2);

        Assert.assertEquals(1, counter.get());

    }

    @Test
    public void getDocumentsByDomain(){
        // Source
        Source source = new Source();
        source.setUri(uriGenerator.newSource());
        udm.saveSource(source);

        // Domain
        Domain domain = new Domain();
        domain.setUri(uriGenerator.newDomain());
        udm.saveDomain(domain);

        // Document 1
        Document doc1 = new Document();
        doc1.setUri(uriGenerator.newDocument());
        udm.saveDocument(doc1,source.getUri());
        // -> in domain
        udm.relateDocumentToDomain(doc1.getUri(),domain.getUri(),timeGenerator.getNowAsISO());

        // Document 2
        Document doc2 = new Document();
        doc2.setUri(uriGenerator.newDocument());
        udm.saveDocument(doc2,source.getUri());
        // -> in domain
        udm.relateDocumentToDomain(doc2.getUri(),domain.getUri(),timeGenerator.getNowAsISO());

        // Getting Documents
        List<String> documents = udm.getDocumentsByDomain(domain.getUri());

        // Delete
        udm.deleteSource(source.getUri());
        udm.deleteDomain(domain.getUri());
        udm.deleteDocument(doc1.getUri());
        udm.deleteDocument(doc2.getUri());

        Assert.assertTrue(documents != null);
        Assert.assertEquals(2,documents.size());
    }

    @Test
    public void getItemsByDomain(){
        // Source
        Source source = new Source();
        source.setUri(uriGenerator.newSource());
        udm.saveSource(source);

        // Domain
        Domain domain = new Domain();
        domain.setUri(uriGenerator.newDomain());
        udm.saveDomain(domain);

        // Document 1
        Document doc1 = new Document();
        doc1.setUri(uriGenerator.newDocument());
        udm.saveDocument(doc1,source.getUri());
        // -> in domain
        udm.relateDocumentToDomain(doc1.getUri(),domain.getUri(),timeGenerator.getNowAsISO());
        // -> Item 1
        Item item11 = new Item();
        item11.setUri(uriGenerator.newItem());
        udm.saveItem(item11,doc1.getUri());
        // -> Item 2
        Item item12 = new Item();
        item12.setUri(uriGenerator.newItem());
        udm.saveItem(item12,doc1.getUri());

        // Document 2
        Document doc2 = new Document();
        doc2.setUri(uriGenerator.newDocument());
        udm.saveDocument(doc2,source.getUri());
        // -> in domain
        udm.relateDocumentToDomain(doc2.getUri(),domain.getUri(),timeGenerator.getNowAsISO());
        // -> Item 1
        Item item21 = new Item();
        item21.setUri(uriGenerator.newItem());
        udm.saveItem(item21,doc2.getUri());
        // -> Item 2
        Item item22 = new Item();
        item22.setUri(uriGenerator.newItem());
        udm.saveItem(item22,doc2.getUri());
        // -> Item 3
        Item item23 = new Item();
        item23.setUri(uriGenerator.newItem());
        udm.saveItem(item23,doc2.getUri());


        // Getting items in domain
        List<String> items = udm.getItemsByDomain(domain.getUri());

        // Delete
        udm.deleteSource(source.getUri());
        udm.deleteDomain(domain.getUri());
        udm.deleteDocument(doc1.getUri());
        udm.deleteDocument(doc2.getUri());
        udm.deleteItem(item11.getUri());
        udm.deleteItem(item12.getUri());
        udm.deleteItem(item21.getUri());
        udm.deleteItem(item22.getUri());
        udm.deleteItem(item23.getUri());

        Assert.assertTrue(items != null);
        Assert.assertEquals(5,items.size());
    }

    @Test
    public void getPartsByDomain(){

        // Source
        Source source = new Source();
        source.setUri(uriGenerator.newSource());
        udm.saveSource(source);

        // Domain
        Domain domain = new Domain();
        domain.setUri(uriGenerator.newDomain());
        udm.saveDomain(domain);

        // Document 1
        Document doc1 = new Document();
        doc1.setUri(uriGenerator.newDocument());
        udm.saveDocument(doc1,source.getUri());
        // -> in domain
        udm.relateDocumentToDomain(doc1.getUri(),domain.getUri(),timeGenerator.getNowAsISO());
        // -> Item 1
        Item item11 = new Item();
        item11.setUri(uriGenerator.newItem());
        udm.saveItem(item11,doc1.getUri());
        // -> -> Part 1
        Part part111 = new Part();
        part111.setUri(uriGenerator.newPart());
        udm.savePart(part111,item11.getUri());
        // -> -> Part 2
        Part part112 = new Part();
        part112.setUri(uriGenerator.newPart());
        udm.savePart(part112,item11.getUri());
        // -> Item2
        Item item12 = new Item();
        item12.setUri(uriGenerator.newItem());
        udm.saveItem(item12,doc1.getUri());
        // -> -> Part 1
        Part part121 = new Part();
        part121.setUri(uriGenerator.newPart());
        udm.savePart(part121,item12.getUri());

        // Document 2
        Document doc2 = new Document();
        doc2.setUri(uriGenerator.newDocument());
        udm.saveDocument(doc2,source.getUri());
        // -> in domain
        udm.relateDocumentToDomain(doc2.getUri(),domain.getUri(),timeGenerator.getNowAsISO());
        // -> Item 1
        Item item21 = new Item();
        item21.setUri(uriGenerator.newItem());
        udm.saveItem(item21,doc2.getUri());
        // -> -> Part 1
        Part part211 = new Part();
        part211.setUri(uriGenerator.newPart());
        udm.savePart(part211,item21.getUri());
        // -> -> Part 2
        Part part212 = new Part();
        part212.setUri(uriGenerator.newPart());
        udm.savePart(part212,item21.getUri());
        // -> Item 2
        Item item22 = new Item();
        item22.setUri(uriGenerator.newItem());
        udm.saveItem(item22,doc2.getUri());
        // -> Item 3
        Item item23 = new Item();
        item23.setUri(uriGenerator.newItem());
        udm.saveItem(item23,doc2.getUri());

        // Getting parts in a domain
        List<String> parts = udm.getPartsByDomain(domain.getUri());

        // Delete
        udm.deleteSource(source.getUri());
        udm.deleteDomain(domain.getUri());
        udm.deleteDocument(doc1.getUri());
        udm.deleteDocument(doc2.getUri());
        udm.deleteItem(item11.getUri());
        udm.deleteItem(item12.getUri());
        udm.deleteItem(item21.getUri());
        udm.deleteItem(item22.getUri());
        udm.deleteItem(item23.getUri());
        udm.deletePart(part111.getUri());
        udm.deletePart(part112.getUri());
        udm.deletePart(part121.getUri());
        udm.deletePart(part211.getUri());
        udm.deletePart(part212.getUri());

        Assert.assertTrue(parts != null);
        Assert.assertEquals(5,parts.size());
    }

}
