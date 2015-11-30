package org.epnoi.hoarder.routes.oaipmh;

import org.apache.camel.Expression;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.model.RouteDefinition;
import org.epnoi.hoarder.routes.Expressions;
import org.epnoi.hoarder.routes.SourceProperty;
import org.epnoi.hoarder.routes.SourceRoute;
import org.epnoi.hoarder.routes.common.CommonRouteBuilder;

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
            case "http://eprints.ucm.es/cgi/oai2":
                pubExpression = Expressions.xpath("//oai:metadata/oai:dc/dc:identifier/text()", ns);
                break;
            case "http://www.sciencepubco.com/index.php/IJAA/oai":
            case "http://innovareacademics.in/journals/index.php/ijags/oai":
            case "http://innovareacademics.in/journals/index.php/ijas/oai":
                pubExpression = Expressions.xpath("replace(substring-before(concat(string-join(//oai:metadata/oai:dc/dc:relation/text(),\\\";\\\"),\\\";\\\"),\\\";\\\"),\\\"view\\\",\\\"download\\\")", ns);
                break;
            default:
                pubExpression = Expressions.xpath("//oai:metadata/oai:dc/dc:relation/text()", ns);
                break;
        }

    }
    
    @Override
    public RouteDefinition definition() {
        return new RouteDefinition().
                from(uri).
                setProperty(SourceProperty.SOURCE_NAME, Expressions.constant(name)).
                setProperty(SourceProperty.SOURCE_URL, Expressions.constant(url)).
                to(OAIPMHRouteBuilder.URI_RETRIEVE_METAINFORMATION).
                setProperty(SourceProperty.PUBLICATION_URL, pubExpression).
                to(CommonRouteBuilder.URI_HTTP_DOWNLOAD_TO_FILE);
    }
}
