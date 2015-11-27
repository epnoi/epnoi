package org.epnoi.hoarder.routes.common;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.model.ExpressionNode;
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
public class HttpDownloadRoute implements CommonRoute {

    public static final String URI = "direct:http.download";

    @Override
    public RouteDefinition definition() {
        RouteDefinition route = new RouteDefinition();
        // Filter resources with available url
        route.from(URI).
            filter(Expressions.value(Expressions.header(SourceProperty.ARGUMENT_PATH)).isNotEqualTo("")).
            setHeader(Exchange.HTTP_METHOD, Expressions.constant("GET")).
            setHeader(Exchange.HTTP_URI, Expressions.simple("${header." + SourceProperty.ARGUMENT_PATH + "}")).
            to("http://dummyhost?throwExceptionOnFailure=false");
        return route;
    }
}
