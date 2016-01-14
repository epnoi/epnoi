package org.epnoi.comparator;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.Event;
import org.epnoi.model.Resource;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.storage.model.Analysis;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 13/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class ComparisonTest {

    private static final Logger LOG = LoggerFactory.getLogger(ComparisonTest.class);

    @Autowired
    EventBus eventBus;

    @Test
    public void simulate() throws InterruptedException {

        Analysis analysis = new Analysis();
        analysis.setUri("http://epnoi.org/analyses/421fcfc0-67c0-4f31-a285-3d667fd59f5b");
        analysis.setType("Topic-Model");
        analysis.setDomain("http://epnoi.org/domains/1f02ae0b-7d96-42c6-a944-25a3050bf1e2");

        eventBus.post(Event.from(analysis), RoutingKey.of(Resource.Type.ANALYSIS, Resource.State.CREATED));

        LOG.info("Sleeping..");
        Thread.sleep(60000);
        LOG.info("Wake Up..");


    }

}
