package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.storage.graph.domain.relationships.TopicDealtByItem;
import org.epnoi.storage.graph.domain.relationships.WordMentionedByItem;
import org.epnoi.storage.graph.domain.relationships.SimilarItem;
import org.epnoi.storage.model.Item;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Item")
@Data
@EqualsAndHashCode(of={"uri"})
@ToString(of={"uri"})
public class ItemNode extends Item {

    @GraphId
    private Long id;

    @Index(unique = true)
    private String uri;

    @Relationship(type = "SIMILAR_TO", direction="UNDIRECTED")
    private Set<SimilarItem> items = new HashSet<>();

    @Relationship(type = "DEALS", direction="OUTGOING")
    private Set<TopicDealtByItem> topics =  new HashSet<>();

    @Relationship(type = "MENTIONS", direction="OUTGOING")
    private Set<WordMentionedByItem> words = new HashSet<>();

    public void addSimilarItem(SimilarItem similarItem){
        items.add(similarItem);
    }

    public void addTopicDealtByItem(TopicDealtByItem topicDealtByItem){
        topics.add(topicDealtByItem);
    }

    public void addWordMentionedByItem(WordMentionedByItem wordMentionedByItem){
        words.add(wordMentionedByItem);
    }

}
