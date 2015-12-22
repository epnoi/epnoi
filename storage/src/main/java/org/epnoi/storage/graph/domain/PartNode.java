package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.storage.graph.domain.relationships.*;
import org.epnoi.storage.model.Part;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

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

    // Undirected
    private Set<SimilarPart> similars = new HashSet<>();

    // Outgoing
    private Set<DealsPartTopic> deals = new HashSet<>();
    private Set<MentionsPartWord> mentions = new HashSet<>();
    private Set<DescribesPartItem> describes = new HashSet<>();


    public void addSimilarRelation(SimilarPart rel){
        similars.add(rel);
    }

    public void removeSimilarRelation(SimilarPart rel){
        similars.remove(rel);
    }

    public void addDealRelation(DealsPartTopic rel){
        deals.add(rel);
    }

    public void removeDealRelation(DealsPartTopic rel){
        deals.remove(rel);
    }

    public void addMentionRelation(MentionsPartWord rel){
        mentions.add(rel);
    }

    public void removeMentionRelation(MentionsPartWord rel){
        mentions.remove(rel);
    }

    public void addDescribeRelation(DescribesPartItem rel){
        describes.add(rel);
    }

    public void removeDescribeRelation(DescribesPartItem rel){
        describes.remove(rel);
    }

}
