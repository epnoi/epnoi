package org.epnoi.hoarder.routes.common;

import org.apache.camel.LoggingLevel;
import org.apache.camel.model.RouteDefinition;
import org.epnoi.hoarder.routes.CommonRoute;
import org.epnoi.hoarder.routes.Expressions;
import org.epnoi.hoarder.routes.SourceProperty;
import org.epnoi.hoarder.routes.processors.TimeGenerator;
import org.epnoi.hoarder.routes.processors.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 27/11/15.
 */
@Component
public class FileSaveRoute implements CommonRoute {

    private static final Logger LOG = LoggerFactory.getLogger(FileSaveRoute.class);

    public static final String URI = "direct:file.save";

    @Value("${storage.path}")
    protected String basedir;

    @Override
    public RouteDefinition definition() {
        return new RouteDefinition().
                from(URI).
                setHeader(SourceProperty.ARGUMENT_PATH, Expressions.simple("${property." + SourceProperty.SOURCE_PROTOCOL + "}/${property." + SourceProperty.SOURCE_NAME + "}/${property" + SourceProperty.PUBLICATION_PUBLISHED_DATE + "}/${header." + SourceProperty.ARGUMENT_NAME + "}")).
                log(LoggingLevel.INFO, LOG, "File Saved: '${header." + SourceProperty.ARGUMENT_PATH + "}'").
                to("file:" + basedir + "/?fileName=${header." + SourceProperty.ARGUMENT_PATH + "}&doneFileName=${file:name}.done");
    }
}
