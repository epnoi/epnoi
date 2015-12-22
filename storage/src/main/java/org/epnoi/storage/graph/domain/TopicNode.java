package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.storage.graph.domain.relationships.*;
import org.epnoi.storage.model.Topic;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

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
    
    // Outgoing
    private Set<EmergesInTopicDomain> emerges = new HashSet<>();
    private Set<MentionsTopicWord> mentions = new HashSet<>();

    public void addEmergeRelation(EmergesInTopicDomain rel){
        emerges.add(rel);
    }

    public void removeEmergeRelation(EmergesInTopicDomain rel){
        emerges.remove(rel);
    }

    public void addMentionRelation(MentionsTopicWord rel){
        mentions.add(rel);
    }

    public void removeMentionRelation(MentionsTopicWord rel){
        mentions.remove(rel);
    }

}
