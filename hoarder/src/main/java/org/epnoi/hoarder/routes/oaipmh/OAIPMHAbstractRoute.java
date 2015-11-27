package org.epnoi.hoarder.routes.oaipmh;

import com.google.common.collect.ImmutableMap;
import org.epnoi.hoarder.routes.SourceRoute;

import java.util.Map;

/**
 * Created by cbadenes on 27/11/15.
 */
public abstract class OAIPMHAbstractRoute implements SourceRoute {

    protected Map<String,String> namespaces = ImmutableMap.of(
            "oai", "http://www.openarchives.org/OAI/2.0/",
            "dc", "http://purl.org/dc/elements/1.1/",
            "provenance", "http://www.openarchives.org/OAI/2.0/provenance",
            "oai_dc", "http://www.openarchives.org/OAI/2.0/oai_dc/"
            );
}
