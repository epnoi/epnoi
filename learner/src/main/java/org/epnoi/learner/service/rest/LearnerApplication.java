package org.epnoi.learner.service.rest;

import com.sun.jersey.api.core.ResourceConfig;
import org.springframework.web.filter.RequestContextFilter;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
/**
 * Created by rgonza on 22/11/15.
 */
/*JERSEY 2
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
*/

public class LearnerApplication extends Application {

@Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
    s.add(RequestContextFilter.class);
    s.add(LearnerResource.class);
        s.add(LearnerConfigurationResource.class);
        s.add(TrainerResource.class);
        s.add(TrainerConfigurationResource.class);
        return s;
    }
}
