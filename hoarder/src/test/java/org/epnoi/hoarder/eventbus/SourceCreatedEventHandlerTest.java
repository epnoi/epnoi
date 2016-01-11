package org.epnoi.hoarder.eventbus;

import es.cbadenes.lab.test.IntegrationTest;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.epnoi.hoarder.Config;
import org.epnoi.model.Event;
import org.epnoi.model.Resource;
import org.epnoi.model.Source;
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

import java.util.List;

/**
 * Created by cbadenes on 20/10/15.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = { "epnoi.eventbus.uri = localhost", "epnoi.hoarder.storage.path = hoarder/target/storage" })
public class SourceCreatedEventHandlerTest {

    private static final Logger logger = LoggerFactory.getLogger(SourceCreatedEventHandlerTest.class);

    @Autowired
    EventBus eventBus;

    @Autowired
    CamelContext camelContext;

    @Test
    public void oaipmh() throws Exception {

        List<Route> initialRoutes = camelContext.getRoutes();

        // List of OAI-PMH Repositories: http://www.opendoar.org/new1interfacelist.php?block=oai-pmh

        Source source = new Source();
        source.setUri("/sources/oaipmh");
        // UCM
        // source.setUrl("oaipmh://eprints.ucm.es/cgi/oai2");

        // Nasa Technical Reports Server (NTRS)
//        source.setUrl("oaipmh://ntrs.nasa.gov/oai");

        // MIT
//        source.setUrl("oaipmh://dspace.mit.edu/oai/request");

        // Bournemouth University
        source.setUrl("oaipmh://eprints.bournemouth.ac.uk/cgi/oai2?from=2015-01-01T00:00:00Z");

        logger.info("trying to send a 'source.created' event: " + source);
        this.eventBus.post(Event.from(source), RoutingKey.of(Resource.Type.SOURCE, Resource.State.CREATED));
        logger.info("event sent. Now going to sleep...");
        Thread.currentThread().sleep(25000);
        logger.info("Wake Up. Now going to sleep...");

        List<Route> modifiedRoutes = camelContext.getRoutes();

        Assert.assertEquals("Number of routes", initialRoutes.size() + 1, modifiedRoutes.size());


        long newroute = modifiedRoutes.stream().filter(route -> route.getConsumer().getEndpoint().getEndpointUri().contains(source.getUrl())).count();

        Assert.assertEquals("New Route",1L,newroute);

        camelContext.stop();


    }

    @Test
    public void rss() throws Exception {

        List<Route> initialRoutes = camelContext.getRoutes();

        Source source = new Source();
        source.setUri("/sources/rss");
        source.setUrl("rss://rss.slashdot.org/Slashdot/slashdot");

        logger.info("trying to send a 'source.created' event: " + source);
        this.eventBus.post(Event.from(source), RoutingKey.of(Resource.Type.SOURCE, Resource.State.CREATED));
        logger.info("event sent. Now going to sleep...");
        Thread.currentThread().sleep(5000);
        logger.info("Wake Up. Now going to sleep...");

        List<Route> modifiedRoutes = camelContext.getRoutes();

        Assert.assertEquals("Number of routes", initialRoutes.size() + 1, modifiedRoutes.size());


        long newroute = modifiedRoutes.stream().filter(route -> route.getConsumer().getEndpoint().getEndpointUri().contains(source.name())).count();

        Assert.assertEquals("New Route",1L,newroute);

        camelContext.stop();


    }

}