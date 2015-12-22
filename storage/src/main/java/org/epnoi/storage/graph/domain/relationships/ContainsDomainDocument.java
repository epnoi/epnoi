package org.epnoi.storage.graph.domain.relationships;

import org.epnoi.storage.graph.domain.DocumentNode;
import org.epnoi.storage.graph.domain.DomainNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="CONTAINS")
public class ContainsDomainDocument {
    @GraphId
    private Long id;

    @StartNode
    private DomainNode domain;

    @EndNode
    private DocumentNode document;

    @Property
    private String date;
}
