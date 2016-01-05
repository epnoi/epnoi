package org.epnoi.harvester.routes.rss;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.harvester.WebContextConfiguration;
import org.epnoi.model.Event;
import org.epnoi.model.Resource;
import org.epnoi.model.Source;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.storage.UDM;
import org.epnoi.storage.model.ResourceUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 04/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebContextConfiguration.class)
@TestPropertySource(properties = { "epnoi.eventbus.uri = localhost", "epnoi.hoarder.storage.path = hoarder/target/storage" })
public class RssTest {

    private static final Logger LOG = LoggerFactory.getLogger(RssTest.class);

    @Autowired
    EventBus eventBus;

    @Autowired
    UDM udm;

    @Test
    public void readFolder() throws Exception {

        Source source = new Source();
        source.setUri("/sources/rss");
        source.setUrl("rss://rss.slashdot.org/Slashdot/slashdot");

        if (!udm.existSource(source.getUri())){
            udm.saveSource(ResourceUtils.map(source, org.epnoi.storage.model.Source.class));
        }


        LOG.info("trying to send a 'source.created' event: " + source);
        this.eventBus.post(Event.from(source), RoutingKey.of(Resource.Type.SOURCE, Resource.State.CREATED));
        LOG.info("event sent. Now going to sleep...");
        Thread.sleep(120000);

    }
}
