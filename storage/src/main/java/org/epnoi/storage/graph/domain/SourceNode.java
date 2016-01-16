package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.storage.graph.domain.relationships.DocumentProvidedBySource;
import org.epnoi.storage.model.Source;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Source")
@Data
@EqualsAndHashCode(of={"uri"})
@ToString(of={"uri"})
public class SourceNode extends Source {

    @GraphId
    private Long id;

    @Index(unique = true)
    private String uri;


    @Relationship(type = "PROVIDES", direction="OUTGOING")
    private Set<DocumentProvidedBySource> documents =  new HashSet<>();

    public void addDocumentProvidedBySource(DocumentProvidedBySource documentProvidedBySource){
        documents.add(documentProvidedBySource);
    }


}
