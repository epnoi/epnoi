package org.epnoi.storage.graph.domain.relationships;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.epnoi.storage.graph.domain.DomainNode;
import org.epnoi.storage.graph.domain.WordNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="EMBEDDED")
@Data
@EqualsAndHashCode(exclude={"id"})
public class EmbeddedWordInDomain {
    @GraphId
    private Long id;

    @StartNode
    private WordNode word;

    @EndNode
    private DomainNode domain;

    @Property
    private String vector;
}
