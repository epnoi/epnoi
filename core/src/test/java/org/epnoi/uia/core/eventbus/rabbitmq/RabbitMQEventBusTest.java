package org.epnoi.uia.core.eventbus.rabbitmq;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.Event;
import org.epnoi.model.Resource;
import org.epnoi.model.State;
import org.epnoi.model.modules.*;
import org.epnoi.uia.core.eventbus.EventBusConfigTest;
import org.epnoi.uia.core.eventbus.rabbitmq.RabbitMQEventBus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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
@ContextConfiguration(classes = EventBusConfigTest.class)
@TestPropertySource(properties = { "epnoi.eventbus.uri = amqp://guest:guest@192.168.99.100:5672/drinventor" })
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
        }, BindingKey.of(RoutingKey.of(Resource.Type.DOCUMENT, State.CLOSED),"test1"));

        this.eventBus.post(Event.from("test-message"), RoutingKey.of(Resource.Type.DOCUMENT, State.CLOSED));

        Thread.sleep(500);

        Assert.assertEquals(1, received.get());
    }


    @Test
    public void OneToMoreExclusiveCommunication() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        this.eventBus.subscribe(event -> {
            LOG.info("[1] New event received: " + event.to(String.class));
            received.incrementAndGet();
        }, BindingKey.of(RoutingKey.of(Resource.Type.SOURCE, State.CLOSED),"test2"));

        this.eventBus.subscribe(event -> {
            LOG.info("[2] New event received: " + event.to(String.class));
            received.incrementAndGet();
        }, BindingKey.of(RoutingKey.of(Resource.Type.SOURCE, State.CLOSED),"test2"));

        this.eventBus.post(Event.from("test-message"), RoutingKey.of(Resource.Type.SOURCE,State.CLOSED));

        Thread.sleep(500);

        Assert.assertEquals(1, received.get());
    }

    @Test
    public void OneToMoreBroadcastCommunication() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        this.eventBus.subscribe(event -> {
            LOG.info("[1] New event received: " + event.to(String.class));
            received.incrementAndGet();
        }, BindingKey.of(RoutingKey.of(Resource.Type.SOURCE, State.OPENED),"test3.1"));

        this.eventBus.subscribe(event -> {
            LOG.info("[2] New event received: " + event.to(String.class));
            received.incrementAndGet();
        }, BindingKey.of(RoutingKey.of(Resource.Type.SOURCE, State.OPENED),"test3.2"));

        this.eventBus.post(Event.from("test-message"), RoutingKey.of(Resource.Type.SOURCE, State.OPENED));

        Thread.sleep(500);

        Assert.assertEquals(2, received.get());
    }


    @Test
    public void OneToOneNonPersistentCommunication() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        this.eventBus.post(Event.from("test-message"), RoutingKey.of(Resource.Type.MODEL, State.NEW));

        Thread.sleep(500);

        this.eventBus.subscribe(event -> {
            LOG.info(" New event received: " + event.to(String.class));
            received.incrementAndGet();
        }, BindingKey.of(RoutingKey.of(Resource.Type.MODEL, State.NEW), "test5"));

        Thread.sleep(500);

        Assert.assertEquals(0, received.get());
    }


    @Test
    public void OneToOneByURI() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        final URI uri    = URI.create("http://epnoi.org/source/1213-1213");

        this.eventBus.post(Event.from(uri), RoutingKey.of(Resource.Type.SOURCE, State.NEW));

        Thread.sleep(500);

        this.eventBus.subscribe(event -> {
            URI eventURI = event.to(URI.class);
            LOG.info(" New event received: " + eventURI);
            Assert.assertEquals(uri, eventURI);
            received.incrementAndGet();
        }, BindingKey.of(RoutingKey.of(Resource.Type.SOURCE, State.NEW), "test6"));

        Thread.sleep(500);

        Assert.assertEquals(0, received.get());
    }

    @Test
    public void OneToOneByObject() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        final Double value = new Double("23.30");

        this.eventBus.post(Event.from(value), RoutingKey.of(Resource.Type.DOCUMENT, State.MARKED));

        Thread.sleep(500);


        this.eventBus.subscribe(event -> {
            Double eventDouble = event.to(Double.class);
            LOG.info(" New event received: " + eventDouble);
            Assert.assertEquals(value, eventDouble);
            received.incrementAndGet();
        }, BindingKey.of(RoutingKey.of(Resource.Type.DOCUMENT, State.MARKED), "test7"));

        Thread.sleep(500);

        Assert.assertEquals(0, received.get());
    }



}
