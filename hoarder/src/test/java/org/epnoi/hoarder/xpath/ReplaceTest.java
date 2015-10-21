package org.epnoi.hoarder.xpath;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;


public class ReplaceTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @Test
    public void replace() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" xmlns:provenance=\"http://www.openarchives.org/OAI/2.0/provenance\" xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
                "    <responseDate>2015-03-09T16:03:30Z</responseDate>\n" +
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
                "                    <dc:relation>http://www.sciencepubco.com/index.php/IJAA/article/downloadSuppFile/1315/325</dc:relation>\n" +
                "                </dc>\n" +
                "            </metadata>\n" +
                "        </record>\n" +
                "    </ListRecords>\n" +
                "</OAI-PMH>\n";


        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedHeaderReceived("xpath","http://www.sciencepubco.com/index.php/IJAA/article/download/1315/879");

        template.sendBody(xml);
        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {

                Namespaces ns = new Namespaces("oai", "http://www.openarchives.org/OAI/2.0/");
                ns.add("dc", "http://purl.org/dc/elements/1.1/");
                ns.add("provenance", "http://www.openarchives.org/OAI/2.0/provenance");
                ns.add("oai_dc","http://www.openarchives.org/OAI/2.0/oai_dc/");

                from("direct:start").
                        setHeader("xpath", xpath("replace(substring-before(string-join(//oai:metadata/oai:dc/dc:relation/text(),\";\"),\";\"),\"view\",\"download\")", String.class).namespaces(ns)).
                        to("mock:result");
            }
        };
    }

}
