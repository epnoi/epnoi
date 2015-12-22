package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.epnoi.storage.graph.domain.relationships.ContainsDomainDocument;
import org.epnoi.storage.graph.domain.relationships.EmergesInTopicDomain;
import org.epnoi.storage.graph.domain.relationships.SimilarDomain;
import org.epnoi.storage.model.Domain;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.List;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Domain")
@Data
@EqualsAndHashCode(exclude={"id"})
public class DomainNode extends Domain {

    @GraphId
    private Long id;

    @Index
    private String uri;

    //@Relationship(type="SIMILAR", direction=Relationship.UNDIRECTED)
    // Undirected
    private List<SimilarDomain> similar;

    //@Relationship(type="CONTAINS", direction=Relationship.OUTGOING)
    // Outgoing
    private List<ContainsDomainDocument> contains;

    //@Relationship(type="EMERGES", direction=Relationship.INCOMING)
    // Incoming
    private List<EmergesInTopicDomain> emergesIn;

}
