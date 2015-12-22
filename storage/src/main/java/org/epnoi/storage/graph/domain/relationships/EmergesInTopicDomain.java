package org.epnoi.storage.graph.domain.relationships;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.epnoi.storage.graph.domain.DomainNode;
import org.epnoi.storage.graph.domain.TopicNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="EMERGES_IN")
@Data
@EqualsAndHashCode(exclude={"id"})
public class EmergesInTopicDomain {
    @GraphId
    private Long id;

    @StartNode
    private TopicNode topic;

    @EndNode
    private DomainNode domain;

    @Property
    private String date;

    @Property
    private String analysis;
}
