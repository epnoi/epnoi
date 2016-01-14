package org.epnoi.modeler.executor;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.modeler.Config;
import org.epnoi.modeler.helper.ModelingHelper;
import org.epnoi.storage.model.Domain;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 11/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class ModelingExecutorTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModelingExecutorTest.class);

    @Autowired
    ModelingHelper helper;

    @Test
    public void onlyOneTime() throws InterruptedException {

        Domain domain = new Domain();

        ModelingExecutor modelingExecutor = new ModelingExecutor(domain,helper,5000);

        modelingExecutor.buildModel();
        Thread.sleep(1000);
        modelingExecutor.buildModel();
        Thread.sleep(1000);
        modelingExecutor.buildModel();
        Thread.sleep(1000);

        LOG.info("Waiting for delayed executions...");
        Thread.sleep(5000);

    }

}
