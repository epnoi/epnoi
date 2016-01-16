package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.storage.graph.domain.relationships.ContainedDocument;
import org.epnoi.storage.graph.domain.relationships.SimilarDomain;
import org.epnoi.storage.model.Domain;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

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

    @Relationship(type = "SIMILAR_TO", direction="UNDIRECTED")
    private Set<SimilarDomain> domains = new HashSet<>();

    @Relationship(type = "CONTAINS", direction="OUTGOING")
    private Set<ContainedDocument> documents = new HashSet<>();


    public void addSimilarDomain(SimilarDomain similarDomain){
        domains.add(similarDomain);
    }

    public void addContainedDocument(ContainedDocument containedDocument){
        documents.add(containedDocument);
    }

}
