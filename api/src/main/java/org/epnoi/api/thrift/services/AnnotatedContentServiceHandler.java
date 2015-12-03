package org.epnoi.api.thrift.services;

import org.springframework.stereotype.Component;

/**
 * Created by rgonzalez on 3/12/15.
 */
@Component
public class AnnotatedContentServiceHandler extends ThriftServiceHandler {
    private static final String name = "AnnotatedContentServiceHandler";
    @Override
    public String getService() {
        return name;
    }

    public AnnotatedContentServiceHandler() {

    }
}
