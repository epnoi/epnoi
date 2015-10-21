package org.epnoi.hoarder.processor;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;


public class FilterOAITest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @Test
    public void deleted() throws Exception {

        resultEndpoint.expectedMessageCount(0);

        String xml = "<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" xmlns:provenance=\"http://www.openarchives.org/OAI/2.0/provenance\" xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
                "    <responseDate>2015-03-09T14:12:01Z</responseDate>\n" +
                "    <request verb=\"ListRecords\" metadataPrefix=\"oai_dc\" from=\"1970-01-01T00:00:00Z\">http://eprints.ucm.es/cgi/oai2</request>\n" +
                "    <ListRecords>\n" +
                "        <record>\n" +
                "            <header status=\"deleted\">\n" +
                "                <identifier>oai:www.ucm.es:1543</identifier>\n" +
                "                <datestamp>2014-02-06T07:30:43Z</datestamp>\n" +
                "            </header>\n" +
                "        </record>\n" +
                "    </ListRecords>\n" +
                "</OAI-PMH>";


        template.sendBody(xml);
        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void nonDeleted() throws Exception {

        resultEndpoint.expectedMessageCount(1);

        String xml = "<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" xmlns:provenance=\"http://www.openarchives.org/OAI/2.0/provenance\" xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
                "    <responseDate>2015-03-09T14:12:01Z</responseDate>\n" +
                "    <request verb=\"ListRecords\" metadataPrefix=\"oai_dc\" from=\"1970-01-01T00:00:00Z\">http://eprints.ucm.es/cgi/oai2</request>\n" +
                "    <ListRecords>\n" +
                "        <record>\n" +
                "            <header>\n" +
                "                <identifier>oai:www.ucm.es:1543</identifier>\n" +
                "                <datestamp>2014-02-06T07:30:43Z</datestamp>\n" +
                "            </header>\n" +
                "        </record>\n" +
                "    </ListRecords>\n" +
                "</OAI-PMH>";


        template.sendBody(xml);
        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {

                Namespaces ns = new Namespaces("oai", "http://www.openarchives.org/OAI/2.0/")
                        .add("dc", "http://purl.org/dc/elements/1.1/")
                        .add("provenance", "http://www.openarchives.org/OAI/2.0/provenance")
                        .add("oai_dc", "http://www.openarchives.org/OAI/2.0/oai_dc/")
                        .add("rss", "http://purl.org/rss/1.0/");

                from("direct:start").
                        choice().
                            when().xpath("//oai:header[@status=\"deleted\"]",String.class,ns).stop().
                        end().
                        to("mock:result");
            }
        };
    }

}
