package org.epnoi.modeler.scheduler;

import org.epnoi.modeler.helper.ModelingHelper;
import org.epnoi.storage.model.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by cbadenes on 11/01/16.
 */

public class ModelingPoolExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(ModelingPoolExecutor.class);

    private final Domain domain;
    private final long delay;
    private final ModelingHelper helper;

    private ThreadPoolTaskScheduler threadpool;
    private ScheduledFuture<?> task;


    public ModelingPoolExecutor(Domain domain, ModelingHelper helper, long delayInMsecs) {
        this.domain = domain;
        this.delay  = delayInMsecs;
        this.helper = helper;

        this.threadpool = new ThreadPoolTaskScheduler();
        this.threadpool.setPoolSize(1);
        this.threadpool.initialize();

        LOG.info("created a new modeling executor delayed by: " + delayInMsecs + "msecs for domain: " + domain);
    }

    public ModelingPoolExecutor buildModel(){
        LOG.info("scheduling a new build model task");
        if (task != null) task.cancel(false);
        this.task = this.threadpool.schedule(new ModelingTask(domain,helper), new Date(System.currentTimeMillis() + delay));
        return this;
    }
}
