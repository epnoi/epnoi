package org.epnoi.modeler.services;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.modeler.Config;
import org.epnoi.storage.model.Domain;
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
 * Created by cbadenes on 11/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {"epnoi.modeler.delay = 5000"})
public class ModelingServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModelingServiceTest.class);

    @Autowired
    ModelingService service;

    @Test
    public void scheduleModelingTasks() throws InterruptedException {

        Domain domain1 = new Domain();
        domain1.setUri("http://epnoi.org/domains/ad8ceb56-e5e4-488b-91be-96ce1a7f115a");

        Domain domain2 = new Domain();
        domain2.setUri("http://epnoi.org/domains/ad8ceb56-e5e4-488b-91be-96ce1a7f4444");

        service.buildModels(domain1);
        Thread.sleep(1000);
        service.buildModels(domain2);
        service.buildModels(domain1);
        Thread.sleep(1000);
        service.buildModels(domain1);
        service.buildModels(domain2);
        Thread.sleep(1000);

        LOG.info("waiting for execution....");
        Thread.sleep(10000);


    }
}
