package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.storage.graph.domain.relationships.ItemBundledByDocument;
import org.epnoi.storage.graph.domain.relationships.TopicDealtByDocument;
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

    @Relationship(type = "SIMILAR_TO", direction="UNDIRECTED")
    private Set<SimilarDocument> documents = new HashSet<>();

    @Relationship(type = "DEALS_WITH", direction="OUTGOING")
    private Set<TopicDealtByDocument> topics = new HashSet<>();

    @Relationship(type = "BUNDLES", direction="OUTGOING")
    private Set<ItemBundledByDocument> items = new HashSet<>();


    public void addSimilarDocument(SimilarDocument similarDocument){
        documents.add(similarDocument);
    }

    public void addTopicDealtByDocument(TopicDealtByDocument topicDealtByDocument){
        topics.add(topicDealtByDocument);
    }

    public void addItemBundledByDocument(ItemBundledByDocument itemBundledByDocument){
        items.add(itemBundledByDocument);
    }
}
