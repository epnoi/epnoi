package org.epnoi.hoarder.routes.oaipmh;

import org.apache.camel.Expression;
import org.apache.camel.model.RouteDefinition;
import org.epnoi.hoarder.routes.Expressions;
import org.epnoi.hoarder.routes.SourceProperty;
import org.epnoi.hoarder.routes.common.HttpSaveRoute;

/**
 * Created by cbadenes on 27/11/15.
 */
public class OAIPMHSourceRoute extends OAIPMHAbstractRoute {


    private final String uri;
    private final String name;
    private final String url;
    private final Expression pubExpression;

    public OAIPMHSourceRoute(String uri, String name, String url){
        this.uri    = uri;
        this.name   = name;
        this.url    = url;

        //TODO External Configuration to define this policy
        switch (url.toLowerCase()){
            case "http://eprints.ucm.es/cgi/oai2":
                pubExpression = Expressions.xpath("//oai:metadata/oai:dc/dc:identifier/text()", namespaces);
                break;
            case "http://www.sciencepubco.com/index.php/IJAA/oai":
            case "http://innovareacademics.in/journals/index.php/ijags/oai":
            case "http://innovareacademics.in/journals/index.php/ijas/oai":
                pubExpression = Expressions.xpath("replace(substring-before(concat(string-join(//oai:metadata/oai:dc/dc:relation/text(),\\\";\\\"),\\\";\\\"),\\\";\\\"),\\\"view\\\",\\\"download\\\")", namespaces);
                break;
            default:
                pubExpression = Expressions.xpath("//oai:metadata/oai:dc/dc:relation/text()", namespaces);
                break;
        }

    }
    
    @Override
    public RouteDefinition definition() {
        return new RouteDefinition().
                from(uri).
                setProperty(SourceProperty.SOURCE_NAME, Expressions.constant(name)).
                setProperty(SourceProperty.SOURCE_URL, Expressions.constant(url)).
                to(OAIPMHExtractionRoute.ROUTE).
                setProperty(SourceProperty.PUBLICATION_URL, pubExpression).
                to(HttpSaveRoute.URI);
    }
}
