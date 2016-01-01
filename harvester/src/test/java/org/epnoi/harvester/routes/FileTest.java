package org.epnoi.harvester.routes;

import es.cbadenes.lab.test.IntegrationTest;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.epnoi.model.File;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by cbadenes on 31/12/15.
 */
@Category(IntegrationTest.class)
public class FileTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    @Test
    public void oaipmhMessage() throws Exception {

        resultEndpoint.expectedMessageCount(3);

        Thread.sleep(60000);

//        template.sendBody(xml);
        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {

                Path path = Paths.get("harvester/src/test/resources/inbox");

                from("file://"+path.toFile().getAbsolutePath()+"?"+
                        "recursive=true&" +
                        "noop=true&"+
                        "delete=false&" +
                        "idempotent=true&" +
                        "idempotentKey=${file:name}-${file:size}").
                        to("log:org.epnoi.harvester.routes.FileTest?level=INFO").
                        to("mock:result");

            }
        };
    }

}