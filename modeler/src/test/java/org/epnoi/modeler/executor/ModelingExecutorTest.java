package org.epnoi.modeler.executor;

import org.epnoi.storage.model.Domain;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 11/01/16.
 */
public class ModelingExecutorTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModelingExecutor.class);

    @Test
    public void onlyOneTime() throws InterruptedException {

        Domain domain = new Domain();

        ModelingExecutor modelingExecutor = new ModelingExecutor(domain,5000);


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
