package org.epnoi.storage.graph.domain.relationships;

import org.epnoi.storage.graph.domain.DocumentNode;
import org.epnoi.storage.graph.domain.ItemNode;
import org.neo4j.ogm.annotation.*;

/**
 * Created by cbadenes on 22/12/15.
 */
@RelationshipEntity(type="BUNDLE")
public class BundleDocumentItem {
    @GraphId
    private Long id;

    @StartNode
    private DocumentNode document;

    @EndNode
    private ItemNode item;

}
