package org.epnoi.hoarder;

import com.google.common.base.Joiner;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.epnoi.hoarder.processor.ErrorHandler;
import org.epnoi.hoarder.processor.TimeGenerator;
import org.epnoi.hoarder.processor.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.MalformedURLException;

public abstract class AbstractRouteBuilder extends RouteBuilder {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractRouteBuilder.class);

    private static Joiner joiner                                = Joiner.on(".");
    private static final String EPNOI                           = "epnoi";

    // Time
    public static final String TIME                             = joiner.join(EPNOI,"time");

    // File
    private static final String ARGUMENT                        = joiner.join(EPNOI,"argument");
    public static final String ARGUMENT_NAME                    = joiner.join(ARGUMENT,"name");
    public static final String ARGUMENT_PATH                    = joiner.join(ARGUMENT,"path");

    // Source
    private static final String SOURCE                          = joiner.join(EPNOI,"source");
    public static final String SOURCE_NAME                      = joiner.join(SOURCE,"name");
    public static final String SOURCE_URI                       = joiner.join(SOURCE, "uri");
    public static final String SOURCE_URL                       = joiner.join(SOURCE, "url");
    public static final String SOURCE_PROTOCOL                  = joiner.join(SOURCE,"protocol");

    // Publication
    private static final String PUBLICATION                     = joiner.join(EPNOI,"publication");
    public static final String PUBLICATION_UUID                 = joiner.join(PUBLICATION,"uuid");
    public static final String PUBLICATION_TITLE                = joiner.join(PUBLICATION,"title");
    public static final String PUBLICATION_DESCRIPTION          = joiner.join(PUBLICATION,"description");
    public static final String PUBLICATION_PUBLISHED            = joiner.join(PUBLICATION,"published");
    public static final String PUBLICATION_PUBLISHED_DATE       = joiner.join(PUBLICATION_PUBLISHED,"date");
    public static final String PUBLICATION_PUBLISHED_MILLIS     = joiner.join(PUBLICATION_PUBLISHED,"millis");
    public static final String PUBLICATION_URI                  = joiner.join(PUBLICATION,"uri");
    public static final String PUBLICATION_LANGUAGE             = joiner.join(PUBLICATION,"lang");
    public static final String PUBLICATION_RIGHTS               = joiner.join(PUBLICATION,"rights");
    public static final String PUBLICATION_FORMAT               = joiner.join(PUBLICATION,"format");
    //  -> urls
    public static final String PUBLICATION_URL                 = joiner.join(PUBLICATION,"url");
    public static final String PUBLICATION_URL_LOCAL            = joiner.join(PUBLICATION_URL,"local");

    //  -> reference
    private static final String PUBLICATION_REFERENCE           = joiner.join(PUBLICATION,"reference");
    public static final String PUBLICATION_METADATA_FORMAT = joiner.join(PUBLICATION_REFERENCE,"format");
    public static final String PUBLICATION_REFERENCE_URL        = joiner.join(PUBLICATION_REFERENCE,"url");
    //  -> creators
    public static final String PUBLICATION_CREATORS             = joiner.join(PUBLICATION,"creators"); //CSV


    @Autowired
    protected ErrorHandler errorHandler;

    @Autowired
    protected TimeGenerator timeClock;

    @Autowired
    protected UUIDGenerator uuidGenerator;

    @Value("${storage.path}")
    protected String basedir;


    protected Namespaces ns = new Namespaces("oai", "http://www.openarchives.org/OAI/2.0/")
        .add("dc", "http://purl.org/dc/elements/1.1/")
        .add("provenance", "http://www.openarchives.org/OAI/2.0/provenance")
        .add("oai_dc", "http://www.openarchives.org/OAI/2.0/oai_dc/")
        .add("rss", "http://purl.org/rss/1.0/");

    @Override
    public void configure() throws Exception {

        onException(MalformedURLException.class)
                .process(errorHandler).stop();

        onException(IOException.class)
                .maximumRedeliveries(3)
                .process(errorHandler).stop();

        /*********************************************************************************************************************************
         * -> Save metadata and retrieve resource by Http
         *********************************************************************************************************************************/
        from("direct:retrieveByHttpAndSave").
                process(timeClock).
                process(uuidGenerator).
                setHeader(ARGUMENT_NAME,            simple("${property."+PUBLICATION_UUID+"}."+"${property."+PUBLICATION_METADATA_FORMAT+"}")).
                to("direct:saveToFile").
                setHeader(ARGUMENT_PATH,            simple("${property."+PUBLICATION_URL+"}")).
                to("direct:downloadByHttp").
                setHeader(ARGUMENT_NAME,            simple("${property."+PUBLICATION_UUID+"}."+"${property."+PUBLICATION_FORMAT+"}")).
                to("direct:saveToFile").
                setProperty(PUBLICATION_URL_LOCAL,  simple("${header." + ARGUMENT_PATH + "}"));

        /*********************************************************************************************************************************
         * -> Extract meta-information from RSS
         *********************************************************************************************************************************/
        from("direct:setCommonRssXpathExpressions").
                setProperty(SOURCE_PROTOCOL,            constant("rss")).
                setProperty(SOURCE_URI,                 simple("http://www.epnoi.org/rss/${property."+SOURCE_NAME+"}")).
                setProperty(PUBLICATION_TITLE,          xpath("//rss:item/rss:title/text()", String.class).namespaces(ns)).
                setProperty(PUBLICATION_DESCRIPTION,    xpath("//rss:item/rss:description/text()", String.class).namespaces(ns)).
                setProperty(PUBLICATION_PUBLISHED,      xpath("//rss:item/dc:date/text()", String.class).namespaces(ns)).
                setProperty(PUBLICATION_URI,            xpath("//rss:item/rss:link/text()", String.class).namespaces(ns)).
                setProperty(PUBLICATION_URL,            xpath("//rss:item/rss:link/text()", String.class).namespaces(ns)).
                setProperty(PUBLICATION_LANGUAGE,       xpath("//rss:channel/dc:language/text()", String.class).namespaces(ns)).
                setProperty(PUBLICATION_RIGHTS,         xpath("//rss:channel/dc:rights/text()", String.class).namespaces(ns)).
                setProperty(PUBLICATION_CREATORS,       xpath("string-join(//rss:channel/dc:creator/text(),\";\")", String.class).namespaces(ns)).
                setProperty(PUBLICATION_FORMAT,         constant("htm")).
                setProperty(PUBLICATION_METADATA_FORMAT,constant("xml"));

        /*********************************************************************************************************************************
         * -> Extract meta-information from OAI-PMH
         *********************************************************************************************************************************/
        from("direct:setCommonOaipmhXpathExpressions").
                setProperty(SOURCE_PROTOCOL,                constant("oaipmh")).
                setProperty(SOURCE_URI,                     simple("http://www.epnoi.org/oaipmh/${property." + SOURCE_NAME + "}")).
                setProperty(PUBLICATION_TITLE,              xpath("//oai:metadata/oai:dc/dc:title/text()",String.class).namespaces(ns)).
                setProperty(PUBLICATION_DESCRIPTION,        xpath("//oai:metadata/oai:dc/dc:description/text()",String.class).namespaces(ns)).
                setProperty(PUBLICATION_PUBLISHED,          xpath("//oai:header/oai:datestamp/text()",String.class).namespaces(ns)).
                setProperty(PUBLICATION_URI,                xpath("//oai:header/oai:identifier/text()",String.class).namespaces(ns)).
                setProperty(PUBLICATION_URL,                xpath("//oai:metadata/oai:dc/dc:identifier/text()",String.class).namespaces(ns)).
                setProperty(PUBLICATION_LANGUAGE,           xpath("//oai:metadata/oai:dc/dc:language/text()",String.class).namespaces(ns)).
                setProperty(PUBLICATION_RIGHTS,             xpath("//oai:metadata/oai:dc/dc:rights/text()",String.class).namespaces(ns)).
                setProperty(PUBLICATION_CREATORS,           xpath("string-join(//oai:metadata/oai:dc/dc:creator/text(),\";\")",String.class).namespaces(ns)).
                setProperty(PUBLICATION_FORMAT,             xpath("substring-after(//oai:metadata/oai:dc/dc:format[1]/text(),\"/\")", String.class).namespaces(ns)).
                setProperty(PUBLICATION_METADATA_FORMAT,    constant("xml")).
                to("direct:avoidDeletedMessages");


        /*********************************************************************************************************************************
         * -> Avoid OAI-PMH Deleted Resources
         *********************************************************************************************************************************/
        from("direct:avoidDeletedMessages").
                choice().
                when().xpath("//oai:header[@status=\"deleted\"]", String.class, ns).stop().
                end();

        /*********************************************************************************************************************************
         * -> Save File
         *********************************************************************************************************************************/
        from("direct:saveToFile").
                setHeader(ARGUMENT_PATH, simple("${property." + SOURCE_PROTOCOL + "}/${property." + SOURCE_NAME + "}/${property" + PUBLICATION_PUBLISHED_DATE + "}/${header." + ARGUMENT_NAME + "}")).
                log(LoggingLevel.INFO,LOG,"File Saved: '${header."+ARGUMENT_PATH+"}'").
                to("file:" + basedir + "/?fileName=${header." + ARGUMENT_PATH + "}&doneFileName=${file:name}.done");


        /*********************************************************************************************************************************
         * -> Download Resource by Http
         *********************************************************************************************************************************/
        from("direct:downloadByHttp").
                // Filter resources with available url
                filter(header(ARGUMENT_PATH).isNotEqualTo("")).
                setHeader(Exchange.HTTP_METHOD, constant("GET")).
                setHeader(Exchange.HTTP_URI, simple("${header." + ARGUMENT_PATH + "}")).
                to("http://dummyhost?throwExceptionOnFailure=false");



    }


}
