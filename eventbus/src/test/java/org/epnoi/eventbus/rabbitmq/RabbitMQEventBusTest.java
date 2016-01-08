package org.epnoi.eventbus.rabbitmq;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.eventbus.Config;
import org.epnoi.model.Event;
import org.epnoi.model.Resource;
import org.epnoi.model.modules.BindingKey;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.RoutingKey;
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

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cbadenes on 13/10/15.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = { "epnoi.eventbus.uri = amqp://epnoi:drinventor@zavijava.dia.fi.upm.es:5040/drinventor" })
public class RabbitMQEventBusTest {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQEventBusTest.class);

    @Autowired
    EventBus eventBus;

    @Test
    public void OneToOneCommunication() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        this.eventBus.subscribe(event -> {
            LOG.info("New event received: " + event.to(String.class));
            received.incrementAndGet();
        }, BindingKey.of(RoutingKey.of(Resource.Type.DOCUMENT, Resource.State.UPDATED),"test1"));

        this.eventBus.post(Event.from("test-message"), RoutingKey.of(Resource.Type.DOCUMENT, Resource.State.UPDATED));

        Thread.sleep(500);

        Assert.assertEquals(1, received.get());
    }


    @Test
    public void OneToMoreExclusiveCommunication() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        this.eventBus.subscribe(event -> {
            LOG.info("[1] New event received: " + event.to(String.class));
            received.incrementAndGet();
        }, BindingKey.of(RoutingKey.of(Resource.Type.SOURCE, Resource.State.UPDATED),"test2"));

        this.eventBus.subscribe(event -> {
            LOG.info("[2] New event received: " + event.to(String.class));
            received.incrementAndGet();
        }, BindingKey.of(RoutingKey.of(Resource.Type.SOURCE, Resource.State.UPDATED),"test2"));

        this.eventBus.post(Event.from("test-message"), RoutingKey.of(Resource.Type.SOURCE, Resource.State.UPDATED));

        Thread.sleep(500);

        Assert.assertEquals(1, received.get());
    }

    @Test
    public void OneToMoreBroadcastCommunication() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        this.eventBus.subscribe(event -> {
            LOG.info("[1] New event received: " + event.to(String.class));
            received.incrementAndGet();
        }, BindingKey.of(RoutingKey.of(Resource.Type.SOURCE, Resource.State.UPDATED),"test3.1"));

        this.eventBus.subscribe(event -> {
            LOG.info("[2] New event received: " + event.to(String.class));
            received.incrementAndGet();
        }, BindingKey.of(RoutingKey.of(Resource.Type.SOURCE, Resource.State.UPDATED),"test3.2"));

        this.eventBus.post(Event.from("test-message"), RoutingKey.of(Resource.Type.SOURCE, Resource.State.UPDATED));

        Thread.sleep(500);

        Assert.assertEquals(2, received.get());
    }


    @Test
    public void OneToOneNonPersistentCommunication() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        this.eventBus.post(Event.from("test-message"), RoutingKey.of(Resource.Type.TOPIC, Resource.State.CREATED));

        Thread.sleep(500);

        this.eventBus.subscribe(event -> {
            LOG.info(" New event received: " + event.to(String.class));
            received.incrementAndGet();
        }, BindingKey.of(RoutingKey.of(Resource.Type.TOPIC, Resource.State.CREATED), "test5"));

        Thread.sleep(500);

        Assert.assertEquals(0, received.get());
    }


    @Test
    public void OneToOneByURI() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        final URI uri    = URI.create("http://epnoi.org/source/1213-1213");

        this.eventBus.post(Event.from(uri), RoutingKey.of(Resource.Type.SOURCE, Resource.State.CREATED));

        Thread.sleep(500);

        this.eventBus.subscribe(event -> {
            URI eventURI = event.to(URI.class);
            LOG.info(" New event received: " + eventURI);
            Assert.assertEquals(uri, eventURI);
            received.incrementAndGet();
        }, BindingKey.of(RoutingKey.of(Resource.Type.SOURCE, Resource.State.CREATED), "test6"));

        Thread.sleep(500);

        Assert.assertEquals(0, received.get());
    }

    @Test
    public void OneToOneByObject() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        final Double value = new Double("23.30");

        this.eventBus.post(Event.from(value), RoutingKey.of(Resource.Type.DOCUMENT, Resource.State.UPDATED));

        Thread.sleep(500);


        this.eventBus.subscribe(event -> {
            Double eventDouble = event.to(Double.class);
            LOG.info(" New event received: " + eventDouble);
            Assert.assertEquals(value, eventDouble);
            received.incrementAndGet();
        }, BindingKey.of(RoutingKey.of(Resource.Type.DOCUMENT, Resource.State.UPDATED), "test7"));

        Thread.sleep(500);

        Assert.assertEquals(0, received.get());
    }



}
