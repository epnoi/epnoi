package org.epnoi.storage.graph.domain.relationships;

import org.epnoi.storage.graph.domain.PartNode;
import org.epnoi.storage.graph.domain.WordNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="MENTIONS")
public class MentionsPartWord {
    @GraphId
    private Long id;

    @StartNode
    private PartNode part;

    @EndNode
    private WordNode word;

    @Property
    private Long times;
}
