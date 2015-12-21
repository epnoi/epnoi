package org.epnoi.hoarder.processor;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.epnoi.hoarder.routes.processors.TimeGenerator;
import org.epnoi.hoarder.utils.FileServer;
import org.epnoi.model.Record;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class HttpRSSTest extends CamelTestSupport {

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
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns=\"http://purl.org/rss/1.0/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:sy=\"http://purl.org/rss/1.0/modules/syndication/\">\n" +
                "  <channel rdf:about=\"http://slashdot.org/\">\n" +
                "    <title>Slashdot</title>\n" +
                "    <link>http://slashdot.org/</link>\n" +
                "    <description>News for nerds, stuff that matters</description>\n" +
                "    <items>\n" +
                "      <rdf:Seq>\n" +
                "        <rdf:li rdf:resource=\"http://entertainment.slashdot.org/story/15/03/05/2325238/musician-releases-album-of-music-to-code-by?utm_source=rss1.0mainlinkanon&amp;utm_medium=feed\" />\n" +
                "      </rdf:Seq>\n" +
                "    </items>\n" +
                "    <sy:updatePeriod>hourly</sy:updatePeriod>\n" +
                "    <sy:updateFrequency>1</sy:updateFrequency>\n" +
                "    <sy:updateBase>1970-01-01T00:00:00Z</sy:updateBase>\n" +
                "    <dc:creator>help@slashdot.org</dc:creator>\n" +
                "    <dc:subject>Technology</dc:subject>\n" +
                "    <dc:publisher>Dice</dc:publisher>\n" +
                "    <dc:date>2015-03-06T13:37:20Z</dc:date>\n" +
                "    <dc:language>en-us</dc:language>\n" +
                "    <dc:rights>Copyright 1997-2015, Dice. All Rights Reserved. Slashdot is a Dice Holdings, Inc. service</dc:rights>\n" +
                "  </channel>\n" +
                "  <image>\n" +
                "    <title>Slashdot</title>\n" +
                "    <url>http://a.fsdn.com/sd/topics/topicslashdot.gif</url>\n" +
                "    <link>http://slashdot.org/</link>\n" +
                "  </image>\n" +
                "  <item rdf:about=\"http://entertainment.slashdot.org/story/15/03/05/2325238/musician-releases-album-of-music-to-code-by?utm_source=rss1.0mainlinkanon&amp;utm_medium=feed\">\n" +
                "    <title>Musician Releases Album of Music To Code By</title>\n" +
                "    <link>http://localhost:8080/rss/resource.htm</link>\n" +
                "    <slash:department xmlns:slash=\"http://purl.org/rss/1.0/modules/slash/\">brian-eno</slash:department>\n" +
                "    <slash:section xmlns:slash=\"http://purl.org/rss/1.0/modules/slash/\">entertainment</slash:section>\n" +
                "    <slash:comments xmlns:slash=\"http://purl.org/rss/1.0/modules/slash/\">135</slash:comments>\n" +
                "    <slash:hit_parade xmlns:slash=\"http://purl.org/rss/1.0/modules/slash/\">135,134,90,70,21,10,3</slash:hit_parade>\n" +
                "    <feedburner:origLink xmlns:feedburner=\"http://rssnamespace.org/feedburner/ext/1.0\">http://entertainment.slashdot.org/story/15/03/05/2325238/musician-releases-album-of-music-to-code-by?utm_source=rss1.0mainlinkanon&amp;utm_medium=feed</feedburner:origLink>\n" +
                "    <description>itwbennett writes Music and programming go hand-in-keyboard. And now programmer/musician Carl Franklin has released an album of music he wrote specifically for use as background music when writing software. \"The biggest challenge was dialing back my instinct to make real music,\" Franklin told ITworld's Phil Johnson. \"This had to fade into the background. It couldn't distract the listener, but it couldn't be boring either. That was a particular challenge that I think most musicians would have found maddening.\"&lt;p&gt;&lt;div class=\"share_submission\" style=\"position:relative;\"&gt;\n" +
                "&lt;a class=\"slashpop\" href=\"http://twitter.com/home?status=Musician+Releases+Album+of+Music+To+Code+By%3A+http%3A%2F%2Fbit.ly%2F1BdhjQq\"&gt;&lt;img src=\"http://a.fsdn.com/sd/twitter_icon_large.png\"&gt;&lt;/a&gt;\n" +
                "&lt;a class=\"slashpop\" href=\"http://www.facebook.com/sharer.php?u=http%3A%2F%2Fentertainment.slashdot.org%2Fstory%2F15%2F03%2F05%2F2325238%2Fmusician-releases-album-of-music-to-code-by%3Futm_source%3Dslashdot%26utm_medium%3Dfacebook\"&gt;&lt;img src=\"http://a.fsdn.com/sd/facebook_icon_large.png\"&gt;&lt;/a&gt;\n" +
                "\n" +
                "&lt;a class=\"nobg\" href=\"http://plus.google.com/share?url=http://entertainment.slashdot.org/story/15/03/05/2325238/musician-releases-album-of-music-to-code-by?utm_source=slashdot&amp;amp;utm_medium=googleplus\" onclick=\"javascript:window.open(this.href,'', 'menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=600,width=600');return false;\"&gt;&lt;img src=\"http://www.gstatic.com/images/icons/gplus-16.png\" alt=\"Share on Google+\"/&gt;&lt;/a&gt;                                                                                                                                                                              \n" +
                "\n" +
                "\n" +
                "\n" +
                "&lt;/div&gt;&lt;/p&gt;&lt;p&gt;&lt;a href=\"http://entertainment.slashdot.org/story/15/03/05/2325238/musician-releases-album-of-music-to-code-by?utm_source=rss1.0moreanon&amp;amp;utm_medium=feed\"&gt;Read more of this story&lt;/a&gt; at Slashdot.&lt;/p&gt;&lt;iframe src=\"http://slashdot.org/slashdot-it.pl?op=discuss&amp;amp;id=7060155&amp;amp;smallembed=1\" style=\"height: 300px; width: 100%; border: none;\"&gt;&lt;/iframe&gt;&lt;img src=\"//feeds.feedburner.com/~r/Slashdot/slashdot/~4/qFYJLyH7aXA\" height=\"1\" width=\"1\" alt=\"\"/&gt;</description>\n" +
                "    <dc:creator>samzenpus</dc:creator>\n" +
                "    <dc:subject>music</dc:subject>\n" +
                "    <dc:date>2015-03-06T03:03:00Z</dc:date>\n" +
                "  </item>\n" +
                "  <atom10:link xmlns:atom10=\"http://www.w3.org/2005/Atom\" rel=\"self\" type=\"application/rdf+xml\" href=\"http://rss.slashdot.org/Slashdot/slashdot\" />\n" +
                "  <feedburner:info xmlns:feedburner=\"http://rssnamespace.org/feedburner/ext/1.0\" uri=\"slashdot/slashdot\" />\n" +
                "  <atom10:link xmlns:atom10=\"http://www.w3.org/2005/Atom\" rel=\"hub\" href=\"http://pubsubhubbub.appspot.com/\" />\n" +
                "</rdf:RDF>\n" +
                "\n";


        resultEndpoint.expectedMessageCount(1);

        template.sendBody(xml);
        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {

                /************************************************************************************************************
                 * RSS Data Providers
                 ************************************************************************************************************/

                TimeGenerator timeClock = new TimeGenerator();

                Namespaces ns = new Namespaces("oai", "http://www.openarchives.org/OAI/2.0/")
                        .add("dc", "http://purl.org/dc/elements/1.1/")
                        .add("provenance", "http://www.openarchives.org/OAI/2.0/provenance")
                        .add("oai_dc", "http://www.openarchives.org/OAI/2.0/oai_dc/")
                        .add("rss", "http://purl.org/rss/1.0/");

                from("direct:start").
                        setProperty(Record.SOURCE_NAME, constant("slashdot")).
                        setProperty(Record.SOURCE_URI, constant("http://www.epnoi.org/feeds/slashdot")).
                        setProperty(Record.SOURCE_URL, constant("http://rss.slashdot.org/Slashdot/slashdot")).
                        setProperty(Record.SOURCE_PROTOCOL, constant("rss")).
                        setProperty(Record.PUBLICATION_TITLE, xpath("//rss:item/rss:title/text()", String.class).namespaces(ns)).
                        setProperty(Record.PUBLICATION_DESCRIPTION, xpath("//rss:item/rss:description/text()", String.class).namespaces(ns)).
                        setProperty(Record.PUBLICATION_PUBLISHED, xpath("//rss:item/dc:date/text()", String.class).namespaces(ns)).
                        setProperty(Record.PUBLICATION_URI, xpath("//rss:item/rss:link/text()", String.class).namespaces(ns)).
                        setProperty(Record.PUBLICATION_URL, xpath("//rss:item/rss:link/text()", String.class).namespaces(ns)).
                        setProperty(Record.PUBLICATION_LANGUAGE, xpath("//rss:channel/dc:language/text()", String.class).namespaces(ns)).
                        setProperty(Record.PUBLICATION_RIGHTS, xpath("//rss:channel/dc:rights/text()", String.class).namespaces(ns)).
                        setProperty(Record.PUBLICATION_CREATORS, xpath("string-join(//rss:channel/dc:creator/text(),\";\")", String.class).namespaces(ns)).
                        setProperty(Record.PUBLICATION_FORMAT, constant("htm")).
                        setProperty(Record.PUBLICATION_METADATA_FORMAT, constant("xml")).
                        to("seda:inbox");



                /************************************************************************************************************
                 * Common retriever and storer
                 ************************************************************************************************************/

                from("seda:inbox").
                        process(timeClock).
                        setProperty(Record.PUBLICATION_REFERENCE_URL,
                                simple("${property." + Record.SOURCE_PROTOCOL + "}/" +
                                        "${property." + Record.SOURCE_NAME + "}/" +
                                        "${property." + Record.PUBLICATION_PUBLISHED_DATE + "}/" +
                                        "resource-${property." + Record.PUBLICATION_PUBLISHED_MILLIS + "}.${property." + Record.PUBLICATION_METADATA_FORMAT + "}")).
                        to("file:target/?fileName=${property." + Record.PUBLICATION_REFERENCE_URL + "}").
                        setHeader(Exchange.HTTP_METHOD, constant("GET")).
                        setHeader(Exchange.HTTP_URI, simple("${property." + Record.PUBLICATION_URL + "}")).
                        log(">>>>>>>>>>>>>>>> ${property." + Record.PUBLICATION_URL + "}").
                        to("http://dummyhost?throwExceptionOnFailure=true&httpClient.soTimeout=5000").
                        setProperty(Record.PUBLICATION_URL_LOCAL,
                                simple("${property." + Record.SOURCE_PROTOCOL + "}/" +
                                        "${property." + Record.SOURCE_NAME + "}/" +
                                        "${property." + Record.PUBLICATION_PUBLISHED_DATE + "}/" +
                                        "resource-${property." + Record.PUBLICATION_PUBLISHED_MILLIS + "}.${property." + Record.PUBLICATION_FORMAT + "}")).
                        to("file:target/?fileName=${property." + Record.PUBLICATION_URL_LOCAL + "}").
                        to("mock:result");
            }
        };
    }

}
