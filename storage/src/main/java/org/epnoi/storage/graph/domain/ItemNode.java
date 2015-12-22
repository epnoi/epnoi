package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.epnoi.storage.graph.domain.relationships.*;
import org.epnoi.storage.model.Item;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.List;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Item")
@Data
@EqualsAndHashCode(exclude={"id"})
public class ItemNode extends Item {

    @GraphId
    private Long id;

    @Index
    private String uri;

    // Undirected
    private List<SimilarItem> similar;

    // Outgoing
    private List<DealsItemTopic> deals;
    private List<MentionsItemWord> mentions;

    // Incoming
    private List<BundleDocumentItem> bundle;
    private List<DescribesPartItem> describes;

}
