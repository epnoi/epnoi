package org.epnoi.hoarder.routes.rss;

import com.google.common.collect.ImmutableMap;
import org.epnoi.hoarder.routes.SourceRoute;

import java.util.Map;

/**
 * Created by cbadenes on 27/11/15.
 */
public abstract class RSSAbstractRoute implements SourceRoute {

    protected Map<String,String> namespaces = ImmutableMap.of(
            "provenance", "http://www.openarchives.org/OAI/2.0/provenance",
            "rss", "http://purl.org/rss/1.0/"
    );
}