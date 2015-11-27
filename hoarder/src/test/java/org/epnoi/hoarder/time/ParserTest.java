package org.epnoi.hoarder.time;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.epnoi.hoarder.routes.SourceProperty;
import org.epnoi.hoarder.routes.processors.TimeGenerator;
import org.junit.Test;

public class ParserTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @Test
    public void validTime() throws Exception {
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedPropertyReceived(SourceProperty.PUBLICATION_PUBLISHED_DATE, "2015-03-03");
        resultEndpoint.expectedPropertyReceived(SourceProperty.PUBLICATION_PUBLISHED_MILLIS,"1425389934000");

        template.sendBodyAndProperty("message", SourceProperty.PUBLICATION_PUBLISHED, "2015-03-03T13:38:54Z");

        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {

                TimeGenerator timeClock = new TimeGenerator();

                from("direct:start")
                        .process(timeClock)
                        .to("mock:result");
            }
        };
    }

}