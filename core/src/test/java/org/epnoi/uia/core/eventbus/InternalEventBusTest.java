package org.epnoi.uia.core.eventbus;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.epnoi.model.Event;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.EventBusPublisher;
import org.epnoi.model.modules.EventBusSubscriber;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cbadenes on 14/10/15.
 */
public class InternalEventBusTest {

    private EventBus eventBus;

    @Before
    public void setup() throws IOException {
        this.eventBus = new InternalEventBusImpl();
        this.eventBus.init();
    }

    @After
    public void destroy() throws IOException {
        this.eventBus.destroy();
    }


    @Test
    public void OneToOneCommunication() throws IOException, InterruptedException {

        System.out.println("Starting the bus test");

        AtomicInteger counter = new AtomicInteger(0);

        EventBusPublisher publisher = new EventBusPublisher() {
            @Override
            public String topic() {
                return "sample.topic";
            }
        };

        eventBus.publish(publisher, new Event("no body should be listening"));

        eventBus.subscribe(new EventBusSubscriber() {
            @Override
            public String topic() {
                return "sample.topic";
            }

            @Override
            public String group() {
                return "sample.group";
            }

            @AllowConcurrentEvents
            @Subscribe
            @Override
            public void onEvent(Event event) {
                System.out.println("Reacting to the event ! " + event);
                counter.incrementAndGet();
            }
        });

        eventBus.publish(publisher, new Event((new Date()).toString()));

        Thread.sleep(100);

        Assert.assertEquals(1,counter.get());
    }

}
