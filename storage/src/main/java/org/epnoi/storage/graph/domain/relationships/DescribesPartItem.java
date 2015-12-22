package org.epnoi.storage.graph.domain.relationships;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.epnoi.storage.graph.domain.ItemNode;
import org.epnoi.storage.graph.domain.PartNode;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="DESCRIBES")
@Data
@EqualsAndHashCode(exclude={"id"})
public class DescribesPartItem {
    @GraphId
    private Long id;

    @StartNode
    private PartNode part;

    @EndNode
    private ItemNode item;

}
