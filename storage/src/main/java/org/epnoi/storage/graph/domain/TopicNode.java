package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.storage.graph.domain.relationships.*;
import org.epnoi.storage.model.Topic;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Topic")
@Data
@EqualsAndHashCode(of={"uri"})
@ToString(of={"uri"})
public class TopicNode extends Topic {

    @GraphId
    private Long id;

    @Index(unique = true)
    private String uri;

    @Relationship(type = "EMERGES_IN", direction="OUTGOING")
    private Set<DomainInTopic> domains = new HashSet<>();

    @Relationship(type = "MENTIONS", direction="OUTGOING")
    private Set<WordMentionedByTopic> words = new HashSet<>();

    public void addDomainInTopic(DomainInTopic domainInTopic){
        domains.add(domainInTopic);
    }

    public void addWordMentionedByTopic(WordMentionedByTopic wordMentionedByTopic){
        words.add(wordMentionedByTopic);
    }

}
