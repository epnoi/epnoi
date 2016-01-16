package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.storage.graph.domain.relationships.*;
import org.epnoi.storage.model.Part;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Part")
@Data
@EqualsAndHashCode(of={"uri"})
@ToString(of={"uri"})
public class PartNode extends Part {

    @GraphId
    private Long id;

    @Index(unique = true)
    private String uri;

    @Relationship(type = "SIMILAR_TO", direction="UNDIRECTED")
    private Set<SimilarPart> parts = new HashSet<>();

    @Relationship(type = "DEALS_WITH", direction="OUTGOING")
    private Set<TopicDealtByPart> topics = new HashSet<>();

    @Relationship(type = "MENTIONS", direction="OUTGOING")
    private Set<WordMentionedByPart> words = new HashSet<>();

    @Relationship(type = "DESCRIBES", direction="OUTGOING")
    private Set<ItemDescribedByPart> items = new HashSet<>();


    public void addSimilarPart(SimilarPart similarPart){
        parts.add(similarPart);
    }

    public void addTopicDealtByPart(TopicDealtByPart topicDealtByPart){
        topics.add(topicDealtByPart);
    }

    public void addWordMentionedByPart(WordMentionedByPart wordMentionedByPart){
        words.add(wordMentionedByPart);
    }

    public void addItemDescribedByPart(ItemDescribedByPart itemDescribedByPart){
        items.add(itemDescribedByPart);
    }

}
