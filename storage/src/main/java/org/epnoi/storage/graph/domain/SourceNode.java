package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.epnoi.storage.model.Document;
import org.epnoi.storage.model.Source;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Source")
@Data
@EqualsAndHashCode(exclude={"id"})
public class SourceNode extends Source {

    @GraphId
    private Long id;

    @Index
    private String uri;
    


}
