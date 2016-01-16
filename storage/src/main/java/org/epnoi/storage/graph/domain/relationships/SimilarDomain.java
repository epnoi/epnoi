package org.epnoi.storage.graph.domain.relationships;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.epnoi.storage.graph.domain.DomainNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="SIMILAR_TO")
@Data
@EqualsAndHashCode(exclude={"id"})
public class SimilarDomain {
    @GraphId
    private Long id;

    @StartNode
    private DomainNode x;

    @EndNode
    private DomainNode y;

    @Property
    private Double weight;
}
