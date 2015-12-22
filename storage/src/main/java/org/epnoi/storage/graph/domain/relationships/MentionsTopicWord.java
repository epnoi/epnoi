package org.epnoi.storage.graph.domain.relationships;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.epnoi.storage.graph.domain.TopicNode;
import org.epnoi.storage.graph.domain.WordNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="MENTIONS")
@Data
@EqualsAndHashCode(exclude={"id"})
public class MentionsTopicWord {
    @GraphId
    private Long id;

    @StartNode
    private TopicNode topic;

    @EndNode
    private WordNode word;

    @Property
    private Double weight;
}
