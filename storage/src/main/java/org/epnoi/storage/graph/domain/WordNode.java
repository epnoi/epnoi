package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.epnoi.storage.graph.domain.relationships.*;
import org.epnoi.storage.model.Word;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

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

    @Relationship(type = "PAIRS_WITH", direction="UNDIRECTED")
    private Set<PairedWord> words = new HashSet<>();

    @Relationship(type = "EMBEDDED_IN", direction="OUTGOING")
    private Set<DomainInWord> domains = new HashSet<>();

    public void addPairedWord(PairedWord pairedWord){
        words.add(pairedWord);
    }

    public void addDomainInWord(DomainInWord domainInWord){
        domains.add(domainInWord);
    }
}
