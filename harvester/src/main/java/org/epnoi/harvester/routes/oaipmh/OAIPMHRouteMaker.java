package org.epnoi.harvester.routes.oaipmh;

import org.apache.camel.model.RouteDefinition;
import org.epnoi.harvester.routes.RouteMaker;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 01/12/15.
 */
@Component
public class OAIPMHRouteMaker implements RouteMaker{

    @Override
    public boolean accept(String protocol) {
        return protocol.equalsIgnoreCase("oaipmh");
    }

    @Override
    public RouteDefinition build(String url) {

        //        /*********************************************************************************************************************************
//         * ROUTE 2: OAIPMH
//         *********************************************************************************************************************************/
//        from("file:"+inputDir+"/oaipmh?recursive=true&include=.*\\.xml&doneFileName=\${file:name}.done").
//                to("direct:setCommonOaipmhXpathExpressions").
//                to("seda:createRO")


        return null;
    }
}
