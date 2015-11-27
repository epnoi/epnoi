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
import org.junit.Ignore;
import org.junit.Test;


public class HttpRelationOAITest extends CamelTestSupport {

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
    @Ignore
    public void harvest() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" xmlns:provenance=\"http://www.openarchives.org/OAI/2.0/provenance\" xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
                "    <responseDate>2015-03-10T07:56:40Z</responseDate>\n" +
                "    <request verb=\"ListRecords\" metadataPrefix=\"oai_dc\">http://www.sciencepubco.com/index.php/IJAA/oai</request>\n" +
                "    <ListRecords>\n" +
                "        <record>\n" +
                "            <header>\n" +
                "                <identifier>oai:ojs.pkp.sfu.ca:article/1315</identifier>\n" +
                "                <datestamp>2014-11-13T08:20:10Z</datestamp>\n" +
                "                <setSpec>IJAA:ART</setSpec>\n" +
                "            </header>\n" +
                "            <metadata>\n" +
                "                <dc>\n" +
                "                    <dc:title xml:lang=\"en-US\">Black hole Cosmos and the Micro Cosmos</dc:title>\n" +
                "                    <dc:creator>UV, Satya Seshavatharam; I-SERVE, Hyderabad, AP, India.\n" +
                "Sr. Engineer, QA-DIP, Lanco Industries Ltd, Tirupati, AP, India.</dc:creator>\n" +
                "                    <dc:creator>S, Lakshminarayana; AU, AP, India</dc:creator>\n" +
                "                    <dc:description xml:lang=\"en-US\"></dc:description>\n" +
                "                    <dc:publisher xml:lang=\"en-US\">Science Publishing Corporation</dc:publisher>\n" +
                "                    <dc:date>2013-10-19</dc:date>\n" +
                "                    <dc:type>info:eu-repo/semantics/article</dc:type>\n" +
                "                    <dc:type>info:eu-repo/semantics/publishedVersion</dc:type>\n" +
                "                    <dc:type xml:lang=\"en-US\">Peer-reviewed Article</dc:type>\n" +
                "                    <dc:format>application/pdf</dc:format>\n" +
                "                    <dc:identifier>http://www.sciencepubco.com/index.php/IJAA/article/view/1315</dc:identifier>\n" +
                "                    <dc:identifier>10.14419/ijaa.v1i2.1315</dc:identifier>\n" +
                "                    <dc:source xml:lang=\"en-US\">International Journal of Advanced Astronomy; Vol 1, No 2 (2013); 37-59</dc:source>\n" +
                "                    <dc:source>2312-7414</dc:source>\n" +
                "                    <dc:source>10.14419/ijaa.v1i2</dc:source>\n" +
                "                    <dc:language>eng</dc:language>\n" +
                "                    <dc:relation>http://www.sciencepubco.com/index.php/IJAA/article/view/1315/879</dc:relation>\n" +
                "                    <dc:relation>http://www.sciencepubco.com/index.php/IJAA/article/downloadSuppFile/1315/325</dc:relation>\n"+
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
                        setProperty(SourceProperty.PUBLICATION_URL, xpath("replace(substring-before(concat(string-join(//oai:metadata/oai:dc/dc:relation/text(),\";\"),\";\"),\";\"),\"view\",\"download\")", String.class).namespaces(ns)).
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
