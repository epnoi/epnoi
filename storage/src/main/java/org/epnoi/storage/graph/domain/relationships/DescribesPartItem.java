package org.epnoi.storage.graph.domain.relationships;

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
public class DescribesPartItem {
    @GraphId
    private Long id;

    @StartNode
    private PartNode part;

    @EndNode
    private ItemNode item;

}
