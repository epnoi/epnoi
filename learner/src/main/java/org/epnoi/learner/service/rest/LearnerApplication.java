package org.epnoi.learner.service.rest;

/**
 * Created by rgonza on 22/11/15.
 */

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;


public class LearnerApplication extends ResourceConfig {


    public LearnerApplication() {
        register(RequestContextFilter.class);
        register(LearnerResource.class);
        register(LearnerConfigurationResource.class);
        register(TrainerResource.class);
        register(TrainerConfigurationResource.class);
        register(JacksonFeature.class);
    }
}
