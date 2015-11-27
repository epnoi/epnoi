package org.epnoi.hoarder.xpath;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.epnoi.hoarder.routes.SourceProperty;
import org.junit.Test;


public class RSSTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @Test
    public void rssMessage() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns=\"http://purl.org/rss/1.0/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:sy=\"http://purl.org/rss/1.0/modules/syndication/\">\n" +
                "  <channel rdf:about=\"http://slashdot.org/\">\n" +
                "    <title>Slashdot</title>\n" +
                "    <link>http://slashdot.org/</link>\n" +
                "    <description>News for nerds, stuff that matters</description>\n" +
                "    <items>\n" +
                "      <rdf:Seq>\n" +
                "        <rdf:li rdf:resource=\"http://mobile.slashdot.org/story/15/03/05/2137215/eu-free-data-roaming-net-neutrality-plans-in-jeopardy?utm_source=rss1.0mainlinkanon&amp;utm_medium=feed\" />\n" +
                "      </rdf:Seq>\n" +
                "    </items>\n" +
                "    <sy:updatePeriod>hourly</sy:updatePeriod>\n" +
                "    <sy:updateFrequency>1</sy:updateFrequency>\n" +
                "    <sy:updateBase>1970-01-01T00:00:00Z</sy:updateBase>\n" +
                "    <dc:creator>help@slashdot.org</dc:creator>\n" +
                "    <dc:subject>Technology</dc:subject>\n" +
                "    <dc:publisher>Dice</dc:publisher>\n" +
                "    <dc:date>2015-03-06T13:19:09Z</dc:date>\n" +
                "    <dc:language>en-us</dc:language>\n" +
                "    <dc:rights>Copyright 1997-2015, Dice. All Rights Reserved. Slashdot is a Dice Holdings, Inc. service</dc:rights>\n" +
                "  </channel>\n" +
                "  <image>\n" +
                "    <title>Slashdot</title>\n" +
                "    <url>http://a.fsdn.com/sd/topics/topicslashdot.gif</url>\n" +
                "    <link>http://slashdot.org/</link>\n" +
                "  </image>\n" +
                "  <item rdf:about=\"http://mobile.slashdot.org/story/15/03/05/2137215/eu-free-data-roaming-net-neutrality-plans-in-jeopardy?utm_source=rss1.0mainlinkanon&amp;utm_medium=feed\">\n" +
                "    <title>EU Free Data Roaming, Net Neutrality Plans In Jeopardy</title>\n" +
                "    <link>http://rss.slashdot.org/~r/Slashdot/slashdot/~3/7DWYLD8XMjk/eu-free-data-roaming-net-neutrality-plans-in-jeopardy</link>\n" +
                "    <slash:department xmlns:slash=\"http://purl.org/rss/1.0/modules/slash/\">can't-we-all-just-not-get-along?</slash:department>\n" +
                "    <slash:section xmlns:slash=\"http://purl.org/rss/1.0/modules/slash/\">mobile</slash:section>\n" +
                "    <slash:comments xmlns:slash=\"http://purl.org/rss/1.0/modules/slash/\">45</slash:comments>\n" +
                "    <slash:hit_parade xmlns:slash=\"http://purl.org/rss/1.0/modules/slash/\">45,44,27,22,7,3,0</slash:hit_parade>\n" +
                "    <feedburner:origLink xmlns:feedburner=\"http://rssnamespace.org/feedburner/ext/1.0\">http://mobile.slashdot.org/story/15/03/05/2137215/eu-free-data-roaming-net-neutrality-plans-in-jeopardy?utm_source=rss1.0mainlinkanon&amp;utm_medium=feed</feedburner:origLink>\n" +
                "    <description>An anonymous reader writes EU free</description>\n" +
                "    <dc:creator>timothy</dc:creator>\n" +
                "    <dc:subject>eu</dc:subject>\n" +
                "    <dc:date>2015-03-05T22:00:00Z</dc:date>\n" +
                "  </item>\n" +
                "  <atom10:link xmlns:atom10=\"http://www.w3.org/2005/Atom\" rel=\"self\" type=\"application/rdf+xml\" href=\"http://rss.slashdot.org/Slashdot/slashdot\" />\n" +
                "  <feedburner:info xmlns:feedburner=\"http://rssnamespace.org/feedburner/ext/1.0\" uri=\"slashdot/slashdot\" />\n" +
                "  <atom10:link xmlns:atom10=\"http://www.w3.org/2005/Atom\" rel=\"hub\" href=\"http://pubsubhubbub.appspot.com/\" />\n" +
                "</rdf:RDF>\n" +
                "\n";

        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedHeaderReceived(SourceProperty.PUBLICATION_TITLE,"EU Free Data Roaming, Net Neutrality Plans In Jeopardy");
        resultEndpoint.expectedHeaderReceived(SourceProperty.PUBLICATION_DESCRIPTION,"An anonymous reader writes EU free");
        resultEndpoint.expectedHeaderReceived(SourceProperty.PUBLICATION_PUBLISHED,"2015-03-05T22:00:00Z");
        resultEndpoint.expectedHeaderReceived(SourceProperty.PUBLICATION_URI,"http://rss.slashdot.org/~r/Slashdot/slashdot/~3/7DWYLD8XMjk/eu-free-data-roaming-net-neutrality-plans-in-jeopardy");
        resultEndpoint.expectedHeaderReceived(SourceProperty.PUBLICATION_URL,"http://rss.slashdot.org/~r/Slashdot/slashdot/~3/7DWYLD8XMjk/eu-free-data-roaming-net-neutrality-plans-in-jeopardy");
        resultEndpoint.expectedHeaderReceived(SourceProperty.PUBLICATION_LANGUAGE,"en-us");
        resultEndpoint.expectedHeaderReceived(SourceProperty.PUBLICATION_RIGHTS,"Copyright 1997-2015, Dice. All Rights Reserved. Slashdot is a Dice Holdings, Inc. service");
        resultEndpoint.expectedHeaderReceived(SourceProperty.PUBLICATION_CREATORS,"timothy");


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
                        .add("rss","http://purl.org/rss/1.0/");

                from("direct:start").
                        setHeader(SourceProperty.SOURCE_NAME,                   constant("slashdot")).
                        setHeader(SourceProperty.SOURCE_URI,                    constant("http://www.epnoi.org/feeds/slashdot")).
                        setHeader(SourceProperty.SOURCE_URL,                    constant("http://rss.slashdot.org/Slashdot/slashdot")).
                        setHeader(SourceProperty.SOURCE_PROTOCOL,               constant("rss")).
                        setHeader(SourceProperty.PUBLICATION_TITLE,             xpath("//rss:item/rss:title/text()", String.class).namespaces(ns)).
                        setHeader(SourceProperty.PUBLICATION_DESCRIPTION,       xpath("//rss:item/rss:description/text()", String.class).namespaces(ns)).
                        setHeader(SourceProperty.PUBLICATION_PUBLISHED,         xpath("//rss:item/dc:date/text()", String.class).namespaces(ns)).
                        setHeader(SourceProperty.PUBLICATION_URI,               xpath("//rss:item/rss:link/text()", String.class).namespaces(ns)).
                        setHeader(SourceProperty.PUBLICATION_URL,        xpath("//rss:item/rss:link/text()", String.class).namespaces(ns)).
                        setHeader(SourceProperty.PUBLICATION_LANGUAGE,          xpath("//rss:channel/dc:language/text()", String.class).namespaces(ns)).
                        setHeader(SourceProperty.PUBLICATION_RIGHTS,            xpath("//rss:channel/dc:rights/text()", String.class).namespaces(ns)).
                        setHeader(SourceProperty.PUBLICATION_CREATORS,          xpath("//rss:item/dc:creator/text()", String.class).namespaces(ns)).
                        setHeader(SourceProperty.PUBLICATION_FORMAT,            constant("htm")).
                        setHeader(SourceProperty.PUBLICATION_METADATA_FORMAT,  constant("xml")).
                        to("mock:result");
            }
        };
    }

}
