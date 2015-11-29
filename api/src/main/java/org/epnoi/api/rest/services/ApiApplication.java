package org.epnoi.api.rest.services;

import org.epnoi.api.rest.services.knowledgebase.KnowledgeBaseResource;
import org.springframework.web.filter.RequestContextFilter;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by rgonza on 27/11/15.
 */
public class ApiApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(RequestContextFilter.class);
        s.add(KnowledgeBaseResource.class);


        return s;
    }
}