package org.epnoi.storage.graph.domain.relationships;

import org.epnoi.storage.graph.domain.DocumentNode;
import org.epnoi.storage.graph.domain.SourceNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="PROVIDES")
public class ProvidesSourceDocument {
    @GraphId
    private Long id;

    @StartNode
    private SourceNode source;

    @EndNode
    private DocumentNode document;

    @Property
    private String date;
}
