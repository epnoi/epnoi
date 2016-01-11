package org.epnoi.modeler.eventbus;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.Event;
import org.epnoi.model.Resource;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.modeler.Config;
import org.epnoi.storage.model.Domain;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 11/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
//@TestPropertySource(properties = { "epnoi.eventbus.uri = amqp://epnoi:drinventor@zavijava.dia.fi.upm.es:5041/drinventor" })
public class DomainUpdatedEventHandlerTest {

    @Autowired
    EventBus eventBus;

    @Test
    public void simulateDomainUpdated() throws InterruptedException {

        Domain domain = new Domain();
        domain.setUri("http://epnoi.org/models/18ab6c2c-3bd7-460c-b0a9-9cd780a6513c");
        domain.setCreationTime("20150112T10:39");
        domain.setName("test-domain");
        domain.setDescription("For testing purposes");

        eventBus.post(Event.from(domain), RoutingKey.of(Resource.Type.DOMAIN, Resource.State.UPDATED));

        Thread.sleep(2000);

    }

}
