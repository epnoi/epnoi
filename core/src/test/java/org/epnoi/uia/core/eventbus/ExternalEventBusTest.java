package org.epnoi.uia.core.eventbus;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.Event;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.EventBusPublisher;
import org.epnoi.model.modules.EventBusSubscriber;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cbadenes on 13/10/15.
 */
@Category(IntegrationTest.class)
public class ExternalEventBusTest {

    private EventBus eventBus;

    @Before
    public void setup() throws IOException {
        this.eventBus = new ExternalEventBusImpl("amqp://guest:guest@eventBus:5672/drinventor");
        this.eventBus.init();
    }

    @After
    public void destroy() throws IOException {
        this.eventBus.destroy();
    }


    @Test
    public void OneToOneCommunication() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        this.eventBus.subscribe(new EventBusSubscriber() {
            @Override
            public String topic() {
                return "test1.source.new";
            }

            @Override
            public String group() {
                return "test1.consumer";
            }

            @Override
            public void onEvent(Event event) {
                System.out.println("New event received: " + event);
                received.incrementAndGet();
            }
        });

        this.eventBus.publish(new EventBusPublisher() {
            @Override
            public String topic() {
                return "test1.source.new";
            }
        }, new Event.Builder().fromString("test-message"));

        Thread.sleep(500);

        Assert.assertEquals(1, received.get());
    }


    @Test
    public void OneToMoreExclusiveCommunication() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        this.eventBus.subscribe(new EventBusSubscriber() {
            @Override
            public String topic() {
                return "test2.source.new";
            }

            @Override
            public String group() {
                return "test2.consumer";
            }

            @Override
            public void onEvent(Event event) {
                System.out.println("[1] New event received: " + event);
                received.incrementAndGet();
            }
        });

        this.eventBus.subscribe(new EventBusSubscriber() {
            @Override
            public String topic() {
                return "test2.source.new";
            }

            @Override
            public String group() {
                return "test2.consumer";
            }

            @Override
            public void onEvent(Event event) {
                System.out.println("[2] New event received: " + event);
                received.incrementAndGet();
            }
        });

        this.eventBus.publish(new EventBusPublisher() {
            @Override
            public String topic() {
                return "test2.source.new";
            }
        }, new Event.Builder().fromString("test-message"));

        Thread.sleep(500);

        Assert.assertEquals(1, received.get());
    }

    @Test
    public void OneToMoreBroadcastCommunication() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        this.eventBus.subscribe(new EventBusSubscriber() {
            @Override
            public String topic() {
                return "test3.source.new";
            }

            @Override
            public String group() {
                return "test3.consumer1";
            }

            @Override
            public void onEvent(Event event) {
                System.out.println("[1] New event received: " + event.toString());
                received.incrementAndGet();
            }
        });

        this.eventBus.subscribe(new EventBusSubscriber() {
            @Override
            public String topic() {
                return "test3.source.new";
            }

            @Override
            public String group() {
                return "test3.consumer2";
            }

            @Override
            public void onEvent(Event event) {
                System.out.println("[2] New event received: " + event);
                received.incrementAndGet();
            }
        });

        this.eventBus.publish(new EventBusPublisher() {
            @Override
            public String topic() {
                return "test3.source.new";
            }
        }, new Event.Builder().fromString("test-message"));

        Thread.sleep(500);

        Assert.assertEquals(2, received.get());
    }


    @Test
    public void OneToOneNonPersistentCommunication() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        this.eventBus.publish(new EventBusPublisher() {
            @Override
            public String topic() {
                return "test4.source.new";
            }
        }, new Event.Builder().fromString("test-message"));

        Thread.sleep(500);


        this.eventBus.subscribe(new EventBusSubscriber() {
            @Override
            public String topic() {
                return "test4.source.new";
            }

            @Override
            public String group() {
                return "test4.consumer1";
            }

            @Override
            public void onEvent(Event event) {
                System.out.println(" New event received: " + event);
                received.incrementAndGet();
            }
        });

        Thread.sleep(500);

        Assert.assertEquals(0, received.get());
    }


    @Test
    public void OneToOneByURI() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        final String topic  = "test5.source.new";

        final String uri    = "http://epnoi.org/source/1213-1213";

        this.eventBus.publish(new EventBusPublisher() {
            @Override
            public String topic() {
                return topic;
            }
        }, new Event.Builder().fromURI(URI.create(uri)));

        Thread.sleep(500);


        this.eventBus.subscribe(new EventBusSubscriber() {
            @Override
            public String topic() {
                return topic;
            }

            @Override
            public String group() {
                return "test-subscriber";
            }

            @Override
            public void onEvent(Event event) {

                System.out.println(" New event received: " + event);
                Assert.assertEquals(URI.create(uri),event.toURI());

                received.incrementAndGet();
            }
        });

        Thread.sleep(500);

        Assert.assertEquals(0, received.get());
    }

    @Test
    public void OneToOneByObject() throws InterruptedException, IOException {

        final AtomicInteger received = new AtomicInteger(0);

        final String topic  = "test5.source.new";

        this.eventBus.publish(new EventBusPublisher() {
            @Override
            public String topic() {
                return topic;
            }
        }, new Event.Builder().fromObject(new Double("23.30")));

        Thread.sleep(500);


        this.eventBus.subscribe(new EventBusSubscriber() {
            @Override
            public String topic() {
                return topic;
            }

            @Override
            public String group() {
                return "test-subscriber";
            }

            @Override
            public void onEvent(Event event) {

                System.out.println(" New event received: " + event);
                Assert.assertEquals(new Double("23.30"),(Double) event.toObject());

                received.incrementAndGet();
            }
        });

        Thread.sleep(500);

        Assert.assertEquals(0, received.get());
    }



}
