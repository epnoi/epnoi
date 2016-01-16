package org.epnoi.storage.graph.domain.relationships;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.epnoi.storage.graph.domain.PartNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="SIMILAR_TO")
@Data
@EqualsAndHashCode(exclude={"id"})
public class SimilarPart {
    @GraphId
    private Long id;

    @StartNode
    private PartNode x;

    @EndNode
    private PartNode y;

    @Property
    private Double weight;

    @Property
    private String domain;
}
