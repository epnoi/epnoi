package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.storage.graph.domain.relationships.*;
import org.epnoi.storage.model.Word;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Word")
@Data
@EqualsAndHashCode(of={"uri"})
@ToString(of={"uri"})
public class WordNode extends Word {

    @GraphId
    private Long id;

    @Index(unique = true)
    private String uri;

    // Undirected
    private Set<SimilarWord> similars = new HashSet<>();

    // Outgoing
    private Set<EmbeddedWordInDomain> embeddeds = new HashSet<>();

    public void addSimilarRelation(SimilarWord rel){
        similars.add(rel);
    }

    public void removeSimilarRelation(SimilarWord rel){
        similars.remove(rel);
    }

    public void addEmbeddedRelation(EmbeddedWordInDomain rel){
        embeddeds.add(rel);
    }

    public void removeEmergeRelation(EmbeddedWordInDomain rel){
        embeddeds.remove(rel);
    }

}
