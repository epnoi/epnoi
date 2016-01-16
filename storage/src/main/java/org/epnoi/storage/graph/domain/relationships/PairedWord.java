package org.epnoi.storage.graph.domain.relationships;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.epnoi.storage.graph.domain.PartNode;
import org.epnoi.storage.graph.domain.WordNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="PAIRS_WITH")
@Data
@EqualsAndHashCode(exclude={"id"})
public class PairedWord {
    @GraphId
    private Long id;

    @StartNode
    private WordNode x;

    @EndNode
    private WordNode y;

    @Property
    private Double weight;

    @Property
    private String domain;
}
