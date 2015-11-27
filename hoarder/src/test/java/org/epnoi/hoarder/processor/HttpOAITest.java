package org.epnoi.hoarder.processor;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.epnoi.hoarder.routes.SourceProperty;
import org.epnoi.hoarder.routes.processors.TimeGenerator;
import org.epnoi.hoarder.utils.FileServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class HttpOAITest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    private FileServer server;

    @Before
    public void setup() throws Exception {
        server = new FileServer();
        server.start(8080, "src/test/resources");
    }

    @After
    public void close() throws Exception {
        server.stop();
    }

    @Test
    public void harvest() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" xmlns:provenance=\"http://www.openarchives.org/OAI/2.0/provenance\" xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
                "    <responseDate>2015-03-03T14:28:29Z</responseDate>\n" +
                "    <request verb=\"ListRecords\" metadataPrefix=\"oai_dc\" from=\"2015-03-03T13:00:00Z\">http://eprints.ucm.es/cgi/oai2</request>\n" +
                "    <ListRecords>\n" +
                "        <record>\n" +
                "            <header>\n" +
                "                <identifier>oai:www.ucm.es:28974</identifier>\n" +
                "                <datestamp>2015-03-03T13:38:54Z</datestamp>\n" +
                "                <setSpec>7374617475733D756E707562</setSpec>\n" +
                "                <setSpec>7375626A656374733D42:425F33</setSpec>\n" +
                "                <setSpec>74797065733D746865736973</setSpec>\n" +
                "            </header>\n" +
                "            <metadata>\n" +
                "                <dc>\n" +
                "                    <dc:relation>http://eprints.ucm.es/28974/</dc:relation>\n" +
                "                    <dc:title>Identificación y caracterización funcional del complejo nuclear de proteínas LSM de \"Arabidopsis thaliana\" en la respuesta de aclimatación a las temperaturas bajas</dc:title>\n" +
                "                    <dc:creator>Hernández Verdeja, Tamara</dc:creator>\n" +
                "                    <dc:creator>Fernandez Libre, Antonio</dc:creator>\n" +
                "                    <dc:contributor>Salinas Muñoz, Julio</dc:contributor>\n" +
                "                    <dc:subject>Biología</dc:subject>\n" +
                "                    <dc:publisher>Universidad Complutense de Madrid</dc:publisher>\n" +
                "                    <dc:date>2014-12-12</dc:date>\n" +
                "                    <dc:type>info:eu-repo/semantics/doctoralThesis</dc:type>\n" +
                "                    <dc:type>PeerReviewed</dc:type>\n" +
                "                    <dc:identifier>http://localhost:8080/oaipmh/resource-1425392911570-reduced.pdf</dc:identifier>\n" +
                "                    <dc:format>application/pdf</dc:format>\n" +
                "                    <dc:language>es</dc:language>\n" +
                "                    <dc:rights>info:eu-repo/semantics/openAccess</dc:rights>\n" +
                "                    <dc:relation>http://localhost:8080/oaipmh/resource-1425392911570-reduced.pdf</dc:relation>\n" +
                "                </dc>\n" +
                "            </metadata>\n" +
                "        </record>\n" +
                "    </ListRecords>\n" +
                "</OAI-PMH>\n";


        resultEndpoint.expectedMessageCount(1);

        template.sendBody(xml);
        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {

                /************************************************************************************************************
                 * OAI-PMH Data Providers
                 ************************************************************************************************************/

                TimeGenerator timeClock = new TimeGenerator();

                Namespaces ns = new Namespaces("oai", "http://www.openarchives.org/OAI/2.0/")
                        .add("dc", "http://purl.org/dc/elements/1.1/")
                        .add("provenance", "http://www.openarchives.org/OAI/2.0/provenance")
                        .add("oai_dc", "http://www.openarchives.org/OAI/2.0/oai_dc/")
                        .add("rss", "http://purl.org/rss/1.0/");

                from("direct:start").
                        setProperty(SourceProperty.SOURCE_NAME, constant("ucm")).
                        setProperty(SourceProperty.SOURCE_URI, constant("http://www.epnoi.org/oai-providers/ucm")).
                        setProperty(SourceProperty.SOURCE_URL, constant("http://eprints.ucm.es/cgi/oai2")).
                        setProperty(SourceProperty.SOURCE_PROTOCOL, constant("oaipmh")).
                        setProperty(SourceProperty.PUBLICATION_TITLE, xpath("//oai:metadata/oai:dc/dc:title/text()", String.class).namespaces(ns)).
                        setProperty(SourceProperty.PUBLICATION_DESCRIPTION, xpath("//oai:metadata/oai:dc/dc:description/text()", String.class).namespaces(ns)).
                        setProperty(SourceProperty.PUBLICATION_PUBLISHED, xpath("//oai:header/oai:datestamp/text()", String.class).namespaces(ns)).
                        setProperty(SourceProperty.PUBLICATION_URI, xpath("//oai:header/oai:identifier/text()", String.class).namespaces(ns)).
                        setProperty(SourceProperty.PUBLICATION_URL, xpath("//oai:metadata/oai:dc/dc:identifier/text()", String.class).namespaces(ns)).
                        setProperty(SourceProperty.PUBLICATION_LANGUAGE, xpath("//oai:metadata/oai:dc/dc:language/text()", String.class).namespaces(ns)).
                        setProperty(SourceProperty.PUBLICATION_RIGHTS, xpath("//oai:metadata/oai:dc/dc:rights/text()", String.class).namespaces(ns)).
                        setProperty(SourceProperty.PUBLICATION_CREATORS, xpath("string-join(//oai:metadata/oai:dc/dc:creator/text(),\";\")", String.class).namespaces(ns)).
                        setProperty(SourceProperty.PUBLICATION_FORMAT, constant("pdf")).
                        setProperty(SourceProperty.PUBLICATION_METADATA_FORMAT, constant("xml")).
                        to("seda:inbox");



                /************************************************************************************************************
                 * Common retriever and storer
                 ************************************************************************************************************/

                from("seda:inbox").
                        process(timeClock).
                        setProperty(SourceProperty.PUBLICATION_REFERENCE_URL,
                                simple("${property." + SourceProperty.SOURCE_PROTOCOL + "}/" +
                                        "${property." + SourceProperty.SOURCE_NAME + "}/" +
                                        "${property." + SourceProperty.PUBLICATION_PUBLISHED_DATE + "}/" +
                                        "resource-${property." + SourceProperty.PUBLICATION_PUBLISHED_MILLIS + "}.${property." + SourceProperty.PUBLICATION_METADATA_FORMAT + "}")).
                        to("file:target/?fileName=${property." + SourceProperty.PUBLICATION_REFERENCE_URL + "}").
                        setHeader(Exchange.HTTP_METHOD, constant("GET")).
                        setHeader(Exchange.HTTP_URI, simple("${property." + SourceProperty.PUBLICATION_URL + "}")).
                        to("http://dummyhost?throwExceptionOnFailure=false").
                        setProperty(SourceProperty.PUBLICATION_URL_LOCAL,
                                simple("${property." + SourceProperty.SOURCE_PROTOCOL + "}/" +
                                        "${property." + SourceProperty.SOURCE_NAME + "}/" +
                                        "${property." + SourceProperty.PUBLICATION_PUBLISHED_DATE + "}/" +
                                        "resource-${property." + SourceProperty.PUBLICATION_PUBLISHED_MILLIS + "}.${property." + SourceProperty.PUBLICATION_FORMAT + "}")).
                        to("file:target/?fileName=${property." + SourceProperty.PUBLICATION_URL_LOCAL + "}").
                        to("mock:result");
            }
        };
    }

}
