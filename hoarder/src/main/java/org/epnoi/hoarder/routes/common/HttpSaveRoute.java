package org.epnoi.hoarder.routes.common;

import org.apache.camel.model.RouteDefinition;
import org.epnoi.hoarder.routes.CommonRoute;
import org.epnoi.hoarder.routes.Expressions;
import org.epnoi.hoarder.routes.SourceProperty;
import org.epnoi.hoarder.routes.processors.TimeGenerator;
import org.epnoi.hoarder.routes.processors.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 27/11/15.
 */
@Component
public class HttpSaveRoute implements CommonRoute {

    public static final String URI = "direct:http.save";

    @Autowired
    TimeGenerator timeGenerator;

    @Autowired
    UUIDGenerator uuidGenerator;

    @Override
    public RouteDefinition definition() {
        return new RouteDefinition().
                from(URI).
                process(timeGenerator).
                process(uuidGenerator).
                setHeader(SourceProperty.ARGUMENT_NAME, Expressions.simple("${property." + SourceProperty.PUBLICATION_UUID + "}." + "${property." + SourceProperty.PUBLICATION_METADATA_FORMAT + "}")).
                to(FileSaveRoute.URI).
                setHeader(SourceProperty.ARGUMENT_PATH, Expressions.simple("${property." + SourceProperty.PUBLICATION_URL + "}")).
                to(HttpDownloadRoute.URI).
                setHeader(SourceProperty.ARGUMENT_NAME, Expressions.simple("${property." + SourceProperty.PUBLICATION_UUID + "}." + "${property." + SourceProperty.PUBLICATION_FORMAT + "}")).
                to(FileSaveRoute.URI).
                setProperty(SourceProperty.PUBLICATION_URL_LOCAL, Expressions.simple("${header." + SourceProperty.ARGUMENT_PATH + "}"));
    }
}
