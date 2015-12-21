package org.epnoi.harvester.routes.common;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.epnoi.harvester.routes.processor.ErrorHandler;
import org.epnoi.harvester.routes.processor.ROBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by cbadenes on 01/12/15.
 */
@Component
public class CommonRouteBuilder extends RouteBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(CommonRouteBuilder.class);

    public static final String URI_RO_BUILD        = "seda:common.ro.build";

    @Autowired
    ErrorHandler errorHandler;

    @Autowired
    ROBuilder roBuilder;


    @Value("${epnoi.hoarder.storage.path}")
    protected String outdir;


    @Override
    public void configure() throws Exception {

        onException(MalformedURLException.class)
                .process(errorHandler).stop();

        onException(IOException.class)
                .maximumRedeliveries(3)
                .process(errorHandler).stop();

        from(URI_RO_BUILD).
                process(roBuilder).
                log(LoggingLevel.INFO,LOG,"File Read: '${header.CamelFileName}'").
                to("file:"+outdir+"?fileName=${header.FileName}");
                // TODO Store it in db

    }
}

