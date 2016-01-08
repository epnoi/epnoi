package org.epnoi.eventbus.guava;

import org.epnoi.eventbus.Config;
import org.epnoi.model.Event;
import org.epnoi.model.Resource;
import org.epnoi.model.modules.BindingKey;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.EventBusSubscriber;
import org.epnoi.model.modules.RoutingKey;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cbadenes on 14/10/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = { "epnoi.eventbus.uri = localhost" })
public class GuavaEventBusTest {

    private static final Logger LOG = LoggerFactory.getLogger(GuavaEventBus.class);

    @Autowired
    EventBus eventBus;


    @Test
    public void OneToOneCommunication() throws IOException, InterruptedException {

        LOG.info("Starting the bus test");

        final AtomicInteger counter = new AtomicInteger(0);

        eventBus.post(Event.from("nobody should be listening"), RoutingKey.of(Resource.Type.ITEM, Resource.State.CREATED));

        eventBus.subscribe(new EventBusSubscriber() {

            @Override
            public void handle(Event event) {
                LOG.info("Reacting to the event ! " + event.to(String.class));
                counter.incrementAndGet();
            }
        }, BindingKey.of(RoutingKey.of(Resource.Type.ITEM, Resource.State.CREATED),"test"));

        eventBus.post(Event.from((new Date()).toString()), RoutingKey.of(Resource.Type.ITEM, Resource.State.CREATED));

        Thread.sleep(100);

        Assert.assertEquals(1,counter.get());
    }

}
