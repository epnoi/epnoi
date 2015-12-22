package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.epnoi.storage.graph.domain.relationships.*;
import org.epnoi.storage.model.Document;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.List;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Document")
@Data
@EqualsAndHashCode(exclude={"id"})
public class DocumentNode extends Document {

    @GraphId
    private Long id;

    @Index
    private String uri;

    // -> Undirected
    private List<SimilarDocument> similar;

    // -> Outgoing
    private List<DealsDocumentTopic> deals;

    private List<BundleDocumentItem> bundle;

    // -> Incoming
    private List<ProvidesSourceDocument> provides;

    private List<ContainsDomainDocument> contains;

}
