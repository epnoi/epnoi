package org.epnoi.storage.graph.domain.relationships;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.epnoi.storage.graph.domain.ItemNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="SIMILAR")
@Data
@EqualsAndHashCode(exclude={"id"})
public class SimilarItem {
    @GraphId
    private Long id;

    @StartNode
    private ItemNode x;

    @EndNode
    private ItemNode y;

    @Property
    private Double weight;

    @Property
    private String domain;
}
