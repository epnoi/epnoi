package org.epnoi.storage;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.Event;
import org.epnoi.model.Resource;
import org.epnoi.model.modules.BindingKey;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.EventBusSubscriber;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.storage.graph.domain.DocumentNode;
import org.epnoi.storage.graph.domain.DomainNode;
import org.epnoi.storage.model.Document;
import org.epnoi.storage.model.Domain;
import org.epnoi.storage.model.Item;
import org.epnoi.storage.model.Source;
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

import java.util.Arrays;
import java.util.Iterator;
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

        Domain domain = new Domain();
        domain.setUri(uriGenerator.newDomain());
        udm.saveDomain(domain);

        Document doc1 = new Document();
        doc1.setUri(uriGenerator.newDocument());
        udm.saveDocument(doc1);

        Document doc2 = new Document();
        doc2.setUri(uriGenerator.newDocument());
        udm.saveDocument(doc2);

        udm.relateDocumentToDomain(doc1.getUri(),domain.getUri(),timeGenerator.getNowAsISO());
        udm.relateDocumentToDomain(doc2.getUri(),domain.getUri(),timeGenerator.getNowAsISO());

        List<String> documents = udm.getDocumentsByDomainURI(domain.getUri());

        udm.deleteDomain(domain.getUri());
        udm.deleteDocument(doc1.getUri());
        udm.deleteDocument(doc2.getUri());

        Assert.assertTrue(documents != null);
        Assert.assertEquals(2,documents.size());
    }

    @Test
    public void getItemsByDomain(){

        Domain domain = new Domain();
        domain.setUri(uriGenerator.newDomain());
        udm.saveDomain(domain);

        Document doc1 = new Document();
        doc1.setUri(uriGenerator.newDocument());
        udm.saveDocument(doc1);
        udm.relateDocumentToDomain(doc1.getUri(),domain.getUri(),timeGenerator.getNowAsISO());
        Item item11 = new Item();
        item11.setUri(uriGenerator.newItem());
        udm.saveItem(item11);
        udm.relateItemToDocument(item11.getUri(),doc1.getUri());
        Item item12 = new Item();
        item12.setUri(uriGenerator.newItem());
        udm.saveItem(item12);
        udm.relateItemToDocument(item12.getUri(),doc1.getUri());


        Document doc2 = new Document();
        doc2.setUri(uriGenerator.newDocument());
        udm.saveDocument(doc2);
        udm.relateDocumentToDomain(doc2.getUri(),domain.getUri(),timeGenerator.getNowAsISO());
        Item item21 = new Item();
        item21.setUri(uriGenerator.newItem());
        udm.saveItem(item21);
        udm.relateItemToDocument(item21.getUri(),doc2.getUri());
        Item item22 = new Item();
        item22.setUri(uriGenerator.newItem());
        udm.saveItem(item22);
        udm.relateItemToDocument(item22.getUri(),doc2.getUri());
        Item item23 = new Item();
        item23.setUri(uriGenerator.newItem());
        udm.saveItem(item23);
        udm.relateItemToDocument(item23.getUri(),doc2.getUri());


        List<String> items = udm.getItemsByDomainURI(domain.getUri());

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

}
