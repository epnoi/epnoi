package org.epnoi.modeler.executor;

import org.epnoi.storage.model.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by cbadenes on 11/01/16.
 */

public class ModelingExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(ModelingExecutor.class);

    private final Domain domain;
    private final long delay;
    private ThreadPoolTaskScheduler threadpool;
    private ScheduledFuture<?> task;


    public ModelingExecutor(Domain domain, long delayInMsecs) {
        this.domain = domain;
        this.delay  = delayInMsecs;

        this.threadpool = new ThreadPoolTaskScheduler();
        this.threadpool.setPoolSize(1);
        this.threadpool.initialize();

        LOG.info("created a new modeling executor delayed by: " + delayInMsecs + "msecs for domain: " + domain);
    }

    public ModelingExecutor buildModel(){
        LOG.info("scheduling a new build model task");
        if (task != null) task.cancel(false);
        this.task = this.threadpool.schedule(new ModelingTask(domain), new Date(System.currentTimeMillis() + delay));
        return this;
    }
}
