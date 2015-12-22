package org.epnoi.storage.graph.domain.relationships;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.epnoi.storage.graph.domain.PartNode;
import org.epnoi.storage.graph.domain.TopicNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="DEALS")
@Data
@EqualsAndHashCode(exclude={"id"})
public class DealsPartTopic {
    @GraphId
    private Long id;

    @StartNode
    private PartNode part;

    @EndNode
    private TopicNode topic;

    @Property
    private Double weight;
}
