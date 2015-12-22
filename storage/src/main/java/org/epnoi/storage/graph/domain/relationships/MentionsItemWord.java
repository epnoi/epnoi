package org.epnoi.storage.graph.domain.relationships;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.epnoi.storage.graph.domain.ItemNode;
import org.epnoi.storage.graph.domain.WordNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="MENTIONS")
@Data
@EqualsAndHashCode(exclude={"id"})
public class MentionsItemWord {
    @GraphId
    private Long id;

    @StartNode
    private ItemNode item;

    @EndNode
    private WordNode word;

    @Property
    private Long times;
}
