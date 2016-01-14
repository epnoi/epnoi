package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.storage.graph.domain.relationships.*;
import org.epnoi.storage.model.Item;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.*;

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

    // Undirected
    private Set<SimilarItem> similar = new HashSet<>();

    // Outgoing
    private Set<DealsItemTopic> deals =  new HashSet<>();
    private Set<MentionsItemWord> mentions = new HashSet<>();

    public void addSimilarRelation(SimilarItem rel){
        similar.add(rel);
    }

    public void removeSimilarRelation(SimilarItem rel){
        similar.remove(rel);
    }

    public void addDealRelation(DealsItemTopic rel){
        deals.add(rel);
    }

    public void removeDealRelation(DealsItemTopic rel){
        deals.remove(rel);
    }

    public void addMentionRelation(MentionsItemWord rel){
        mentions.add(rel);
    }

    public void removeMentionRelation(MentionsItemWord rel){
        mentions.remove(rel);
    }
}
