package org.epnoi.storage.graph.domain.relationships;

import org.epnoi.storage.graph.domain.DocumentNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="SIMILAR")
public class SimilarDocument {
    @GraphId
    private Long id;

    @StartNode
    private DocumentNode x;

    @EndNode
    private DocumentNode y;

    @Property
    private Double weight;

    @Property
    private String domain;
}
