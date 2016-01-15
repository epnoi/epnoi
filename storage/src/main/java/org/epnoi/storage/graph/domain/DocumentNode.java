package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.storage.graph.domain.relationships.BundleDocumentItem;
import org.epnoi.storage.graph.domain.relationships.DealsDocumentTopic;
import org.epnoi.storage.graph.domain.relationships.SimilarDocument;
import org.epnoi.storage.model.Document;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Document")
@Data
@EqualsAndHashCode(of={"uri"})
@ToString(of={"uri"})
public class DocumentNode extends Document {

    @GraphId
    private Long id;

    @Index(unique = true)
    private String uri;

    // -> Undirected
    private Set<SimilarDocument> similar = new HashSet<>();

    // -> Outgoing
    private Set<DealsDocumentTopic> deals = new HashSet<>();

    private Set<BundleDocumentItem> bundles = new HashSet<>();


    public void addSimilarRelation(SimilarDocument rel){
        similar.add(rel);
    }

    public void removeSimilarRelation(SimilarDocument rel){
        similar.remove(rel);
    }

    public void addDealRelation(DealsDocumentTopic rel){
        deals.add(rel);
    }

    public void removeDealRelation(DealsDocumentTopic rel){
        deals.remove(rel);
    }

    public void addBundleRelation(BundleDocumentItem rel){
        bundles.add(rel);
    }

    public void removeBundleRelation(BundleDocumentItem rel){
        bundles.remove(rel);
    }

}
