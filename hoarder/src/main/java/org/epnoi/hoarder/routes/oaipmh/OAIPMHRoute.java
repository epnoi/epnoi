package org.epnoi.hoarder.routes.oaipmh;

import org.apache.camel.Expression;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.model.RouteDefinition;
import org.epnoi.hoarder.routes.Expressions;
import org.epnoi.hoarder.routes.SourceRoute;
import org.epnoi.hoarder.routes.common.CommonRouteBuilder;
import org.epnoi.model.Record;

/**
 * Created by cbadenes on 27/11/15.
 */
public class OAIPMHRoute implements SourceRoute {


    private final String uri;
    private final String name;
    private final String url;
    private final Expression pubExpression;

    public OAIPMHRoute(String uri, String name, String url, Namespaces ns){
        this.uri    = uri;
        this.name   = name;
        this.url    = url;

        //TODO External Configuration to define this policy
        // setting the expression to download the related pdf file
        switch (url.toLowerCase()){
            case "oaipmh://eprints.ucm.es/cgi/oai2":
            case "oaipmh://dspace.mit.edu/oai/request":
                pubExpression = Expressions.xpath("//oai:metadata/oai:dc/dc:identifier/text()", ns);
                break;
            case "oaipmh://eprints.bournemouth.ac.uk/cgi/oai2":
                pubExpression = Expressions.xpath("substring-before(string-join(//oai:metadata/oai:dc/dc:identifier/text(),\";\"),\";\")", ns);
                break;
            case "oaipmh://ntrs.nasa.gov/oai":
                pubExpression = Expressions.xpath("substring-after(string-join(//oai:metadata/oai:dc/dc:identifier/text(),\";\"),\";\")", ns);
                break;
            case "oaipmh://oa.upm.es/perl/oai2":
                pubExpression = Expressions.xpath("//oai:metadata/oai:dc/dc:relation/text()", ns);
                break;
            default:
                pubExpression = Expressions.xpath("replace(substring-before(concat(string-join(//oai:metadata/oai:dc/dc:relation/text(),\";\"),\";\"),\";\"),\"view\",\"download\")", ns);
                break;
        }

    }
    
    @Override
    public RouteDefinition definition() {
        return new RouteDefinition().
                from(uri).
                setProperty(Record.SOURCE_NAME, Expressions.constant(name)).
                setProperty(Record.SOURCE_URL, Expressions.constant(url)).
                to(OAIPMHRouteBuilder.URI_RETRIEVE_METAINFORMATION).
                setProperty(Record.PUBLICATION_URL, pubExpression).
                to(CommonRouteBuilder.URI_HTTP_DOWNLOAD_TO_FILE);
    }
}
