package org.epnoi.hoarder.xpath;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.epnoi.model.Record;
import org.junit.Test;


public class OAITest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @Test
    public void oaipmhMessage() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" xmlns:provenance=\"http://www.openarchives.org/OAI/2.0/provenance\" xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
                "    <responseDate>2015-03-03T09:38:52Z</responseDate>\n" +
                "    <request verb=\"ListRecords\" metadataPrefix=\"oai_dc\" from=\"2015-03-03T05:00:00Z\">http://eprints.ucm.es/cgi/oai2</request>\n" +
                "    <ListRecords>\n" +
                "        <record>\n" +
                "            <header>\n" +
                "                <identifier>oai:www.ucm.es:28868</identifier>\n" +
                "                <datestamp>2015-03-03T09:30:04Z</datestamp>\n" +
                "                <setSpec>7374617475733D707562</setSpec>\n" +
                "                <setSpec>7375626A656374733D41:415F3131:415F31315F323836</setSpec>\n" +
                "                <setSpec>7375626A656374733D41:415F3131:415F31315F333031</setSpec>\n" +
                "                <setSpec>74797065733D61727469636C65</setSpec>\n" +
                "            </header>\n" +
                "            <metadata>\n" +
                "                <dc>\n" +
                "                    <dc:relation>http://eprints.ucm.es/28868/</dc:relation>\n" +
                "                    <dc:title>Radiation tolerant isolation amplifiers for temperature measurement</dc:title>\n" +
                "                    <dc:creator>Zong, Yi</dc:creator>\n" +
                "                    <dc:creator>Franco Peláez, Francisco Javier</dc:creator>\n" +
                "                    <dc:creator>Agapito Serrano, Juan Andrés</dc:creator>\n" +
                "                    <dc:creator>Fernandes, Ana C.</dc:creator>\n" +
                "                    <dc:creator>Marques, José G.</dc:creator>\n" +
                "                    <dc:subject>Electrónica</dc:subject>\n" +
                "                    <dc:subject>Radiactividad</dc:subject>\n" +
                "                    <dc:description>This paper concentrates on the selection of </dc:description>\n" +
                "                    <dc:publisher>Elsevier Science BV</dc:publisher>\n" +
                "                    <dc:date>2006-09-29</dc:date>\n" +
                "                    <dc:type>info:eu-repo/semantics/article</dc:type>\n" +
                "                    <dc:type>PeerReviewed</dc:type>\n" +
                "                    <dc:identifier>http://eprints.ucm.es/28868/1/Zong2006_Eprint.pdf</dc:identifier>\n" +
                "                    <dc:format>application/pdf</dc:format>\n" +
                "                    <dc:language>en</dc:language>\n" +
                "                    <dc:rights>info:eu-repo/semantics/openAccess</dc:rights>\n" +
                "                    <dc:relation>http://www.sciencedirect.com/science/article/pii/S0168900206015774</dc:relation>\n" +
                "                    <dc:relation>10.1016/j.nima.2006.09.007</dc:relation>\n" +
                "                    <dc:relation>FPA2002-00912</dc:relation>\n" +
                "                    <dc:relation>K476/LHC</dc:relation>\n" +
                "                </dc>\n" +
                "            </metadata>\n" +
                "        </record>\n" +
                "    </ListRecords>\n" +
                "</OAI-PMH>\n";


        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedHeaderReceived(Record.PUBLICATION_TITLE,"Radiation tolerant isolation amplifiers for temperature measurement");
        resultEndpoint.expectedHeaderReceived(Record.PUBLICATION_DESCRIPTION,"This paper concentrates on the selection of ");
        resultEndpoint.expectedHeaderReceived(Record.PUBLICATION_PUBLISHED,"2015-03-03T09:30:04Z");
        resultEndpoint.expectedHeaderReceived(Record.PUBLICATION_URI,"http://eprints.ucm.es/28868/1/Zong2006_Eprint.pdf");
        resultEndpoint.expectedHeaderReceived(Record.PUBLICATION_URL,"http://eprints.ucm.es/28868/1/Zong2006_Eprint.pdf");
        resultEndpoint.expectedHeaderReceived(Record.PUBLICATION_LANGUAGE,"en");
        resultEndpoint.expectedHeaderReceived(Record.PUBLICATION_RIGHTS,"info:eu-repo/semantics/openAccess");
        resultEndpoint.expectedHeaderReceived(Record.PUBLICATION_CREATORS,"Zong, Yi;Franco Peláez, Francisco Javier;Agapito Serrano, Juan Andrés;Fernandes, Ana C.;Marques, José G.");
        resultEndpoint.expectedHeaderReceived(Record.PUBLICATION_FORMAT,"pdf");

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
                        setHeader(Record.SOURCE_NAME,                   constant("ucm")).
                        setHeader(Record.SOURCE_URI, constant("http://www.epnoi.org/oai-providers/ucm")).
                        setHeader(Record.SOURCE_URL, constant("http://eprints.ucm.es/cgi/oai2")).
                        setHeader(Record.SOURCE_PROTOCOL,               constant("oaipmh")).
                        setHeader(Record.PUBLICATION_TITLE,             xpath("//oai:metadata/oai:dc/dc:title/text()", String.class).namespaces(ns)).
                        setHeader(Record.PUBLICATION_DESCRIPTION,       xpath("//oai:metadata/oai:dc/dc:description/text()", String.class).namespaces(ns)).
                        setHeader(Record.PUBLICATION_PUBLISHED,         xpath("//oai:header/oai:datestamp/text()", String.class).namespaces(ns)).
                        setHeader(Record.PUBLICATION_URI,               xpath("//oai:metadata/oai:dc/dc:identifier/text()", String.class).namespaces(ns)).
                        setHeader(Record.PUBLICATION_URL,        xpath("//oai:metadata/oai:dc/dc:identifier/text()", String.class).namespaces(ns)).
                        setHeader(Record.PUBLICATION_LANGUAGE,          xpath("//oai:metadata/oai:dc/dc:language/text()", String.class).namespaces(ns)).
                        setHeader(Record.PUBLICATION_RIGHTS,            xpath("//oai:metadata/oai:dc/dc:rights/text()", String.class).namespaces(ns)).
                        setHeader(Record.PUBLICATION_CREATORS,          xpath("string-join(//oai:metadata/oai:dc/dc:creator/text(),\";\")", String.class).namespaces(ns)).
                        setHeader(Record.PUBLICATION_FORMAT,            xpath("substring-after(//oai:metadata/oai:dc/dc:format[1]/text(),\"/\")", String.class).namespaces(ns)).
                        setHeader(Record.PUBLICATION_METADATA_FORMAT, constant("xml")).
                        to("log:es.upm.oeg.epnoi.hoarder?level=INFO").
                        to("mock:result");
            }
        };
    }

}
