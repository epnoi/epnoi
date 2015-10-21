package org.epnoi.hoarder.processor;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;


public class HttpTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @Test
    @Ignore
    public void harvest() throws Exception {

        resultEndpoint.expectedMessageCount(1);

        template.sendBody("sample");
        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {


                from("direct:start").
                        setHeader(Exchange.HTTP_METHOD, constant("GET")).
                        setHeader(Exchange.HTTP_URI, constant("http://rss.slashdot.org/~r/Slashdot/slashdot/~3/qFYJLyH7aXA/musician-releases-album-of-music-to-code-by")).
                        to("http://dummyhost?throwExceptionOnFailure=true").
                        to("file:target/?fileName=out.htm").
                        to("mock:result");
            }
        };
    }

}
