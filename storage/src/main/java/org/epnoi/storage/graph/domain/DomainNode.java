package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.storage.graph.domain.relationships.ContainsDomainDocument;
import org.epnoi.storage.graph.domain.relationships.SimilarDomain;
import org.epnoi.storage.model.Domain;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Domain")
@Data
@EqualsAndHashCode(of={"uri"})
@ToString(of={"uri"})
public class DomainNode extends Domain {

    @GraphId
    private Long id;

    @Index(unique = true)
    private String uri;

    // Undirected
    private Set<SimilarDomain> similars = new HashSet<>();

    // Outgoing
    private Set<ContainsDomainDocument> contains = new HashSet<>();


    public void addSimilarRelation(SimilarDomain rel){
        similars.add(rel);
    }

    public void removeSimilarRelation(SimilarDomain rel){
        similars.remove(rel);
    }

    public void addContainRelation(ContainsDomainDocument rel){
        contains.add(rel);
    }

    public void removeContainRelation(ContainsDomainDocument rel){
        contains.remove(rel);
    }

}
